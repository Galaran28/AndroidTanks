package edu.unh.cs.cs619_2015_project2.g9.tiles;

/**
 * A tile representing a tank
 *
 * @Author Chris Sleys
 */
public class Tank extends Tile {
    public int life;
    public int direction;
    public int id;

    public Tank(int integerRepresentation) {
        String parsable = Integer.toString(integerRepresentation);
        id = Integer.parseInt(parsable.substring(1, 4));
        life =  Integer.parseInt(parsable.substring(4, 6));
        direction = Integer.parseInt(parsable.substring(7));
    }

    @Override
    public int getType() {
        return this.TANK;
    }

    public int getDirection()
    {
        return direction;
    }
    @Override
    public int getId()
    {
        return id;
    }
}
