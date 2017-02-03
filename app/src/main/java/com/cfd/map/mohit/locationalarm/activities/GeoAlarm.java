package com.cfd.map.mohit.locationalarm.activities;

import android.media.Ringtone;

/**
 * Created by Mohit on 2/2/2017.
 */

public class GeoAlarm {

    private String mName, mRingtoneName;
    private boolean mVibration;
    private Ringtone mRingtone;
    private LocationCoordiante mLocationCoordinate;
    private int radius;

    public GeoAlarm(String name, LocationCoordiante locationCoordinate, boolean vibration, Ringtone ringtone, String ringtoneName) {
        mName = name;
        mLocationCoordinate = locationCoordinate;
        mVibration = vibration;
        mRingtone = ringtone;
        mRingtoneName = ringtoneName;
    }

    //Setter Methods
    public void setName(String name){
        mName = name;
    }
    public void setRingtone(String name, Ringtone value){
        mRingtoneName = name;
        mRingtone = value;

    }
    public void setVibration(boolean vib){
        mVibration = vib;
    }

    //Getter Methods
    public String getName() {
        return mName;
    }

    public String getLocationCoordinate() {

        return ("" + mLocationCoordinate.longitude + ", " + mLocationCoordinate.latitude);
    }

    public boolean getVibration() {
        return mVibration;
    }

    public Ringtone getRingtone() {
        return mRingtone;
    }

    public String getRingtoneName() {
        return mRingtoneName;
    }
}
