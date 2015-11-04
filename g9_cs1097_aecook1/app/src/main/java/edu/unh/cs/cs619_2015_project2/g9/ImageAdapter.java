package edu.unh.cs.cs619_2015_project2.g9;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private int[] sgrid =
            { 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
              1000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 1001, 0000, 0000, 1000,
              1000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 1001, 0000, 0000, 1000,
              1000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 1001, 0000, 0000, 1000,
              1000, 1001, 1001, 1001, 1001, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 1001, 0000, 0000, 1000,
              1000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 1001, 0000, 0000, 1000,
              1000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 1001, 0000, 0000, 1000,
              1000, 0000, 0000, 0000, 0000, 10000060, 0000, 0000, 0000, 0000, 0000, 0000, 1001, 0000, 0000, 1000,
              1000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 1001, 0000, 0000, 1000,
              1000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 1001, 1001, 1001, 1001, 1001, 0000, 0000, 1000,
              1000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 1000,
              1000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 1000,
              1000, 0000, 1001, 1001, 1001, 1001, 1001, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 1000,
              1000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 1000,
              1000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 1000,
              1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000
            };
    // Constructor
    public ImageAdapter(Context c) {
        mContext = c;
    }
    public ImageAdapter(Context c, int[][] mgrid) {
        mContext = c;
        int index = 0;
        for ( int i = 0; i < 16; i++)
        {
            for ( int u = 0; u < 16; u++)
            {
                sgrid[index] = mgrid[i][u];
            }
        }
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
    public void updateGrid( int[][] mgrid)
    {
        int index = 0;
        for ( int i = 0; i < 16; i++)
        {
            for ( int u = 0; u < 16; u++)
            {
                sgrid[index] = mgrid[i][u];
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
        if(sgrid[pos] == 1000)
        {
            return R.mipmap.wall_unbreakable;
        }
        if(sgrid[pos] >1000 && sgrid[pos] <= 2000)
        {
            return R.mipmap.wall_breakable;
        }
        if(sgrid[pos] > 10000000 && sgrid[pos] <= 20000000)
        {
            return R.mipmap.tank_forward;
        }

        return R.mipmap.blankspace;
    }

}
