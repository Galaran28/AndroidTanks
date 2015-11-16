package edu.unh.cs.cs619_2015_project2.g9.ui;

import android.content.Context;
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

    public TileUI getBullet() { return createTile(R.mipmap.missile_4); }
    public TileUI getWallUnbreakable() { return createTile(R.mipmap.breakable_wall); }
    public TileUI getWallBreakable() { return createTile(R.mipmap.breakable_wall); }
    public TileUI getTank() { return createTile(R.mipmap.enemy_up); }
    public TileUI getPlayer() { return createTile(R.mipmap.player_up_1); }
    public TileUI getBlank() { return createTile(R.mipmap.tile_base); }

    private TileUI createTile(int drawable) {
        TileUI ui = new TileUI();
        ui.setImage(ContextCompat.getDrawable(root, drawable));
        return ui;
    }
}