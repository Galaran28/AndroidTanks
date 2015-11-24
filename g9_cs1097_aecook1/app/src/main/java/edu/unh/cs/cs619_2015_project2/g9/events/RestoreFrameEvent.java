package edu.unh.cs.cs619_2015_project2.g9.events;

/**
 * Created by chris on 11/24/15.
 */
public class RestoreFrameEvent {
    public int[][] frame;

    public RestoreFrameEvent(int[][] f) {
        this.frame = f;
    }
}
