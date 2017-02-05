package com.cfd.map.mohit.locationalarm.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.cfd.map.mohit.locationalarm.R;

public class AlarmScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_screen);
    }
    public void offAlarm(View view){
        //stopService(new Intent(AlarmScreenActivity.this,GeoService.class));
        GeoAlarm geoAlarm = (GeoAlarm) getIntent().getSerializableExtra("geoAlarm");
        geoAlarm.setStatus(false);
        AlarmDatabase database = new AlarmDatabase(getApplicationContext());
        database.updateData(geoAlarm);
        startActivity(new Intent(AlarmScreenActivity.this,MainActivity.class));
        finish();
       // startService(new Intent(AlarmScreenActivity.this,GeoService.class));
    }

    @Override
    public void onBackPressed() {

    }
}
