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
 * Represents a image UI element for a tile
 * @Author Chris Sleys
 */
@EBean
public class TileUI {
    public static final String TAG = "TileUI";

    private Drawable image;

    public Drawable display() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }
}
