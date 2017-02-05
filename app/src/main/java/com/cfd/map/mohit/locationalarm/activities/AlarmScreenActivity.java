package com.cfd.map.mohit.locationalarm.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
        getApplicationContext().getSharedPreferences("my",0).edit().putBoolean("inUse",true).commit();
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
        geoAlarm.setStatus(false);
        AlarmDatabase database = new AlarmDatabase(getApplicationContext());
        database.updateData(geoAlarm);
        startActivity(new Intent(AlarmScreenActivity.this,MainActivity.class));
        if(player.isPlaying()){
            player.stop();
            player.reset();
        }
        getApplicationContext().getSharedPreferences("my",0).edit().putBoolean("inUse",false).commit();
        startService(new Intent(getApplicationContext(),GeoService.class));
        finish();
    }

    @Override
    public void onBackPressed() {

    }
}
