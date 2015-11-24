package edu.unh.cs.cs619_2015_project2.g9.restore;

import java.io.Serializable;

/**
 * Created by chris on 11/24/15.
 */
public class ElementChange implements Serializable{
    public int x, y, gridInt;

    public ElementChange(int x, int y, int gridInt) {
        this.x = x;
        this.y = y;
        this.gridInt = gridInt;
    }
}
