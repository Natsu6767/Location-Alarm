package com.cfd.map.mohit.locationalarm.activities;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Random;

public class GeoService extends Service {

    LocationListener locationListener;
    LocationManager locationManager;
    private int radius = 10;
    private LatLng alarmPos ;
    private ArrayList<GeoAlarm> geoAlarms;
    public GeoService() {
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        //alarmPos = new LatLng(0,0);
        loadAlarms();
        alarmPos = new LatLng(intent.getDoubleExtra("latitude",1),intent.getDoubleExtra("longitude",1));
        Toast.makeText(this,"starting sevices",Toast.LENGTH_LONG).show();

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                //makeUseOfNewLocation(location);
                LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());

                if(geoAlarms!=null){
                    for(GeoAlarm geoAlarm:geoAlarms){
                        if(calculateDis(geoAlarm.getLatLang(),loc)<geoAlarm.getRadius()){

                        }
                    }
                }
                if (alarmPos != null) {
                    Log.d("Location", alarmPos.toString());
                    if (calculateDis(alarmPos, loc) < radius) {

                        Toast.makeText(GeoService.this, "You have arrived", Toast.LENGTH_SHORT).show();
                        stopSelf();

                    }
                    Log.d("Location", "" + calculateDis(alarmPos, loc));
                }
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

// Register the listener with the Location Manager to receive location updates
       // locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);

        Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Stopping Service", Toast.LENGTH_SHORT).show();
        locationManager.removeUpdates(locationListener);
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
    public void setAlarm(LatLng alarm){
        alarmPos = new LatLng(alarm.latitude,alarm.longitude);
        Log.d("position",alarm.toString());
    }

    public void loadAlarms(){
        geoAlarms = MainActivity.alarmDatabase.getAllData();
    }

}
