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
    private int mRadius, mTime;

    public GeoAlarm(String name, LocationCoordiante locationCoordinate, boolean vibration,
                    Ringtone ringtone, String ringtoneName, int radius, int time) {
        mName = name;
        mLocationCoordinate = locationCoordinate;
        mVibration = vibration;
        mRingtone = ringtone;
        mRingtoneName = ringtoneName;
        mRadius = radius;
        mTime = time;
    }

    //Setter Methods
    public void setName(String name) {
        mName = name;
    }

    public void setRingtone(String name, Ringtone value) {
        mRingtoneName = name;
        mRingtone = value;

    }

    public void setVibration(boolean vib) {
        mVibration = vib;
    }

    public void setRadius(int r){
        mRadius = r;
    }
    public void setTime(int t){
        mTime = t;
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

    public int getRadius() {
        return mRadius;
    }

    public int getTime() {
        return mTime;
    }
}
