package edu.unh.cs.cs619_2015_project2.g9.tiles;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.RotateDrawable;

import edu.unh.cs.cs619_2015_project2.g9.ui.TankUI;

/**
 * A tile representing a tank
 *
 * @Author Chris Sleys
 */
public class Tank extends Tile {
    public int life;
    public int direction;
    public int id;

    public Tank(int integerRepresentation) {
        String parsable = Integer.toString(integerRepresentation);
        id = Integer.parseInt(parsable.substring(1, 3));
        life =  Integer.parseInt(parsable.substring(4, 6));
        direction = Integer.parseInt(parsable.substring(7));
        this.setUI(new TankUI());
    }

    @Override
    public Drawable display() {
        RotateDrawable d = (RotateDrawable)getUI().getImage();
        d.setLevel(500);

        return d;
    }
}
    }
}
