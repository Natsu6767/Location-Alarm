package com.cfd.map.mohit.locationalarm.activities;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.cfd.map.mohit.locationalarm.R;

import java.io.IOException;

public class AlarmScreenActivity extends AppCompatActivity {
    private GeoAlarm geoAlarm;
    private MediaPlayer player;
    private  Vibrator v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
        /***************
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ********/
        setContentView(R.layout.activity_alarm_screen);
        geoAlarm = (GeoAlarm) getIntent().getSerializableExtra("geoAlarm");

        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        TextView name, message;
        name = (TextView) findViewById(R.id.show_name);
        message = (TextView) findViewById(R.id.show_message);

        if (geoAlarm.getName() != "") {
            name.setText(" at " + geoAlarm.getName());
        } else {
            name.setText("" + geoAlarm.getName());
        }
        message.setText("" + geoAlarm.getMessage());

        if (geoAlarm.getVibration()) {

            long[] pattern = {0, 100, 1000};//to set vibration for 100 millisecond and pause of 1000 milliseconds
            v.vibrate(pattern, 0);//starts indefinite vibration
        }

        player = new MediaPlayer();
        try {
            player.setDataSource(this,Uri.parse(geoAlarm.getRingtoneUri()));
            player.setLooping(true);
            player.prepare();
            player.start();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void offAlarm(View view) {
        geoAlarm.setStatus(false);
        AlarmDatabase database = new AlarmDatabase(getApplicationContext());
        database.updateData(geoAlarm);
        startActivity(new Intent(AlarmScreenActivity.this, MainActivity.class));
        if (player.isPlaying()) {
            player.stop();
            player.reset();
        }
        v.cancel();//stops vibration
        startService(new Intent(getApplicationContext(), GeoService.class));
        finish();
    }
    @Override
    public void onBackPressed() {

    }
}
