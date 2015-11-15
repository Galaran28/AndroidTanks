package edu.unh.cs.cs619_2015_project2.g9.tiles;

/**
 * A tile representing a player
 *
 * @Author Alex Cook
 */
public class Player extends Tile {
    public int life;
    public int direction;
    public int id;

    public Player(int integerRepresentation) {
        String parsable = Integer.toString(integerRepresentation);
        id = Integer.parseInt(parsable.substring(1, 4));
        life =  Integer.parseInt(parsable.substring(4, 6));
        direction = Integer.parseInt(parsable.substring(7));
    }

    @Override
    public int getType() {
        return this.PLAYER;
    }

    public int getDirection()
    {
        return direction;
    }

    public int getId()
    {
        return id;
    }
}
