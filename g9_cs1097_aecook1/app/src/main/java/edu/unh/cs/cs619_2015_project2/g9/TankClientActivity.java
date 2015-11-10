package edu.unh.cs.cs619_2015_project2.g9;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;


import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.ViewById;

import edu.unh.cs.cs619_2015_project2.g9.util.OttoBus;

@EActivity(R.layout.content_main)
public class TankClientActivity extends AppCompatActivity implements View.OnTouchListener {
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        mSensorManager = (SensorManager) getSystemService(getApplicationContext().SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;


        gridview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Toast toast = Toast.makeText(getApplicationContext(), "Device has pressed.", Toast.LENGTH_SHORT);
                toast.show();
                return true;
            }
        });
      /*  Button up = (Button) findViewById(R.id.up);
        Button down = (Button) findViewById(R.id.down);
        Button left = (Button) findViewById(R.id.left);
        Button right = (Button) findViewById(R.id.right);

        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.move(GameGrid.UP);
               Toast.makeText(getApplicationContext(), "UP", Toast.LENGTH_SHORT);
            }
        }
        );

        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.move(GameGrid.DOWN);
            }
        });

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {;
                game.turn(GameGrid.LEFT);
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.turn(GameGrid.RIGHT);
            }
        });

/*        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, String.valueOf(tankId), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }


   /* @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // Normal event dispatch to this container's children, ignore the return value
        super.dispatchTouchEvent(ev);
      Toast toast = Toast.makeText(getApplicationContext(), "Device was pressed.", Toast.LENGTH_SHORT);
        toast.show();
        return true;
    }*/

    @Override
    public boolean onTouch(View v, MotionEvent ev)
    {
        Toast toast = Toast.makeText(getApplicationContext(), "Device was pressed.", Toast.LENGTH_SHORT);
        toast.show();
        return true;
    }

    @Override
    public void onUserInteraction(){
      //  Toast toast = Toast.makeText(getApplicationContext(), "Device has shaken.", Toast.LENGTH_SHORT);
       // toast.show();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Toast toast = Toast.makeText(getApplicationContext(), "Device was touched.", Toast.LENGTH_SHORT);
        toast.show();
        return true;
    }

    @Touch
    public boolean button(View v, MotionEvent event) {
        Toast toast = Toast.makeText(getApplicationContext(), "Device was pressed.", Toast.LENGTH_SHORT);
        toast.show();
        return true;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @AfterViews
    void afterView() {
        gridview.setAdapter(gridAdapter );
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
            if (mAccel > 12) {
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
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }
}
