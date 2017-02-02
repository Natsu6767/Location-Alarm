package com.cfd.map.mohit.locationalarm.activities;

import android.content.Intent;
import android.os.Bundle;
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

    private AlarmAdapter mAdapter;
    private ArrayList<Alarm> mAlarms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);


        mAlarms = new ArrayList<Alarm>();

        //Implements RecyclerView
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.alarm_list);
        mRecyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);//sets the RecyclerView as Vertical
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new AlarmAdapter(mAlarms);
        mRecyclerView.setAdapter(mAdapter);
        //Adds horizontal bar after each item
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);

    }
    public void setAlarm(View view){
        startActivityForResult(new Intent(this,SetAlarmActivity.class),1);
    }
   


    @Override
    protected void onResume() {
        super.onResume();
        ((AlarmAdapter) mAdapter).setOnItemClickListener(new AlarmAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {


            }
        });
    }
}
