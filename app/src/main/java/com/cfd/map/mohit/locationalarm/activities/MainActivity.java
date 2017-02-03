package com.cfd.map.mohit.locationalarm.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cfd.map.mohit.locationalarm.R;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    // Database

    AlarmDatabase alarmDatabase;
    final Context context = MainActivity.this;
    final private int REQUEST_CODE = 1;
    double lati, lang;
    RecyclerView mRecyclerView;
    TextView posi;
    // Location variables
    LocationManager locationManager;
    LocationListener locationListener;
    LatLng alarmPos;
    private GeoAlarmAdapter mAdapter;
    private ArrayList<GeoAlarm> mAlarms;
    private int radius = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                if(alarmPos!=null) {
                    if (calculateDis(alarmPos, loc) < radius) {

                        Toast.makeText(MainActivity.this, "You have arrived", Toast.LENGTH_SHORT).show();
                        // locationManager.removeUpdates(locationListener);
                    }
                    posi.setText(calculateDis(alarmPos, loc) + "");
                    Log.d("Location", "" + calculateDis(alarmPos, loc));
                }
                // Toast.makeText(MainActivity.this,""+ calculateDis(alarmPos.latitude,alarmPos.longitude),Toast.LENGTH_LONG).show();
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {

            }
        };
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        posi = (TextView) findViewById(R.id.positoion);
        //Buton used to set the alarm
        FloatingActionButton setAlarm = (FloatingActionButton) findViewById(R.id.set_alarm);
        //On click listener for setting the alarm button
        setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,""+alarmDatabase.getAllData(), Toast.LENGTH_SHORT).show();
                startActivityForResult(new Intent(MainActivity.this, CustomPlacePicker.class), REQUEST_CODE);
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
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);


        // adding database
        alarmDatabase = new AlarmDatabase(this);

    }

    //Use to set info for new alarm
    public void setAlarm(String name, LocationCoordiante location, boolean vibrate,
                         Ringtone ringtone, String ringtoneName, int range, int time) {
        mAlarms.add(new GeoAlarm(name, location, vibrate, ringtone, ringtoneName, range, time));
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

                        //Extracts the names of the ringtones
                        final ArrayList<String> ringtoneNames = new ArrayList<String>();
                        for (Map.Entry<String, Ringtone> entry : ringtones.entrySet()) {
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
                alarmPos = new LatLng(lati, lang);

                Log.d("location", alarmPos.toString());
                Toast.makeText(this, "" + lati + ", " + lang, Toast.LENGTH_SHORT).show();

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
                TimePicker expectedTime = (TimePicker) promptsView.findViewById(R.id.expected_time);

                expectedTime = new TimePicker(MainActivity.this);
                expectedTime.setIs24HourView(true);
                int hour = expectedTime.getCurrentHour();
                int minute = expectedTime.getCurrentMinute();
                final int second = (hour * 3600) + (minute * 60);


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


                // set dialog message
                alertDialogBuilder
                        .setCancelable(true)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //Sets the alarm. Code needs to be entered
                                        setAlarm(userInput.getText().toString(),
                                                new LocationCoordiante(lati, lang), vibration.isChecked(),
                                                ringtones.get(ringtoneSelect.getSelectedItem()), ringtoneSelect.getSelectedItem().toString(),
                                                Integer.parseInt(range.getText().toString()), second);}
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

    // distance formula from current location
    private double calculateDis(LatLng destiny, LatLng myLoc) {
        double R = 6371e3; // metres
        double φ1 = Math.PI * destiny.latitude / 180;
        double φ2 = Math.PI * myLoc.latitude / 180;
        double Δφ = φ2 - φ1;
        double Δλ = Math.PI * (destiny.longitude - myLoc.longitude) / 180;
        double a = Math.sin(Δφ / 2) * Math.sin(Δφ / 2) + Math.cos(φ1) * Math.cos(φ2) * Math.sin(Δλ / 2) * Math.sin(Δλ / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double d = R * c;
        return d;
    }
    public void loadData(){

    }

}
