package edu.unh.cs.cs619_2015_project2.g9;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
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


import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.LongClick;
import org.androidannotations.annotations.ViewById;

import edu.unh.cs.cs619_2015_project2.g9.events.BeginReplayEvent;
import edu.unh.cs.cs619_2015_project2.g9.events.FireEvent;
import edu.unh.cs.cs619_2015_project2.g9.events.MoveEvent;
import edu.unh.cs.cs619_2015_project2.g9.events.TurnEvent;
import edu.unh.cs.cs619_2015_project2.g9.restore.SaveRestore;
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
    SaveRestore saveRestore;

    @Bean
    GridAdapter gridAdapter;

    @ViewById
    GridView gridview;

    private SensorManager mySensorManager;
    private Sensor mySensor;
    private MyShakeListener myShakeListener;

    private boolean replay = false;
    private static MediaPlayer mediaPlayer;

    @AfterViews
    public void afterViews() {
        gridview.setAdapter(gridAdapter);
        enableImmersive();
    }

    @AfterInject
    public void afterInjection() {
        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("message");
        if (message.equals("replay")) {
            bus.post(new BeginReplayEvent(1));
            replay = true;
        }
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();

        mySensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mySensor = mySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        myShakeListener = new MyShakeListener();
        myShakeListener.setOnShakeListener(new MyShakeListener.OnShakeListener() {
            @Override
            public void onShake(int count) {
                if(!replay)
                    bus.post(new FireEvent());
            }
        });
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
        mySensorManager.registerListener(myShakeListener, mySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }


    /**
     * Un-Registers the shakeListener and stops music when the activity pauses
     *
     * @Author Alex Cook
     */
    @Override
    protected void onPause() {
        mediaPlayer.release();
        mySensorManager.unregisterListener(myShakeListener);
        super.onPause();
    }

    @Override
    protected  void onDestroy() {
        game.release();
        gridAdapter.release();
        saveRestore.release();
        game = null;
        gridAdapter = null;
        saveRestore = null;
        super.onDestroy();
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
        if (!replay)
        bus.post(new MoveEvent(Tile.LEFT));
    }

    @Background
    @Click(R.id.right)
    void rightClicked(){
        Log.i(TAG, "rightClicked");
        if (!replay)
        bus.post(new MoveEvent(Tile.RIGHT));
    }

    @Background
    @Click(R.id.up)
    void upClicked(){
        Log.i(TAG, "upClicked");
        if (!replay)
        bus.post(new MoveEvent(Tile.UP));
    }

    @Background
    @Click(R.id.down)
    void downClicked(){
        Log.i(TAG, "downClicked");
        if (!replay)
        bus.post(new MoveEvent(Tile.DOWN));
    }

    @Background
    @Click(R.id.fire)
    void fireClicked(){
        if (!replay)
        bus.post(new FireEvent());
    }
}
