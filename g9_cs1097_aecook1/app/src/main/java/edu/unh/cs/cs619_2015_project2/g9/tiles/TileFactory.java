package edu.unh.cs.cs619_2015_project2.g9.tiles;

import android.util.Log;
import android.widget.Toast;

import org.androidannotations.annotations.EBean;

/**
 * Factory for generating tiles
 *
 * @Author Chris Sleys
 */
@EBean(scope = EBean.Scope.Singleton)
public class TileFactory {
    public static final String TAG = "TileFactory";

    /**
     * Take in the integer representations of the tiles and generates the correct tile object
     *
     * @param integerRepresentation integer form of the object
     * @return
     */
    public Tile createTile(int integerRepresentation) {
/*        Tile ret;
        if (integerRepresentation == 0) {
            ret = new Tile();
        } else if (integerRepresentation >= 100 || integerRepresentation <= 2000) {
            ret = new Wall(integerRepresentation);
        } else if (integerRepresentation >= 2000000 || integerRepresentation <= 3000000) {
            ret = new Bullet(integerRepresentation);
        } else if (integerRepresentation >= 10000000 || integerRepresentation <= 20000000) {
            ret = new Tank(integerRepresentation);
        } else {
            Log.e(TAG, "invalid value in grid");
            ret = null;
        }
*/
        Tile ret;
        if (integerRepresentation == 0) {
            ret = new Tile();
         //   Log.e(TAG, String.valueOf(integerRepresentation) );
            return ret;
        }

        if (integerRepresentation >= 100 && integerRepresentation <= 2000)
        {
            ret = new Wall(integerRepresentation);
         //   Log.e(TAG, String.valueOf(integerRepresentation) );
            return ret;
        }

        if  (integerRepresentation >= 2000000 && integerRepresentation <= 3000000)
        {
            ret = new Bullet(integerRepresentation);
          //  Log.e(TAG, String.valueOf(integerRepresentation) );
            return ret;
        }

        if (integerRepresentation >= 10000000 && integerRepresentation <= 20000000)
        {
            ret = new Tank(integerRepresentation);
            return ret;
        }

        Log.e(TAG, "invalid value in grid");
        ret = null;
        return ret;
    }
}
