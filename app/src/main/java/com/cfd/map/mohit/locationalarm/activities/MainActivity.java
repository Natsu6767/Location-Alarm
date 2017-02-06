package com.cfd.map.mohit.locationalarm.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
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
    // Database

    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 12;
    static AlarmDatabase alarmDatabase;
    final Context context = MainActivity.this;
    final private int REQUEST_CODE = 1;
    double lati, lang;
    RecyclerView mRecyclerView;
    private GeoService geoService;
    private GeoAlarmAdapter mAdapter;
    static public ArrayList<GeoAlarm> mAlarms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        geoService = new GeoService();
        //Button used to set the alarm
        FloatingActionButton setAlarm = (FloatingActionButton) findViewById(R.id.set_alarm);
        //On click listener for setting the alarm button
        setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "" + alarmDatabase.getAllData(), Toast.LENGTH_SHORT).show();
                requestPermission();
            }
        });
        mAlarms = new ArrayList<GeoAlarm>();

        //Implements RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.alarm_list);
        mRecyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);//sets the RecyclerView as Vertical
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new GeoAlarmAdapter(mAlarms);
        mRecyclerView.setAdapter(mAdapter);
        //Adds horizontal bar after each item
        /*
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);
        */
        mRecyclerView.addItemDecoration(new MyDividerItemDecoration(this));


        // adding database
        alarmDatabase = new AlarmDatabase(getApplicationContext());
        //shows all of the alarms present in the database
        showAlarms();
       stopService(new Intent(this,GeoService.class));
       startService(new Intent(this,GeoService.class));

    }

    //Use to set info for new alarm
    public void setAlarm(String name, LocationCoordiante location, boolean vibrate,
                         String ringtone, String ringtoneName, int range, String message) {
        GeoAlarm geoAlarm = new GeoAlarm(name, location, vibrate, ringtone, ringtoneName, range, message);
        geoAlarm.setStatus(true);
        alarmDatabase.insertData(geoAlarm);
        geoAlarm.setmId(alarmDatabase.getId());
        mAlarms.add(geoAlarm);
        mAdapter.addItem(mAdapter.getItemCount());
        mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((GeoAlarmAdapter) mAdapter).setOnItemClickListener(
                new GeoAlarmAdapter.MyClickListener() {
                    @Override
                    public void onItemClick(final int position, View v) {
                        //On click event for row items

                        //Adds Haptic Feedback
                        View view = findViewById(R.id.activity_main);
                        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                        //Creates the dialog for configuring the new alarm
                        LayoutInflater li = LayoutInflater.from(context);
                        View promptsView = li.inflate(R.layout.location_alarm_dialog, null);

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                        // set prompts.xml to alertdialog builder
                        alertDialogBuilder.setView(promptsView);

                        final EditText userInput = (EditText) promptsView.findViewById(R.id.alarm_name_input);
                        TextView locationShow = (TextView) promptsView.findViewById(R.id.location_coordinates);
                        final Spinner ringtoneSelect = (Spinner) promptsView.findViewById(R.id.ringtone);
                        final CheckBox vibration = (CheckBox) promptsView.findViewById(R.id.vibration);
                        vibration.setChecked(mAlarms.get(position).getVibration());
                        locationShow.setText(mAlarms.get(position).getLocationCoordinate());
                        userInput.setText(mAlarms.get(position).getName());
                        final EditText range = (EditText) promptsView.findViewById(R.id.range);
                        range.setText("" + mAlarms.get(position).getRadius());
                        final EditText message = (EditText) promptsView.findViewById(R.id.message);
                        message.setText("" + mAlarms.get(position).getMessage());

                        //Use to retreive ringtones from the phone
                        final Map<String, String> ringtones = new HashMap<>();
                        RingtoneManager manager = new RingtoneManager(MainActivity.this);
                        manager.setType(RingtoneManager.TYPE_ALARM);
                        Cursor cursor = manager.getCursor();
                        cursor.moveToFirst();
                        while (!cursor.isAfterLast()) {
                            ringtones.put(cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX),
                                    manager.getRingtone(cursor.getPosition()).toString());
                            cursor.moveToNext();
                        }

                        //Extracts the names of the ringtones
                        final ArrayList<String> ringtoneNames = new ArrayList<String>();
                        for (Map.Entry<String, String> entry : ringtones.entrySet()) {
                            ringtoneNames.add(entry.getKey());
                        }
                        //Puts the values in the ringtone spinner
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(MainActivity.this,
                                android.R.layout.simple_spinner_item, ringtoneNames);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        ringtoneSelect.setAdapter(dataAdapter);


                        // set dialog message
                        alertDialogBuilder
                                .setCancelable(true)
                                .setPositiveButton("SAVE",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                //Sets the alarm. Code needs to be entered
                                                mAlarms.get(position).setName(userInput.getText().toString());
                                                mAlarms.get(position).setRingtone(ringtoneSelect.getSelectedItem().toString(),
                                                        ringtones.get(ringtoneSelect.getSelectedItem()));
                                                mAlarms.get(position).setVibration(vibration.isChecked());
                                                mAlarms.get(position).setRadius(Integer.parseInt(range.getText().toString()));
                                                mAlarms.get(position).setMessage("" + message.getText());
                                                mAdapter.refreshItem(position);
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
                }


        );

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Intent", "" + resultCode);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                // coordinates for destination

                lati = data.getDoubleExtra("latitude", 0);
                lang = data.getDoubleExtra("longitude", 0);

                //Creates the dialog for configuring the new alarm
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.location_alarm_dialog, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView.findViewById(R.id.alarm_name_input);
                TextView locationShow = (TextView) promptsView.findViewById(R.id.location_coordinates);
                final EditText range = (EditText) promptsView.findViewById(R.id.range);
                final Spinner ringtoneSelect = (Spinner) promptsView.findViewById(R.id.ringtone);
                final CheckBox vibration = (CheckBox) promptsView.findViewById(R.id.vibration);
                final EditText message = (EditText) promptsView.findViewById(R.id.message);




//Use to retreive ringtones from the phone
                final Map<String, String> ringtones = new HashMap<>();
                RingtoneManager manager = new RingtoneManager(MainActivity.this);
                manager.setType(RingtoneManager.TYPE_ALARM);
                Cursor cursor = manager.getCursor();
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    ringtones.put(cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX), manager.getRingtoneUri(cursor.getPosition()).toString());
                    cursor.moveToNext();
                }


