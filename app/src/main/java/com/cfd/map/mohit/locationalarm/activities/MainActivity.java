package com.cfd.map.mohit.locationalarm.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.cfd.map.mohit.locationalarm.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private GeoAlarmAdapter mAdapter;
    private ArrayList<GeoAlarm> mAlarms;
    final Context context = MainActivity.this;

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

                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.location_alarm_dialog, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView.findViewById(R.id.alarm_name_input);

                Button locationSet = (Button) promptsView.findViewById(R.id.select_location);

                locationSet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivityForResult(new Intent(MainActivity.this, CustomPlacePicker.class), 1);
                    }
                });

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //Sets the alarm. Code needs to be entered
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
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

}
