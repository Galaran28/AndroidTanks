package edu.unh.cs.cs619_2015_project2.g9.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import edu.unh.cs.cs619_2015_project2.g9.R;

/**
 * Created by chris on 11/11/15.
 */

@EBean(scope = EBean.Scope.Singleton)
public class TileUIFactory {
    @RootContext
    Context root;

    TileUI base;

    public TileUIFactory() {
        base = new TileUI();
        base.setImage(new ColorDrawable(Color.TRANSPARENT));
    }

    public TileUI getBullet() { return creatTile(R.drawable.bullet); }
    public TileUI getWallUnbreakable() { return creatTile(R.drawable.wall_unbreakable); }
    public TileUI getWallBreakable() { return creatTile(R.drawable.wall_breakable); }
    public TileUI getTank() { return creatTile(R.drawable.tank_forward); }
    public TileUI getPerson() { return creatTile(R.drawable.our_tank); }
    public TileUI getBlank() {
        return base;
    }

    private TileUI creatTile(int drawable) {
        TileUI ui = new TileUI();
        ui.setImage(ContextCompat.getDrawable(root, drawable));
        return ui;
    }
}