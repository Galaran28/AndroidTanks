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
    private Tile blankTile, unbreakableWall;

    public TileFactory() {
        blankTile = new Tile();
        unbreakableWall = new Wall(1000);
    }

    /**
     * Take in the integer representations of the tiles and generates the correct tile object
     *
     * @param integerRepresentation integer form of the object
     * @return
     */
    public Tile createTile(int integerRepresentation) {
        Tile ret = null;
        if (integerRepresentation == 0) {
            ret = blankTile;
        } else if (integerRepresentation == 1000) {
            ret = unbreakableWall;
        } else if (integerRepresentation > 1000 && integerRepresentation <= 2000) {
            ret = new Wall(integerRepresentation);
        } else if  (integerRepresentation >= 2000000 && integerRepresentation <= 3000000) {
            ret = new Bullet(integerRepresentation);
        } else if (integerRepresentation >= 10000000 && integerRepresentation <= 20000000) {
            ret = new Tank(integerRepresentation);
        } else {
            Log.e(TAG, "invalid value in grid " + String.valueOf(integerRepresentation));
        }

       return ret;
    }
}
