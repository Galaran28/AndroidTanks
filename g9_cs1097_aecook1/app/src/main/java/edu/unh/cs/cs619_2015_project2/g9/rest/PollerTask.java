package edu.unh.cs.cs619_2015_project2.g9.rest;

/**
 * Created by karenjin on 10/21/15.
 */

import android.os.SystemClock;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.rest.RestService;

import android.util.Log;

import edu.unh.cs.cs619_2015_project2.g9.util.GridWrapper;
import edu.unh.cs.cs619_2015_project2.g9.util.OttoBus;

@EBean
public class PollerTask {
    private static final String TAG = "GridPollerTask";

    @Bean
    protected OttoBus bus;

    @RestService
    BulletZoneRestClient restClient;

    @Background(id = "grid_poller_task")
    public void doPoll(int pollInterval) {
        while (true) {
            onGridUpdate(restClient.grid());
            SystemClock.sleep(pollInterval);
        }
    }

    @UiThread
    public void onGridUpdate(GridWrapper gw) {
        Log.d(TAG, "grid at timestamp: " + gw.getTimeStamp());

        bus.post(gw);
    }
}
