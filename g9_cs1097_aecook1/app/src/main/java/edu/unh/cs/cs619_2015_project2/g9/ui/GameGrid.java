package edu.unh.cs.cs619_2015_project2.g9.ui;

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

    @Background
    @Subscribe
    public void fireBullet(FireEvent f) {
        // TODO: keep track of bullets, cannot fire if 2 or more bullets already exist
        Log.d(TAG, "Firing....");
        if (!hasFired) {
            Log.d(TAG, "Fire allowed....");
            restClient.fire(tankId);
            hasFired = true;
        }
    }

    @Background
    @Subscribe
    public void move(MoveEvent m) {
        // TODO; add constraints, tank can only move in the direction its facing
        Log.d(TAG, "Moving....");

        if (!hasMoved) {
            Log.d(TAG, "Move allowed");
          //  if ( )
            restClient.move(tankId, m.direction);
            hasMoved = true;
        }
    }

    @Background
    @Subscribe
    public void turn(TurnEvent t) {
        Log.d(TAG, "Turning....");
        if (!hasTurned) {
            Log.d(TAG, "Turning allowed");
            restClient.turn(tankId, t.direction);
            hasMoved = true;
        }
    }

    @Subscribe
    public void parseGrid(GridWrapper gw) {
        Log.d(TAG, "parsing grid to Tile array");
        Tile[][] board = new Tile[x][y];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
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

   public long getTankId()
   {
       return tankId;
   }
}