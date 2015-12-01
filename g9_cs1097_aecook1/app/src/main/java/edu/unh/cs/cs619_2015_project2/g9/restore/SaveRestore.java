package edu.unh.cs.cs619_2015_project2.g9.restore;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.SystemClock;
import android.util.Log;

import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Blob;

import edu.unh.cs.cs619_2015_project2.g9.events.BeginReplayEvent;
import edu.unh.cs.cs619_2015_project2.g9.util.GridWrapper;
import edu.unh.cs.cs619_2015_project2.g9.util.OttoBus;

/**
 * Reads and writes changes from and to the SQLite DB
 * @Author Chris Sleys
 */
@EBean(scope = EBean.Scope.Singleton)
public class SaveRestore {
    public static final String TAG = "SaveRestore";
    @RootContext
    Context root;

    @Bean
    OttoBus bus;

    int[][] current;
    boolean restoring = false;
    ChangeDBHelper dbHelper;

    @AfterInject
    public void init() {
        dbHelper = new ChangeDBHelper(root);
        dbHelper.onDelete(dbHelper.getWritableDatabase()); // clear any previous saved games
        dbHelper.onCreate(dbHelper.getWritableDatabase());
        current = new int[16][16];
        bus.register(this);
    }

    /**
     * Capture Gamechange events and write them to the db.
     * @param gc A list of change to the board
     */
    @Subscribe
    @Background
    public void saveFrame(GridChange gc) {
        if (restoring) {return;} // these events are from the restore function, ignore

        // serilize frame
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(gc);
            out.close();
        } catch(IOException ioe) {
            Log.e("serializeObject", "error", ioe);
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ChangeContract.ChangeRow.COLUMN_NAME_CHANGE_BLOB, bos.toByteArray());

        long newRowId;
        newRowId = db.insert(
                ChangeContract.ChangeRow.TABLE_NAME,
                "null",
                values);
        Log.i("dbWrite", "wrote row " + newRowId + " to db");
    }

    /**
     * When signaled by a event, start reading the frames from the db and publishing them to the
     * bus. The replay event contains the scaling factor for the delays between publishing
     * @param e ReplyEvent with the scaling factor
     */
    @Subscribe
    @Background
    public void restoreFrames(BeginReplayEvent e) {
        Log.i(TAG, "Starting to reply from db");
        restoring = true; // signal that the change events are coming from the db
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                ChangeContract.ChangeRow.COLUMN_NAME_CHANGE_BLOB
        };

        String sortOrder =
                ChangeContract.ChangeRow._ID + " ASC";

        Cursor c = db.query(
                ChangeContract.ChangeRow.TABLE_NAME,    // The table to query
                projection,                             // The columns to return
                null,                                   // return all rows
                null,                                   // params for previous where clause
                null,                                   // don't group the rows
                null,                                   // don't filter by row groups
                sortOrder                               // The sort order
        );
        // read all the frames from the db in sequence
        Log.i(TAG, "setting cursor");
        c.moveToFirst();
        if (c.isAfterLast()) {
            restoring = false;
            Log.i(TAG, "Nothing in db");
            return;
        }

        GridChange current, next;
        long sleepTime;
        Log.i(TAG, "getting first frame");
        current = decode(c.getBlob(c.getColumnIndex(ChangeContract.ChangeRow.COLUMN_NAME_CHANGE_BLOB)));
        while(!c.isLast()) {
            Log.i(TAG, "getting next frame");
            next = decode(c.getBlob(c.getColumnIndex(ChangeContract.ChangeRow.COLUMN_NAME_CHANGE_BLOB)));
            bus.post(current);
            sleepTime = (next.timestamp - current.timestamp)/e.speedFactor;
            current = next;
            c.moveToNext();
            Log.i(TAG, "sleeping for " + sleepTime + " miliseconds");
            SystemClock.sleep(sleepTime);
        }

        restoring = false; // singnal that frames are no longer from the db
    }

    /**
     * Decode a blob from the db into a GridChange object
     * @param b Blob of data
     * @return GridChange object
     */
    private GridChange decode(byte[] b) {
        // read and deserilize object
        GridChange gc;
        try {
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(b));
            gc = (GridChange) in.readObject();
            in.close();
            return gc;
        } catch(ClassNotFoundException cnfe) {
            Log.e("deserializeObject", "class not found error", cnfe);
            return null;
        } catch(IOException ioe) {
            Log.e("deserializeObject", "io error", ioe);
            return null;
        }
    }

    public void release() {
        bus.unregister(this);
    }
}
