package edu.unh.cs.cs619_2015_project2.g9;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

import edu.unh.cs.cs619_2015_project2.g9.tiles.Tile;
import edu.unh.cs.cs619_2015_project2.g9.tiles.Wall;
import edu.unh.cs.cs619_2015_project2.g9.util.OttoBus;

@EBean
public class GridAdapter extends BaseAdapter {
    public static final String TAG = "GridAdaptor";
    ArrayList<Tile> tiles;

    @Bean
    OttoBus bus;

    @RootContext
    Context context;

    @AfterInject
    public void init() {
        tiles = new ArrayList<Tile>();
        bus.register(this);
    }

    @Bean
    GameGrid game;


    // create a new ImageView for each item referenced by the Adapter
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        else
        {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(getImage(position));
        imageView.setAdjustViewBounds(true);
        return imageView;
    }



   private int getImage(int pos) {
        Tile t = tiles.get(pos);
        if(t.getType() == Tile.WALL) {
            if (((Wall)t).life == Wall.INDESTRUCTIBLE) {
                return R.mipmap.wall_unbreakable;

            } else {
                return R.mipmap.breakable_wall;
            }
        }
        if(t.getType() == Tile.BULLET){
            return  R.mipmap.missile_base_2;
        }
        if(t.getType() == Tile.TANK){
            if(t.getDirection() == 0){
                return  R.mipmap.enemy_up_2;
            }
            if(t.getDirection() == 2){
                return  R.mipmap.enemy_right_2;
            }
            if(t.getDirection() == 4){
                return  R.mipmap.enemy_down_2;
            }
            if(t.getDirection() == 6) {
                return  R.mipmap.enemy_left_2;
            }
        }

        if(t.getType() == Tile.PLAYER){
            if(t.getDirection() == 0){
                return  R.mipmap.player_up_2;
            }
            if(t.getDirection() == 2){
                return  R.mipmap.player_right_2;
            }
            if(t.getDirection() == 4){
                return  R.mipmap.player_down_2;
            }
            if(t.getDirection() == 6) {
                return  R.mipmap.player_left_2;
            }
        }

        if (t.getType() == Tile.TILE) {
            return R.mipmap.tile_base_2;
        }
        return R.mipmap.ic_launcher;
    }



    @Subscribe
    public void updateGrid(Tile[][] board)
    {
        Log.d(TAG, "Updateing GridView");
        tiles.clear(); //clear internal arraylist
        for ( int i = 0; i < board.length; i++) {
            for ( int j = 0; j < board[i].length; j++) {
                tiles.add(board[i][j]);
            }
        }
        this.notifyDataSetChanged(); //refresh view
    }

    @Override
    public int getCount() {
        return tiles.size();
    }

    @Override
    public Object getItem(int position) {
        return tiles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}
