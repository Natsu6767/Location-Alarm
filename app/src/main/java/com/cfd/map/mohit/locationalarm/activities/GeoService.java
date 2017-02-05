package com.cfd.map.mohit.locationalarm.activities;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;

public class GeoService extends Service {

    LocationListener locationListener;
    LocationManager locationManager;
    private ArrayList<GeoAlarm> geoAlarms;
    private RingtoneManager ringtoneManager;
    private MediaPlayer ringtone;
    private Uri uri;
    NotificationManager notificationManager;

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
        //onStartCommand(intent,flags,startId);

    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        loadAlarms();
        Log.d("Service", "loading alarms");
        Toast.makeText(this, "starting sevices", Toast.LENGTH_LONG).show();
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                //makeUseOfNewLocation(location);
                LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());

                if (geoAlarms != null) {
                    if (!geoAlarms.isEmpty()) {
                        for (GeoAlarm geoAlarm : geoAlarms) {

                            Log.d("Service", "checking alarms");
                            System.out.println(calculateDis(geoAlarm.getLatLang(), loc) + "");
                            System.out.println(geoAlarm.getLatLang() + "");
                            if (calculateDis(geoAlarm.getLatLang(), loc) < geoAlarm.getRadius()) {
                                ringtoneManager = new RingtoneManager(GeoService.this);
                                ringtoneManager.setType(RingtoneManager.TYPE_ALARM);
                                Log.d("Service", "playing alarms");
                                uri = Uri.parse(geoAlarm.getRingtoneUri());
                                playAlarm(uri);
                                Toast.makeText(GeoService.this, "" + "You Have Arrived", Toast.LENGTH_SHORT).show();

                                geoAlarms.remove(geoAlarm);
                                break;
                            }
                        }
                    }
                }


            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

// Register the listener with the Location Manager to receive location updates

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
        }


        Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Stopping Service", Toast.LENGTH_SHORT).show();
        Log.d("Service", "stopping service");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.removeUpdates(locationListener);
        }
        if(ringtone!=null) {
            ringtone.stop();
            ringtone.release();
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

    public void loadAlarms() {
        AlarmDatabase alarmDatabase = new AlarmDatabase(getApplicationContext());
       geoAlarms = alarmDatabase.getAllData();
    }

    public void playAlarm(Uri uri) {
        if(ringtone!=null){
            if(ringtone.isPlaying()){
                return;
            }
        }
        try {
            ringtone = new MediaPlayer();
            ringtone.setDataSource(GeoService.this, uri);
            ringtone.setLooping(true);
            ringtone.prepare();
            ringtone.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent intent1 = new Intent(getApplicationContext(),AlarmScreenActivity.class);
        //intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this.getApplicationContext(),0,intent1,PendingIntent.FLAG_UPDATE_CURRENT);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent1);
        /*NotificationCompat.Builder notificationCompat = new NotificationCompat.Builder(this.getApplicationContext())
                .setContentTitle("Alarm")
                .setContentText("Tic tok Tic")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        notificationManager.notify(1,notificationCompat.build());*/
    }

}
