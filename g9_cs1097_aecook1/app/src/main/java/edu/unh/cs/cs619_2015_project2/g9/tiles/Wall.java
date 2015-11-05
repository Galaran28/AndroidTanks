package edu.unh.cs.cs619_2015_project2.g9.tiles;

/**
 * A tile representing a wall, a life value of -1 is a indestructible wall
 *
 * @Author Chris Sleys
 */
public class Wall extends Tile {
    public static final int INDESTRUCTIBLE = -1;
    public int life;

    public Wall(int integerRepresentation) {
        if (integerRepresentation == 1000) {
            life = -1;
        } else {
            life = integerRepresentation - 1000;
        }
    }

    public int getType() {
        return this.WALL;
    }
}
