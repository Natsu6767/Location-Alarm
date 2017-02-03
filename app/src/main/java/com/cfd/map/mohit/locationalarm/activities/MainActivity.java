package com.cfd.map.mohit.locationalarm.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cfd.map.mohit.locationalarm.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private GeoAlarmAdapter mAdapter;
    private ArrayList<GeoAlarm> mAlarms;
    final Context context = MainActivity.this;
    final private int REQUEST_CODE = 1;
    double lati, lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        //Buton used to set the alarm
        FloatingActionButton setAlarm = (FloatingActionButton) findViewById(R.id.set_alarm);
        //On click listener for setting the alarm button
        setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivityForResult(new Intent(MainActivity.this, CustomPlacePicker.class), REQUEST_CODE);


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
    public void setAlarm(String name, LocationCoordiante location, boolean vibrate, Ringtone ringtone, String ringtoneName) {
        mAlarms.add(new GeoAlarm(name, location, vibrate, ringtone, ringtoneName));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Intent", "" + resultCode);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                lati = data.getDoubleExtra("latitude", 0);
                lang = data.getDoubleExtra("longitude", 0);
                Toast.makeText(this, "" + lati + ", " + lang, Toast.LENGTH_SHORT).show();

                //Creates the dialog for configuring the new alarm
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.location_alarm_dialog, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView.findViewById(R.id.alarm_name_input);
                TextView locationShow = (TextView) promptsView.findViewById(R.id.location_coordinates);
                Button locationSet = (Button) promptsView.findViewById(R.id.select_location);
                final Spinner ringtoneSelect = (Spinner) promptsView.findViewById(R.id.ringtone);
                final CheckBox vibration = (CheckBox) promptsView.findViewById(R.id.vibration);

                //Use to retreive ringtones from the phone
                final Map<String, Ringtone> ringtones = new HashMap<>();
                RingtoneManager manager = new RingtoneManager(MainActivity.this);
                manager.setType(RingtoneManager.TYPE_ALARM);
                Cursor cursor = manager.getCursor();
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {

                    ringtones.put(cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX), manager.getRingtone(cursor.getPosition()));
                    cursor.moveToNext();
                }
                //cursor.close();

                //Extracts the names of the ringtones
                final ArrayList<String> ringtoneNames = new ArrayList<String>();
                for (Map.Entry<String, Ringtone> entry : ringtones.entrySet()) {
                    ringtoneNames.add(entry.getKey());
                }
                //Puts the values in the ringtone spinner
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, ringtoneNames);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ringtoneSelect.setAdapter(dataAdapter);

                locationSet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        startActivityForResult(new Intent(MainActivity.this, CustomPlacePicker.class), REQUEST_CODE);
                    }

                });


                // set dialog message
                alertDialogBuilder
                        .setCancelable(true)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //Sets the alarm. Code needs to be entered
                                        setAlarm(userInput.getText().toString(),
                                                new LocationCoordiante(lati, lang), vibration.isChecked(),
                                                ringtones.get(ringtoneSelect.getSelectedItem()), ringtoneSelect.getSelectedItem().toString());
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

                locationShow.setText("" + lati + ", " + lang);
            }
        }
    }
}
