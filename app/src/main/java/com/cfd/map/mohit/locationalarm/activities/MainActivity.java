package com.cfd.map.mohit.locationalarm.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.cfd.map.mohit.locationalarm.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void setAlarm(View view){
        startActivityForResult(new Intent(this,SetAlarmActivity.class),1);
    }
    public void viewAlarm(View view){

    }
}
