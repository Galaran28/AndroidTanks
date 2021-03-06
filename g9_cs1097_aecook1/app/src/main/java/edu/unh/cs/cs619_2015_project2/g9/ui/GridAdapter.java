package edu.unh.cs.cs619_2015_project2.g9.ui;

import android.content.Context;
import android.support.annotation.UiThread;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;

import edu.unh.cs.cs619_2015_project2.g9.tiles.Tile;
import edu.unh.cs.cs619_2015_project2.g9.util.OttoBus;

@EBean
public class GridAdapter extends BaseAdapter {
    public static final String TAG = "GridAdaptor";
    ArrayList<Tile> tiles, old;

    @AfterInject
    public void init() {
        Log.d(TAG, "init");
        tiles = new ArrayList<>();
        bus.register(this);
    }

    @Bean
    OttoBus bus;

    @RootContext
    Context context;

    // create a new ImageView for each item referenced by the Adapter
    @Override
    @UiThread
    public View getView(final int position, View convertView, ViewGroup parent) {
        //Log.d(TAG, "getView");
        final ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageDrawable(tiles.get(position).getUI().display());
        imageView.setAdjustViewBounds(true);
        return imageView;
    }

    @Subscribe
    @UiThread
    public void updateGrid(Tile[][] board) {
        Log.d(TAG, "Updating GridView");
        tiles.clear(); //clear internal arraylist
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
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

    public void release() {
        bus.unregister(this);
    }
}
