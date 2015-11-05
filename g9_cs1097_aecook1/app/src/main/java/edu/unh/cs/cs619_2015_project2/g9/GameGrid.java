package edu.unh.cs.cs619_2015_project2.g9;

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
        // TODO: keep track of bullets, cannot fire if 2 or more bullets already exist
        // TODO: shake to fire bullets
        if (!hasFired) {
            restClient.fire(tankId);
            hasFired = true;
        }
    }

    @Background
    public void move(byte direction) {
        // TODO; add constraints, tank can only move in the direction its facing
        if (!hasMoved) {
            restClient.move(tankId, direction);
            hasMoved = true;
        }
    }

    @Background
    public void turn(byte direction) {
        if (!hasTurned) {
            restClient.turn(tankId, direction);
            hasMoved = true;
        }
    }

    @Subscribe
    public void parseGrid(GridWrapper gw) {
        Tile[][] board = new Tile[x][y];
        for (int i = 0; i <= board.length; i++) {
            for (int j = 0; j <= board[i].length; j++) {
                board[i][j] = factory.createTile(gw.getGrid()[i][j]);
            }
        }

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