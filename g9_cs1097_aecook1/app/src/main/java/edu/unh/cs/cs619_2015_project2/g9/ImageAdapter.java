package edu.unh.cs.cs619_2015_project2.g9;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import edu.unh.cs.cs619_2015_project2.g9.tiles.Tile;
import edu.unh.cs.cs619_2015_project2.g9.tiles.Wall;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private Tile[] sgrid;
    // Constructor
    public ImageAdapter(Context c) {
        mContext = c;
    }
    public ImageAdapter(Context c, Tile[][] mgrid) {
        mContext = c;
        updateGrid(mgrid);
    }
    public int getCount() {
        return 16*16;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }
    public void updateGrid( Tile[][] mgrid)
    {
        int index = 0;
        for ( int i = 0; i < 16; i++)
        {
            for ( int u = 0; u < 16; u++)
            {
                sgrid[index] = mgrid[i][u];
                index++;
            }
        }
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(mContext);
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


    int getImage(int pos)
    {
        if(sgrid[pos].getType() == Tile.WALL)
        {
            Wall w = (Wall) sgrid[pos];
            if (w.life == -1) {
                return R.mipmap.wall_unbreakable;
            } else {
                return R.mipmap.wall_breakable;
            }
        }

        if(sgrid[pos].getType() == Tile.TANK)
        {
            return R.mipmap.tank_forward;
        }

        return R.mipmap.blankspace;
    }

}
