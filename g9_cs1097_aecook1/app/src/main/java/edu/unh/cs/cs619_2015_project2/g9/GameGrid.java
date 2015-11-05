package edu.unh.cs.cs619_2015_project2.g9;

import android.util.Log;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.rest.RestService;
import org.springframework.web.client.RestClientException;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import edu.unh.cs.cs619_2015_project2.g9.rest.BulletZoneRestClient;
import edu.unh.cs.cs619_2015_project2.g9.tiles.Tile;
import edu.unh.cs.cs619_2015_project2.g9.tiles.TileFactory;
import edu.unh.cs.cs619_2015_project2.g9.util.GridWrapper;
import edu.unh.cs.cs619_2015_project2.g9.util.LongWrapper;

/**
 * GameGrid represents the game board
 *
 * @Author Chris Sleys
 */
@EBean
public class GameGrid {
    // enumberations for the directions and the turn length
    public static final String TAG = "GameGrid";
    public static final long POL_INTERVAL = 100;
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
    private TileFactory factory;
    private ScheduledExecutorService es;

    @RestService
    BulletZoneRestClient restClient;

    private Tile[][] board;

    public GameGrid() {
        es = Executors.newScheduledThreadPool(3);
        factory = new TileFactory();
        board = new Tile[x][y];

        // update board every POL_INTERVAL milliseconds
        es.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                parseGrid(restClient.grid().getGrid());
            }
        }, 0, POL_INTERVAL, TimeUnit.MILLISECONDS);

        // allow movement actions every MOVE_INTERVAL milliseconds
        es.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                hasMoved = false;
                hasTurned = false;
            }
        }, 0, MOVE_INTERVAL, TimeUnit.MILLISECONDS);

        // allow tank to fire after FIRE_INTERVAL milliseconds
        es.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                hasFired = false;
            }
        }, 0, FIRE_INTERVAL, TimeUnit.MILLISECONDS);
    }

    @AfterInject
    @Background
    protected void afterInjection() {
        try {
            tankId = restClient.join().getResult();
        } catch (RestClientException e) {
            Log.e(TAG, e.toString());
        }

        parseGrid(restClient.grid().getGrid());
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

    private void parseGrid(int[][] grid) {
        for (int i = 0; i <= board.length; i++) {
            for (int j = 0; j <= board[0].length; j++) {
                board[i][j] = factory.createTile(grid[i][j]);
            }
        }
    }

    public Tile[][] getGrid() {
        return board;
    }
}