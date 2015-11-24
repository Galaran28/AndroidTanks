package edu.unh.cs.cs619_2015_project2.g9.restore;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import edu.unh.cs.cs619_2015_project2.g9.events.BeginReplayEvent;
import edu.unh.cs.cs619_2015_project2.g9.util.GridWrapper;
import edu.unh.cs.cs619_2015_project2.g9.util.OttoBus;

/**
 * Created by chris on 11/24/15.
 */
@EBean(scope = EBean.Scope.Singleton)
public class SaveRestore {
    @RootContext
    Context root;

    @Bean
    OttoBus bus;

    int[][] current;
    ChangeDBHelper dbHelper;

    @AfterInject
    public void init() {
        dbHelper = new ChangeDBHelper(root);
        dbHelper.onDelete(dbHelper.getWritableDatabase()); // clear any previous saved games
        dbHelper.onCreate(dbHelper.getWritableDatabase());
        current = new int[16][16];
        bus.register(this);
    }

    @Subscribe
    @Background
    public void saveFrame(GridWrapper gw) {
        int[][] next = gw.getGrid();
        if (next == null) { return; } // no data to write
        GridChange changes = diff(next);
        if (changes.length() <= 0) { return; } // no changes to record
        changes.timestamp = gw.getTimeStamp();
        current = next;

        // serilize frame
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(changes);
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

    @Subscribe
    @Background
    public void restoreFrame(BeginReplayEvent e) {
        // TODO read from sqlite db
        // TODO post gridwappers at defined interval
    }

    private GridChange diff(int[][] next) {
        GridChange changes = new GridChange();
        for (int i = 0; i < current.length; i++) {
            for (int j = 0; j < current[i].length; j++) {
                if (current[i][j] == next[i][j]) {
                    continue;
                }

               changes.add(new ElementChange(i, j, next[i][j]));
            }
        }
        return changes;
    }
}
