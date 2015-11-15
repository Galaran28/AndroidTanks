package edu.unh.cs.cs619_2015_project2.g9;

import android.app.Activity;
import android.os.SystemClock;
import android.util.Log;

import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.rest.RestService;
import org.springframework.web.client.RestClientException;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import edu.unh.cs.cs619_2015_project2.g9.TankClientActivity;
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
    public static final byte UP = 0;
    public static final byte DOWN = 4;
    public static final byte LEFT = 6;
    public static final byte RIGHT = 2;
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

    @Background
    public void fireBullet() {
        Log.d(TAG, "Fireing....");
        if (playerAlive)
        if (!hasFired && missilesFired < 2) {
            Log.d(TAG, "Fire allowed....");
            restClient.fire(tankId);
            hasFired = true;
        }
    }

    @Background
    @Subscribe
    public void move(byte direction) {
        // TODO; add constraints, tank can only move in the direction its facing
        Log.d(TAG, "Moving....");

        if (!hasMoved && playerAlive) {
            Log.d(TAG, "Move allowed");
            if (playerDirection == direction) {
                restClient.move(tankId, direction);
                hasMoved = true;
            }
            else
            {
                turn (direction);
            }
        }
    }

    @Background
    public void turn(byte direction) {
        Log.d(TAG, "Turning....");
        if (!hasTurned && playerAlive) {
            Log.d(TAG, "Turning allowed");
            restClient.turn(tankId, direction);
            hasMoved = true;
        }
    }

    @Subscribe
    public void parseGrid(GridWrapper gw) {
        Log.d(TAG, "parsing grid to Tile array");
        Tile[][] board = new Tile[x][y];
        int missiles = 0;
        boolean foundPlayer = false;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
             //   Log.e(TAG, "invalid value in grid " + gw.getGrid()[i][j]);
                board[i][j] = factory.createTile(gw.getGrid()[i][j], tankId);
                if (board[i][j].getType() == Tile.BULLET) {
                    missiles++;
                }
                if (board[i][j].getType() == Tile.PLAYER){
                    foundPlayer = true;
                    playerAlive = true;
                    playerDirection = (byte)board[i][j].getDirection();

                }
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

}