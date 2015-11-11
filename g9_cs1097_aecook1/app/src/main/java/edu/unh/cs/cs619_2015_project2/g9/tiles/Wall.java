package edu.unh.cs.cs619_2015_project2.g9.tiles;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import edu.unh.cs.cs619_2015_project2.g9.ui.TileUI;
import edu.unh.cs.cs619_2015_project2.g9.ui.TileUIFactory;

/**
 * A tile representing a wall, a life value of -1 is a indestructible wall
 *
 * @Author Chris Sleys
 */
public class Wall extends Tile {
    public static final int INDESTRUCTIBLE = -1;
    public int life;

    public Wall(int integerRepresentation, TileUIFactory uiFactory) {
        if (integerRepresentation == 1000) {
            life = -1;
            this.setUI(uiFactory.getWallUnbreakable());
        } else {
            life = integerRepresentation - 1000;
            this.setUI(uiFactory.getWallBreakable());
        }
    }
}
