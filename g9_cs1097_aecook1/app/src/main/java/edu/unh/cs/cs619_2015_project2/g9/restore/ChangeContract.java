package edu.unh.cs.cs619_2015_project2.g9.restore;

import android.provider.BaseColumns;

/**
 * Created by chris on 11/24/15.
 */
public class ChangeContract {
    public ChangeContract() {}

    /* Inner class that defines the table contents */
    public static abstract class ChangeRow implements BaseColumns {
        public static final String TABLE_NAME = "saveRestore";
        public static final String COLUMN_NAME_CHANGE_BLOB = "changeBlob";
    }
}
