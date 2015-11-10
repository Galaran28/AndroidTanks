package edu.unh.cs.cs619_2015_project2.g9.ui;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;

import edu.unh.cs.cs619_2015_project2.g9.R;
import edu.unh.cs.cs619_2015_project2.g9.events.MoveEvent;
import edu.unh.cs.cs619_2015_project2.g9.tiles.Tile;
import edu.unh.cs.cs619_2015_project2.g9.tiles.Wall;
import edu.unh.cs.cs619_2015_project2.g9.ui.GameGrid;
import edu.unh.cs.cs619_2015_project2.g9.util.OttoBus;

@EBean
public class GridAdapter extends BaseAdapter {
    public static final String TAG = "GridAdaptor";
    ArrayList<Tile> tiles;

    @AfterInject
    public void init() {
        tiles = new ArrayList<Tile>();
        bus.register(this);
    }

    @Bean
    OttoBus bus;

    @RootContext
    Context context;

    // create a new ImageView for each item referenced by the Adapter
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    move(position);
                }
            });
        }
        else
        {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(getImage(position));
        imageView.setAdjustViewBounds(true);
        return imageView;
    }

    int getImage(int pos) {
        Tile t = tiles.get(pos);
        if(t.getType() == Tile.WALL) {
            if (((Wall)t).life == Wall.INDESTRUCTIBLE) {
                return R.drawable.wall_unbreakable;

            } else {
                return R.drawable.wall_breakable;
            }
        }

        if(t.getType() == Tile.BULLET)
        {
            return  R.drawable.bullet;
        }

        if(t.getType() == Tile.TANK)
        {
            if(t.getDirection() == 0)
            {
                return  R.drawable.tank_forward;
            }
            if(t.getDirection() == 2)
            {
                return  R.drawable.tank_right;
            }
            if(t.getDirection() == 4)
            {
                return  R.drawable.tank_down;
            }
            if(t.getDirection() == 6)
            {
                return  R.drawable.tank_left;
            }
        }

        return R.drawable.blankspace;
    }

    //TODO: use buttons to indicate direction rather than calculating it on the fly
    void move(int pos)
    {

        for (int i = 0; i < 3; i++)
        {
            if (pos >= 4+(i*16) && pos <= 11+(i*16))
            {
                bus.post(new MoveEvent(GameGrid.UP));
                Toast.makeText(context, "Move forward", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        for (int i = 12; i < 16; i++)
        {
            if (pos >= 4+(i*16) && pos <= 11+(i*16))
            {
                bus.post(new MoveEvent(GameGrid.DOWN));
                Toast.makeText(context, "Move Down", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        for (int i = 0; i < 8; i++)
        {
            if (pos >= (i+4)*16 && pos <= ((i+4)*16)+3 )
            {
                bus.post(new MoveEvent(GameGrid.LEFT));
                Toast.makeText(context, "Move left", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        for (int i = 0; i < 8; i++)
        {
            if (pos >= ( (i+4)*16)+13 && pos <= ((i+4)*16)+15 )
            {
                bus.post(new MoveEvent(GameGrid.RIGHT));
                Toast.makeText(context, "Move right", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    @Subscribe
    public void updateGrid(Tile[][] board)
    {
        Log.d(TAG, "Updating GridView");
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
