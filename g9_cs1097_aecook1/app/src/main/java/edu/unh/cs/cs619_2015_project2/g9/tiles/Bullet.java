package edu.unh.cs.cs619_2015_project2.g9.tiles;

/**
 * A tile representing a bullet
 *
 * @Author Chris Sleys
 */
public class Bullet extends Tile {
    public int sourceTank, damage;

    public Bullet(int integerRepresentation) {
        String parsable = Integer.toString(integerRepresentation);
        sourceTank = Integer.parseInt(parsable.substring(1, 3));
        damage = Integer.parseInt(parsable.substring(4, 6));
    }

    public int getType() {
        return this.BULLET;
    }
}
