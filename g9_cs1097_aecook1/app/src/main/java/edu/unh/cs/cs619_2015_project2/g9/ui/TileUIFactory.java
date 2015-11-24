package edu.unh.cs.cs619_2015_project2.g9.ui;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import edu.unh.cs.cs619_2015_project2.g9.R;
import edu.unh.cs.cs619_2015_project2.g9.tiles.Tile;

/**
 * Created by chris on 11/11/15.
 */

@EBean(scope = EBean.Scope.Singleton)
public class TileUIFactory {
    @RootContext
    Context root;

    private TileUI bullet, wallUnbreakable, wallBreakable, blank;
    private TileUI tankUp, tankLeft, tankRight, tankDown;
    private TileUI playerUp, playerDown, playerRight, playerLeft;

    @AfterInject
    public void afterInjection() {
        bullet = createTile(R.mipmap.missile_base_2);
        wallUnbreakable = createTile(R.mipmap.breakable_wall);
        wallBreakable = createTile(R.mipmap.breakable_wall);
        tankUp = createTile(R.mipmap.enemy_up_2);
        tankDown = createTile(R.mipmap.enemy_down_2);
        tankLeft = createTile(R.mipmap.enemy_left_2);
        tankRight = createTile(R.mipmap.enemy_right_2);
        playerUp = createTile(R.mipmap.player_up_2);
        playerDown = createTile(R.mipmap.player_down_2);
        playerLeft = createTile(R.mipmap.player_left_2);
        playerRight = createTile(R.mipmap.player_right_2);
        blank = createTile(R.mipmap.tile_base_2);
    }

    public TileUI getBullet() { return bullet; }
    public TileUI getWallUnbreakable() { return wallUnbreakable; }
    public TileUI getWallBreakable() { return wallBreakable; }
    public TileUI getTank(int rotation) {
        switch (rotation) {
            case Tile.UP:
                return tankUp;
            case Tile.DOWN:
                return tankDown;
            case Tile.LEFT:
                return tankLeft;
            case Tile.RIGHT:
                return tankRight;
            default:
                return tankUp;
        }
    }
    public TileUI getPlayer(int rotation) {
        switch (rotation) {
            case Tile.UP:
                return playerUp;
            case Tile.DOWN:
                return playerDown;
            case Tile.LEFT:
                return playerLeft;
            case Tile.RIGHT:
                return playerRight;
            default:
                return playerUp;
        }
    }
    public TileUI getBlank() { return blank; }

    private TileUI createTile(int drawable) {
        TileUI ui = new TileUI();
        ui.setImage(ContextCompat.getDrawable(root, drawable));
        return ui;
    }
}