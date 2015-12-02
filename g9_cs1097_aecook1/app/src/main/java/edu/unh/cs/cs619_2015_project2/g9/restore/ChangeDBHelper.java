package edu.unh.cs.cs619_2015_project2.g9.restore;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Helper functions for manage the db
 * @Author Chris Sleys
 */
public class ChangeDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Change.db";
    private static final String TYPE_BLOB = " BLOB";
    private static final String TYPE_TEXT = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ChangeContract.ChangeRow.TABLE_NAME + " (" +
                    ChangeContract.ChangeRow._ID + " INTEGER PRIMARY KEY," +
                    ChangeContract.ChangeRow.COLUMN_NAME_CHANGE_BLOB + TYPE_BLOB + COMMA_SEP +
                    ChangeContract.ChangeRow.COLUMN_NAME_TIMESTAMP + TYPE_TEXT +
            " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ChangeContract.ChangeRow.TABLE_NAME;

    public ChangeDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onDelete(SQLiteDatabase db) {
        db.execSQL(SQL_DELETE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
