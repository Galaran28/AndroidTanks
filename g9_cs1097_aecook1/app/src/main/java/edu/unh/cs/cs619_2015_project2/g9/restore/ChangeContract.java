package edu.unh.cs.cs619_2015_project2.g9.restore;

import android.provider.BaseColumns;

/**
 * Define db table for storing game frames
 * @Author Chris Sleys
 */
public class ChangeContract {
    public ChangeContract() {}

    /* Inner class that defines the table contents */
    public static abstract class ChangeRow implements BaseColumns {
        public static final String TABLE_NAME = "saveRestore";
        public static final String COLUMN_NAME_CHANGE_BLOB = "changeBlob";
    }
}
