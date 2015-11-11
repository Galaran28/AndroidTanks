package edu.unh.cs.cs619_2015_project2.g9.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.media.Image;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ImageView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.ViewById;

import edu.unh.cs.cs619_2015_project2.g9.R;
import edu.unh.cs.cs619_2015_project2.g9.tiles.Tile;

/**
 * Created by chris on 11/11/15.
 */
@EBean
public class TileUI {
    public static final String TAG = "TileUI";

    private Drawable image;
    private float rotation;

    public Drawable display() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public void setRotation(byte direction) {
        switch(direction) {
            case(Tile.UP):
                rotation = 0;
                break;
            case(Tile.DOWN):
                rotation = 180;
                break;
            case(Tile.LEFT):
                rotation = 270;
                break;
            case(Tile.RIGHT):
                rotation = 90;
                break;
        }
    }

    public float getRotation() {
        return rotation;
    }
}
