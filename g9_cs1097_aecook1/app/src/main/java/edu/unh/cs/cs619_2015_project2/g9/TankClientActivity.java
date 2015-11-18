package edu.unh.cs.cs619_2015_project2.g9;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.widget.GridView;
import android.widget.Toast;


import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.LongClick;
import org.androidannotations.annotations.ViewById;

import edu.unh.cs.cs619_2015_project2.g9.events.FireEvent;
import edu.unh.cs.cs619_2015_project2.g9.events.MoveEvent;
import edu.unh.cs.cs619_2015_project2.g9.events.TurnEvent;
import edu.unh.cs.cs619_2015_project2.g9.tiles.GameGrid;
import edu.unh.cs.cs619_2015_project2.g9.tiles.Tile;
import edu.unh.cs.cs619_2015_project2.g9.ui.GridAdapter;
import edu.unh.cs.cs619_2015_project2.g9.ui.TileUIFactory;
import edu.unh.cs.cs619_2015_project2.g9.util.OttoBus;

@EActivity(R.layout.content_main)
public class TankClientActivity extends AppCompatActivity  {
    private static final String TAG = "TankClientActivity";

    @Bean
    OttoBus bus;

    @Bean
    GameGrid game;

    @Bean
    TileUIFactory uiFactory;

    @Bean
    GridAdapter gridAdapter;

    @ViewById
    GridView gridview;

    private SensorManager shakeSensor;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity
    public static MediaPlayer mediaPlayer;

    @AfterViews
    public void afterView() {
        gridview.setAdapter(gridAdapter);

        shakeSensor = (SensorManager) getSystemService(getApplicationContext().SENSOR_SERVICE);
        shakeSensor.registerListener(mSensorListener, shakeSensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
        enableImmersive();

        Toast toast = Toast.makeText(getApplicationContext(), "New Game ", Toast.LENGTH_SHORT);
        toast.show();

    }

    /**
     * Closes the activity when the user presses their back button
     *
     * @Author Alex Cook
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * Determines the build version and closes the status bar.
     * Does this so image grid fills screen without having to scroll
     *
     * @Author Alex Cook
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @SuppressLint("NewApi")
    public void enableImmersive() {
        int newUiOptions = getWindow().getDecorView().getSystemUiVisibility();
        if (Build.VERSION.SDK_INT >= 14) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }

        if (Build.VERSION.SDK_INT >= 16) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        }

        if (Build.VERSION.SDK_INT >= 18 ) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE;
        }
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
    }


    private final SensorEventListener mSensorListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent se) {
            float x = se.values[0];
            float y = se.values[1];
            float z = se.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta; // perform low-cut filter
            if (mAccel > 8) {
                Toast toast = Toast.makeText(getApplicationContext(), "Device has shaken. ", Toast.LENGTH_SHORT);
                toast.show();
                bus.post(new FireEvent());
            }
        }

        @Background
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    /**
     * Registers the shakeListener and plays music when the activity resumes
     *
     * @Author Alex Cook
     */
    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.game_sound_1);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        shakeSensor.registerListener(mSensorListener, shakeSensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }


    /**
     * Un-Registers the shakeListener and stops music when the activity pauses
     *
     * @Author Alex Cook
     */
    @Override
    protected void onPause() {
        mediaPlayer.release();
        shakeSensor.unregisterListener(mSensorListener);
        super.onPause();
    }



    /**
     * These 5 methods are called when the buttons in the tank activity are pressed.
     * They call the gamegrid's move and fire methods
     *
     * @Author Alex Cook
     */

    @Background
    @Click(R.id.left)
    void leftClicked(){
        Log.i(TAG, "leftClicked");
        bus.post(new MoveEvent(Tile.LEFT));
    }

    @Background
    @Click(R.id.right)
    void rightClicked(){
        Log.i(TAG, "rightClicked");
        bus.post(new MoveEvent(Tile.RIGHT));
    }

    @Background
    @Click(R.id.up)
    void upClicked(){
        Log.i(TAG, "upClicked");
        bus.post(new MoveEvent(Tile.UP));
    }

    @Background
    @Click(R.id.down)
    void downClicked(){
        Log.i(TAG, "downClicked");
        bus.post(new MoveEvent(Tile.DOWN));
    }


    @Background
    @Click(R.id.fire)
    void fireClicked(){
       bus.post(new FireEvent());
    }
}
