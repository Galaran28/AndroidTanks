package edu.unh.cs.cs619_2015_project2.g9.tiles;

import org.androidannotations.annotations.EBean;

/**
 * Tile base class
 *
 * @Author Chris Sleys
 */
public class Tile {
    public static final int TILE = 0;
    public static final int WALL = 1;
    public static final int BULLET = 2;
    public static final int TANK = 3;
    public static final int PLAYER = 4;

    public int getType() {
        return this.TILE;
    }
    public int getDirection()
    {
        return 0;
    };
    public int getId()
    {
        return 0;
    };
}
