package edu.unh.cs.cs619_2015_project2.g9;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

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
    ArrayList<Tile> tiles;

    @Bean
    OttoBus bus;

    @RootContext
    Context context;

    @AfterInject
    public void init() {
        tiles = new ArrayList<Tile>();
    }

    // create a new ImageView for each item referenced by the Adapter
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
           // imageView.setPadding(8, 8, 8, 8);
        }
        else
        {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(getImage(position));
        return imageView;
    }

    int getImage(int pos) {
        //TODO: add support for bullets
        Tile t = tiles.get(pos);
        if(t.getType() == Tile.WALL) {
            if (((Wall)t).life == Wall.INDESTRUCTIBLE) {
                return R.mipmap.wall_unbreakable;
            } else {
                return R.mipmap.wall_breakable;
            }
        }

        if(t.getType() == Tile.TANK)
        {
            //TODO: rotate image to match tank direction
            return R.mipmap.tank_forward;
        }

        return R.mipmap.blankspace;
    }

    @Subscribe
    public void updateGrid(Tile[][] board)
    {
        tiles.clear(); //clear internal arraylist
        int index = 0;
        for ( int i = 0; i < board.length; i++) {
            for ( int j = 0; j < board[i].length; j++) {
                tiles.add(board[i][j]);
                index++;
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
