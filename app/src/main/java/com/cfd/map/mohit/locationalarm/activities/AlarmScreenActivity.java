package com.cfd.map.mohit.locationalarm.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.cfd.map.mohit.locationalarm.R;

import java.io.IOException;

public class AlarmScreenActivity extends AppCompatActivity {
    private GeoAlarm geoAlarm;
    MediaPlayer player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_screen);
        geoAlarm = (GeoAlarm) getIntent().getSerializableExtra("geoAlarm");
        player = new MediaPlayer();
        try {
            player.setDataSource(this, Uri.parse(geoAlarm.getRingtoneUri()));
            player.setLooping(true);
            player.prepare();
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void offAlarm(View view){
        //stopService(new Intent(AlarmScreenActivity.this,GeoService.class));
        geoAlarm.setStatus(false);
        AlarmDatabase database = new AlarmDatabase(getApplicationContext());
        database.updateData(geoAlarm);
        startActivity(new Intent(AlarmScreenActivity.this,MainActivity.class));
        player.stop();
        player.reset();
        finish();

        // startService(new Intent(AlarmScreenActivity.this,GeoService.class));
    }

    @Override
    public void onBackPressed() {

    }
}
