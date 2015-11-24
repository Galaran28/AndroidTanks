package edu.unh.cs.cs619_2015_project2.g9.events;

/**
 * Signal a turn event and indicate desiered direction
 * @Author Chris Sleys
 */
public class TurnEvent {
    public byte direction;

    public TurnEvent(byte direction) {
        this.direction = direction;
    }
}
