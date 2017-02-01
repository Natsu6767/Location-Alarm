package com.cfd.map.mohit.locationalarm.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.cfd.map.mohit.locationalarm.R;

public class SetAlarmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);
    }
    public void chooseLocation(View view){
        startActivityForResult(new Intent(this,CustomPlacePicker.class),2);
    }
}
