package edu.unh.cs.cs619_2015_project2.g9.events;

/**
 * Singal the start of a replay, contains the scaling factor to determine how fast to reply
 * @Author Chris Sleys
 */
public class BeginReplayEvent {
    public int speedFactor;

    public BeginReplayEvent(int speedFactor) {
        this.speedFactor = speedFactor;
    }
}
