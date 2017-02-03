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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.Toast;

import com.cfd.map.mohit.locationalarm.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, com.google.android.gms.location.LocationListener,GoogleApiClient.OnConnectionFailedListener{

    private GeoAlarmAdapter mAdapter;
    private ArrayList<GeoAlarm> mAlarms;
    final Context context = MainActivity.this;
    final private int REQUEST_CODE = 1;
    double lati, lang;
    RecyclerView mRecyclerView;
    TextView posi;
    FloatingActionButton setAlarm;
    // Location variables
    LocationManager locationManager;
    LocationListener locationListener;
    LatLng alarmPos;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    private int radius = 50;
    LocationRequest mLocationRequest;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alarmPos = new LatLng(0, 0);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        posi = (TextView)findViewById(R.id.positoion);
        //Buton used to set the alarm
        setAlarm = (FloatingActionButton) findViewById(R.id.set_alarm);
        //On click listener for setting the alarm button
        setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
/*
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                if (calculateDis(alarmPos, loc) < 5) {
                    Snackbar.make(setAlarm,"You have arrived",Snackbar.LENGTH_SHORT).show();
                    // locationManager.removeUpdates(locationListener);
                }
                posi.setText(calculateDis(alarmPos, loc)+"");
                Log.d("Location", "" + calculateDis(alarmPos, loc));
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

        */
        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

    }

    //Use to set info for new alarm
    public void setAlarm(String name, LocationCoordiante location, boolean vibrate, Ringtone ringtone, String ringtoneName) {
        mAlarms.add(new GeoAlarm(name, location, vibrate, ringtone, ringtoneName));

        mAdapter.addItem(mAdapter.getItemCount());

        mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
    }


    @Override
    protected void onResume() {
        super.onResume();
        ((GeoAlarmAdapter) mAdapter).setOnItemClickListener(new GeoAlarmAdapter.MyClickListener() {
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
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        Log.d("Intent",""+resultCode);
        if(resultCode==RESULT_OK){
            if(requestCode==REQUEST_CODE){
                // coordinates for destination
                lati=data.getDoubleExtra("latitude",0);
                lang=data.getDoubleExtra("longitude",0);
                alarmPos=new LatLng(lati,lang);
                Log.d("location",alarmPos.toString());
                Toast.makeText(this,""+lati+", "+lang,Toast.LENGTH_SHORT).show();

                //Creates the dialog for configuring the new alarm
                LayoutInflater li=LayoutInflater.from(context);
                View promptsView=li.inflate(R.layout.location_alarm_dialog,null);

                AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(context);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput=(EditText)promptsView.findViewById(R.id.alarm_name_input);
                TextView locationShow=(TextView)promptsView.findViewById(R.id.location_coordinates);
//Button locationSet = (Button) promptsView.findViewById(R.id.select_location);
                final Spinner ringtoneSelect=(Spinner)promptsView.findViewById(R.id.ringtone);
                final CheckBox vibration=(CheckBox)promptsView.findViewById(R.id.vibration);

//Use to retreive ringtones from the phone
                final Map<String, Ringtone>ringtones=new HashMap<>();
                RingtoneManager manager=new RingtoneManager(MainActivity.this);
                manager.setType(RingtoneManager.TYPE_ALARM);
                Cursor cursor=manager.getCursor();
                cursor.moveToFirst();
                while(!cursor.isAfterLast()){
                    ringtones.put(cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX),manager.getRingtone(cursor.getPosition()));
                    cursor.moveToNext();
                }
//cursor.close();

//Extracts the names of the ringtones
                final ArrayList<String>ringtoneNames=new ArrayList<String>();
                for(Map.Entry<String, Ringtone>entry:ringtones.entrySet()){
                    ringtoneNames.add(entry.getKey());
                }
                //Puts the values in the ringtone spinner
                ArrayAdapter<String>dataAdapter=new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item,ringtoneNames);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ringtoneSelect.setAdapter(dataAdapter);
/**********************************************************************************************************************
 locationSet.setOnClickListener(new View.OnClickListener() {
@Override public void onClick(View v) {
startActivityForResult(new Intent(MainActivity.this, CustomPlacePicker.class), REQUEST_CODE);
}

});
 ***********************************************************************************************************************/

                // set dialog message
                alertDialogBuilder
                        .setCancelable(true)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener(){
                                    public void onClick(DialogInterface dialog,int id){
                                        //Sets the alarm. Code needs to be entered
                                        setAlarm(userInput.getText().toString(),
                                                new LocationCoordiante(lati,lang),vibration.isChecked(),
                                                ringtones.get(ringtoneSelect.getSelectedItem()),
                                                ringtoneSelect.getSelectedItem().toString());
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener(){
                                    public void onClick(DialogInterface dialog,int id){
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog=alertDialogBuilder.create();

                // show it
                alertDialog.show();

                locationShow.setText(""+lati+", "+lang);
            }
        }
    }
    // distance formula from current location
    private double calculateDis(LatLng destiny,LatLng myLoc){
        double R=6371e3; // metres
        double φ1=Math.PI*destiny.latitude/180;
        double φ2=Math.PI*myLoc.latitude/180;
        double Δφ=φ2-φ1;
        double Δλ=Math.PI*(destiny.longitude-myLoc.longitude)/180;

        double a=Math.sin(Δφ/2)*Math.sin(Δφ/2)+
                Math.cos(φ1)*Math.cos(φ2)*
                        Math.sin(Δλ/2)*Math.sin(Δλ/2);
        double c=2*Math.atan2(Math.sqrt(a),Math.sqrt(1-a));

        double d=R*c;
        return d;
    }
    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }
    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }
    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            //mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
            //mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
        }
        createLocationRequest();
        startLocationUpdates();
    }


    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(100);
        mLocationRequest.setFastestInterval(10);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                        builder.build());
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
        if (calculateDis(alarmPos, loc) < radius) {
            Snackbar.make(setAlarm,"You have arrived",Snackbar.LENGTH_SHORT).show();
            // locationManager.removeUpdates(locationListener);
        }
        posi.setText(calculateDis(alarmPos, loc)+"");
        Log.d("Location", "" + calculateDis(alarmPos, loc));
    }

}
