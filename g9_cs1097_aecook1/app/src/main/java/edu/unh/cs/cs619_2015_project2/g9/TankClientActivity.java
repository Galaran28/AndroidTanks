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
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.ViewById;

import edu.unh.cs.cs619_2015_project2.g9.util.OttoBus;

@EActivity(R.layout.content_main)
public class TankClientActivity extends AppCompatActivity  {
    private static final String TAG = "TankClientActivity";

    @Bean
    GameGrid game;

    @Bean
    GridAdapter gridAdapter;

    @ViewById
    GridView gridview;

    private SensorManager mSensorManager;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity
    public static MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        mSensorManager = (SensorManager) getSystemService(getApplicationContext().SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
        enableImmersive();


        Toast toast = Toast.makeText(getApplicationContext(), "New Game ", Toast.LENGTH_SHORT);
        toast.show();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

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


    @AfterViews
    void afterView() {
        gridview.setAdapter(gridAdapter);
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
                game.fireBullet();
            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.game_sound_1);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        mediaPlayer.release();
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

    @Background
    @Click(R.id.down)
    void downClicked() {
        game.move(game.DOWN);
    }

    @Background
    @Click(R.id.up)
    void upClicked(){
        game.move(game.UP);
    }

    @Background
    @Click(R.id.left)
    void leftClicked(){
        game.move(game.LEFT);
    }

    @Background
         @Click(R.id.right)
         void rightClicked(){
        game.move(game.RIGHT);
    }

    @Background
    @Click(R.id.fire)
    void fireClicked(){
        game.fireBullet();
    }
}
