package com.cfd.map.mohit.locationalarm.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.cfd.map.mohit.locationalarm.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private GeoAlarmAdapter mAdapter;
    private ArrayList<GeoAlarm> mAlarms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        //Buton used to set the alarm
        FloatingActionButton setAlarm = (FloatingActionButton) findViewById(R.id.set_alarm);
        //On click listener for setting the alarm button
        setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, SetAlarmActivity.class), 1);
            }
        });


        mAlarms = new ArrayList<GeoAlarm>();

        //Implements RecyclerView
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.alarm_list);
        mRecyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);//sets the RecyclerView as Vertical
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new GeoAlarmAdapter(mAlarms);
        mRecyclerView.setAdapter(mAdapter);
        //Adds horizontal bar after each item
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);


    }

    //Use to set info for new alarm
    public void setAlarm(String name, double location, boolean vibrate, int ringtone) {
        mAlarms.add(new GeoAlarm(name, location, vibrate, ringtone));
    }


    @Override
    protected void onResume() {
        super.onResume();
        ((GeoAlarmAdapter) mAdapter).setOnItemClickListener(new GeoAlarmAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
            //On click event for row items

            }
        });
    }

    public void time(View view){
        Intent intent = new Intent(MainActivity.this, TimeAlarmActivity.class);
        startActivity(intent);
    }
}
