package edu.unh.cs.cs619_2015_project2.g9.rest;

import android.os.SystemClock;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.rest.RestService;

import android.util.Log;

import com.squareup.otto.Subscribe;

import edu.unh.cs.cs619_2015_project2.g9.events.BeginReplayEvent;
import edu.unh.cs.cs619_2015_project2.g9.restore.ElementChange;
import edu.unh.cs.cs619_2015_project2.g9.restore.GridChange;
import edu.unh.cs.cs619_2015_project2.g9.tiles.GameGrid;
import edu.unh.cs.cs619_2015_project2.g9.util.GridWrapper;
import edu.unh.cs.cs619_2015_project2.g9.util.OttoBus;

/**
 * PollerTask polles the Rest API at regular intervals for changes to the grid and pulishes them to
 * the bus.
 *
 * @Author Chris Sleys
 * @Author Karen Jin
 */
@EBean
public class PollerTask {
    private static final String TAG = "GridPollerTask";
    private boolean enabled = true;

    @Bean
    protected OttoBus bus;

    @RestService
    BulletZoneRestClient restClient;

    int[][] current;

    @AfterInject
    public void init() {
        current = new int[GameGrid.x][GameGrid.y];
        bus.register(this);
    }

    /**
     * Get new grid from API every pollInterval millisecons
     *
     * @param pollInterval interval in milliseconds between API calles
     */
    @Background(id = "grid_poller_task")
    public void doPoll(int pollInterval) {
        while (enabled) {
            onGridUpdate(restClient.grid());
            SystemClock.sleep(pollInterval);
        }
    }

    /**
     * Parse a gridwrapper object and push a notifcation of changes
     *
     * @param gw GridWapper object from API
     */
    @UiThread
    public void onGridUpdate(GridWrapper gw) {
        Log.d(TAG, "grid at timestamp: " + gw.getTimeStamp());

        int[][] next = gw.getGrid();
        GridChange changes = diff(next);

        // no changes to record
        if (changes.length() <= 0) {
            Log.d(TAG, "No changes");
            return;
        }
        changes.timestamp = gw.getTimeStamp();
        current = next;

        bus.post(changes);
    }

    /**
     * Determine what has changed between the new grid and the reference grid
     *
     * @param next new int grid
     * @return GridChange object with list of changes
     */
    private GridChange diff(int[][] next) {
        GridChange changes = new GridChange();
        for (int i = 0; i < current.length; i++) {
            for (int j = 0; j < current[i].length; j++) {
                if (current[i][j] == next[i][j]) {
                    continue;
                }

                changes.add(new ElementChange(i, j, next[i][j]));
            }
        }
        return changes;
    }

    /**
     * When the replay begins disable the poller thread
     * @param e the reply initilizer
     */
    @Subscribe
    public void suspend(BeginReplayEvent e) {
        enabled = false;
    }

    public void release() {
        enabled = false;
        bus.unregister(this);
    }
}
