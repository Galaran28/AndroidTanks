package edu.unh.cs.cs619_2015_project2.g9.restore;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by chris on 11/24/15.
 */
public class GridChange implements Serializable{
    public ArrayList<ElementChange> changes;
    public long timestamp;

    public GridChange() {
        changes = new ArrayList<>();
    }

    public GridChange(ArrayList<ElementChange> list) {
        changes = list;
    }

    public void add(ElementChange e) {
       changes.add(e);
    }

    public int length() {
        return changes.size();
    }
}
