package edu.unh.cs.cs619_2015_project2.g9.tiles;

import org.androidannotations.annotations.Bean;

import edu.unh.cs.cs619_2015_project2.g9.ui.TileUI;
import edu.unh.cs.cs619_2015_project2.g9.ui.TileUIFactory;

/**
 * A tile representing a tank
 *
 * @Author Chris Sleys
 */
public class Tank extends Tile {
    public int life;
    public byte direction;
    public int id;

    public Tank(int integerRepresentation, TileUIFactory uiFactory) {
        String parsable = Integer.toString(integerRepresentation);
        id = Integer.parseInt(parsable.substring(1, 3));
        life =  Integer.parseInt(parsable.substring(4, 6));
        direction = Byte.parseByte(parsable.substring(7));

        this.setUI(uiFactory.getTank());
        TileUI ui = uiFactory.getTank();
        ui.setRotation(direction);
        this.setUI(ui);
    }

    public Tank() {};
}
