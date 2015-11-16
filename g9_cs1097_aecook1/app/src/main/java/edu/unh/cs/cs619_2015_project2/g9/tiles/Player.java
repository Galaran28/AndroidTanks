package edu.unh.cs.cs619_2015_project2.g9.tiles;

import edu.unh.cs.cs619_2015_project2.g9.ui.TileUI;
import edu.unh.cs.cs619_2015_project2.g9.ui.TileUIFactory;

/**
 * A tile representing a player
 *
 * @Author Alex Cook
 */
public class Player extends Tank {

    public Player(int integerRepresentation, TileUIFactory uiFactory) {
        super(integerRepresentation, uiFactory);

        TileUI ui = uiFactory.getPlayer();
        ui.setRotation(this.direction);
        this.setUI(ui);
    }
}
