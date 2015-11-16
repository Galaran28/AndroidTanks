package edu.unh.cs.cs619_2015_project2.g9.tiles;

import android.graphics.drawable.Drawable;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import edu.unh.cs.cs619_2015_project2.g9.ui.TileUI;
import edu.unh.cs.cs619_2015_project2.g9.ui.TileUIFactory;

/**
 * Tile base class
 *
 * @Author Chris Sleys
 */
public class Tile {
    public static final byte UP = 0;
    public static final byte DOWN = 4;
    public static final byte LEFT = 6;
    public static final byte RIGHT = 2;

    private TileUI ui;

    public Tile() {};
    public Tile(TileUIFactory uiFactory) {
        setUI(uiFactory.getBlank());
    }

    public TileUI getUI() {
        return ui;
    }

    public void setUI(TileUI ui) {
       this.ui = ui;
    }
}
