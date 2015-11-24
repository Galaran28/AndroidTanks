package edu.unh.cs.cs619_2015_project2.g9.events;

/**
 * Signals a move event, indicates the direction that would like to be moved in
 * @Author Chris Sleys
 */
public class MoveEvent {
    public byte direction;

    public MoveEvent(byte direction) {this.direction = direction;}
}
