package edu.unh.cs.cs619_2015_project2.g9.tiles;

import org.androidannotations.annotations.Bean;

import edu.unh.cs.cs619_2015_project2.g9.ui.TileUIFactory;

/**
 * A tile representing a bullet
 *
 * @Author Chris Sleys
 */
public class Bullet extends Tile {
    public int sourceTank, damage;

    public Bullet(int integerRepresentation, TileUIFactory uiFactory) {
        String parsable = Integer.toString(integerRepresentation);
        sourceTank = Integer.parseInt(parsable.substring(1, 3));
        damage = Integer.parseInt(parsable.substring(4, 6));

        this.setUI(uiFactory.getBullet());
    }
}
