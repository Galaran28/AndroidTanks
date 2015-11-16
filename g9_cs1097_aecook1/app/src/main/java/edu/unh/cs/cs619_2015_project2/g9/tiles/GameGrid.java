package edu.unh.cs.cs619_2015_project2.g9.tiles;

import android.app.Activity;
import android.os.SystemClock;
import android.util.Log;

import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.rest.RestService;
import org.springframework.web.client.RestClientException;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import edu.unh.cs.cs619_2015_project2.g9.TankClientActivity;
import edu.unh.cs.cs619_2015_project2.g9.events.FireEvent;
import edu.unh.cs.cs619_2015_project2.g9.events.MoveEvent;
import edu.unh.cs.cs619_2015_project2.g9.events.TurnEvent;
import edu.unh.cs.cs619_2015_project2.g9.rest.BulletZoneRestClient;
import edu.unh.cs.cs619_2015_project2.g9.rest.PollerTask;
import edu.unh.cs.cs619_2015_project2.g9.tiles.Tile;
import edu.unh.cs.cs619_2015_project2.g9.tiles.TileFactory;
import edu.unh.cs.cs619_2015_project2.g9.util.GridWrapper;
import edu.unh.cs.cs619_2015_project2.g9.util.OttoBus;

/**
 * GameGrid represents the game board
 *
 * @Author Chris Sleys
 */
@EBean
public class GameGrid {
    // enumberations for the directions and the turn length
    public static final String TAG = "GameGrid";
    public static final int POL_INTERVAL = 100;
    public static final int MOVE_INTERVAL = 500;
    public static final int FIRE_INTERVAL = 500;
    public static final int MAX_BULLETS = 2;
    public static final int x = 16;
    public static final int y = 16;

    private long tankId;
    private boolean hasFired, hasMoved, hasTurned;
    private int missilesFired = 0;
    private boolean playerAlive = true;
    private byte playerDirection;

    @Bean
    protected OttoBus bus;

    @Bean
    protected PollerTask poller;

    @Bean
    protected TileFactory factory;

    @RestService
    BulletZoneRestClient restClient;

    @AfterInject
    @Background
    protected void afterInjection() {
        Log.d(TAG, "afterInjection");
        bus.register(this);

        try {
            tankId = restClient.join().getResult();
        } catch (RestClientException e) {
            Log.e(TAG, e.toString());
        }

        // update board every POL_INTERVAL milliseconds
        poller.doPoll(POL_INTERVAL);
        // allow movement actions every MOVE_INTERVAL milliseconds
        this.doMoveTracker();
        // allow tank to fire after FIRE_INTERVAL milliseconds
        this.doFireTracker();

    }

    /**
     * Calls the rest client's fire bullet
     * Will only perform action if player is alive and there are less than 2 bullets
     *
     * @Author Chris Sleys
     * @Author Alex Cook
     */
    @Background
    @Subscribe
    public void fireBullet(FireEvent f) {
        // TODO: keep track of bullets, cannot fire if 2 or more bullets already exist
        Log.i(TAG, "Firing....");
        if (!hasFired && missilesFired < MAX_BULLETS) {
            Log.i(TAG, "Fire allowed....");
            restClient.fire(tankId);
            hasFired = true;
        }
    }

    /**
     * Calls the rest client's move endpoint
     * Will only execute if the player is alive, the move is on the tanks axis, and the players move
     * cooldown is complete
     *
     * @Author Chris Sleys
     * @Author Alex Cook
     */
    @Background
    @Subscribe
    public void move(MoveEvent m) {
        Log.i(TAG, "Moving....");

        if (!hasMoved && playerAlive) {
            Log.i(TAG, "Move allowed");
            if (m.direction == Tile.UP) {
                restClient.move(tankId, playerDirection);
            } else {
                restClient.move(tankId, oppositeDirection(playerDirection));
            }
            hasMoved = true;
        }
    }

    /**
     * Calls the rest client's turn
     * This is called from the gamegrid move method
     * Will only perform action if player is alive and can make movements
     *
     * @Author Cris Sleys
     * Edited by Alex
     */
    @Background
    @Subscribe
    public void turn(TurnEvent t) {
        Log.i(TAG, "Turning....");
        if (!hasTurned && playerAlive) {
            Log.i(TAG, "Turning allowed");
            if (t.direction == Tile.LEFT) {
                restClient.turn(tankId, getLeftDir(playerDirection));
            } else {
                restClient.turn(tankId, getRightDir(playerDirection));
            }
            hasMoved = true;
        }
    }


    /**
     *
     * NOT SURE WHERE CALLED FROM
     *
     *
     *
     */
    @Subscribe
    public void parseGrid(GridWrapper gw) {
        Log.d(TAG, "parsing grid to Tile array");
        Tile[][] board = new Tile[x][y];
        int missiles = 0;
        boolean foundPlayer = false;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                //   Log.e(TAG, "invalid value in grid " + gw.getGrid()[i][j]);
                Tile t = factory.createTile(gw.getGrid()[i][j]);
                if (t instanceof Bullet) {
                    missiles++;
                }
                if (t instanceof Tank) {
                    Tank tank = (Tank) t;
                    if (tank.id == tankId) {
                        tank.setPlayer();
                        foundPlayer = true;
                        playerAlive = true;
                        playerDirection = tank.direction;
                    }
                }

                board[i][j] = t;
            }
        }
        if (!foundPlayer) {
            playerAlive = false;
            //  Log.e(TAG, "could not find player: " + tankId);
            //   restClient.leave(tankId);
        }
        missilesFired = missiles;
        bus.post(board);
    }

    @Background
    public void doMoveTracker() {
        while(true) {
            hasMoved = false;
            hasTurned = false;
            SystemClock.sleep(MOVE_INTERVAL);
        }
    }

    @Background
    public void doFireTracker() {
        while(true) {
            hasFired = false;
            SystemClock.sleep(FIRE_INTERVAL);
        }
    }

    private byte oppositeDirection(byte d) {
        switch(d) {
            case Tile.UP:
                return Tile.DOWN;
            case Tile.DOWN:
                return Tile.UP;
            case Tile.LEFT:
                return Tile.RIGHT;
            case Tile.RIGHT:
                return Tile.LEFT;
            default:
                return 100;
        }
    }

    private byte getLeftDir(byte d) {
        switch(d) {
            case Tile.UP:
                return Tile.LEFT;
            case Tile.DOWN:
                return Tile.RIGHT;
            case Tile.LEFT:
                return Tile.DOWN;
            case Tile.RIGHT:
                return Tile.UP;
            default:
                return 100;
        }
    }

    private byte getRightDir(byte d) {
        return oppositeDirection(getRightDir(d));
    }
}