//Extracts the names of the ringtones
                final ArrayList<String> ringtoneNames = new ArrayList<String>();
                for (Map.Entry<String, String> entry : ringtones.entrySet()) {
                    ringtoneNames.add(entry.getKey());
                }
                //Puts the values in the ringtone spinner
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, ringtoneNames);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ringtoneSelect.setAdapter(dataAdapter);


                // set dialog box
                alertDialogBuilder
                        .setCancelable(true)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //Sets the alarm. Code needs to be entered
                                        setAlarm(userInput.getText().toString(),
                                                new LocationCoordiante(lati, lang), vibration.isChecked(),
                                                ringtones.get(ringtoneSelect.getSelectedItem()), ringtoneSelect.getSelectedItem().toString(),
                                                Integer.parseInt(range.getText().toString()), "" + message.getText());

                                        //stopService(new Intent(context,GeoService.class));
                                        Intent intent = new Intent(context,GeoService.class);
                                        startService(intent);
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


    // loading old alarms
    private void showAlarms() {

        ArrayList<GeoAlarm> geoAlarms = alarmDatabase.getAllData();

        if (geoAlarms == null) {

        } else {
            for (GeoAlarm geoAlarm1 : geoAlarms) {
                mAlarms.add(geoAlarm1);
                mAdapter.addItem(mAdapter.getItemCount());
                mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
            }
        }
    }
    private void requestPermission(){
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                Log.d("permission", "You have to ask for it");
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_FINE_LOCATION);

            } else {
                Log.d("permission", "You do not need  to ask for it");
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            }
            return;
        }
        startActivityForResult(new Intent(MainActivity.this, CustomPlacePicker.class), REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("permission", "You have fine permission");

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Log.d("permission", "all things are fine");
                    //mMap.setMyLocationEnabled(true);
                    startActivityForResult(new Intent(MainActivity.this, CustomPlacePicker.class), REQUEST_CODE);
                }

            }
        }
    }
}
