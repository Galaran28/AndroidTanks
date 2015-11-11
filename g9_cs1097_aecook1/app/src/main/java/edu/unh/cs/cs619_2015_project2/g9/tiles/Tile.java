package edu.unh.cs.cs619_2015_project2.g9.tiles;

import android.graphics.drawable.Drawable;

import org.androidannotations.annotations.EBean;

import edu.unh.cs.cs619_2015_project2.g9.ui.TileUI;

/**
 * Tile base class
 *
 * @Author Chris Sleys
 */
public class Tile {
    private TileUI ui;
    public static final byte UP = 0;
    public static final byte DOWN = 4;
    public static final byte LEFT = 6;
    public static final byte RIGHT = 2;

    public Tile() {
        setUI(new TileUI());
    }

    public Drawable display() {
        return ui.getImage();
    }

    public void setUI(TileUI ui) {
       this.ui = ui;
    }

    public TileUI getUI() {
        return this.ui;
    }
}
