package edu.unh.cs.cs619_2015_project2.g9.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.media.Image;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import edu.unh.cs.cs619_2015_project2.g9.R;
import edu.unh.cs.cs619_2015_project2.g9.tiles.Tile;

/**
 * Created by chris on 11/11/15.
 */
@EBean
public class TileUI {
    @RootContext
    Context root;

    private Drawable image;

    public TileUI() {
        setImage(R.drawable.blankspace);
    }

    public void setImage(int drawable) {
        image = ContextCompat.getDrawable(root, drawable);
    }

    public Drawable getImage() {
       return image;
    }
}
