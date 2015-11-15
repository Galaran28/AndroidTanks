package edu.unh.cs.cs619_2015_project2.g9;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.springframework.web.client.RestClientException;

import edu.unh.cs.cs619_2015_project2.g9.rest.BulletZoneRestClient;

public class HomeScreen extends AppCompatActivity {

    public static MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_home_screen);
        enableImmersive();

        ImageView imageView2 = (ImageView) findViewById(R.id.backImage);
        imageView2.setImageResource(R.mipmap.home_screen_backimage);
        ImageView imageView = (ImageView) findViewById(R.id.screenImage);
        imageView.setImageResource(R.mipmap.home_screen_4);
     //   mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.home_screen_sound_2);
      //  mediaPlayer.setLooping(true);
       // mediaPlayer.start();
        Button join = (Button) findViewById(R.id.join);
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                  //  mediaPlayer.release();
                    Intent intent = new Intent(getApplicationContext(), TankClientActivity_.class);
                    startActivity(intent);
                } catch (RestClientException e) {
                    Toast.makeText(getApplicationContext(), "Unable to join server", Toast.LENGTH_SHORT);
                }

            }
        });


    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @SuppressLint("NewApi")
    private void enableImmersive() {
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

    @Override
    protected void onResume() {
        super.onResume();
      //  enableImmersive();
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.home_screen_sound_2);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    @Override
    protected void onPause() {
        mediaPlayer.release();
        super.onPause();
    }

}
