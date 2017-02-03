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
    public void setmRingtone(Ringtone mRingtone) {
        this.mRingtone = mRingtone;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public void setmRingtoneName(String mRingtoneName) {
        this.mRingtoneName = mRingtoneName;
    }

    public void setmVibration(boolean mVibration) {
        this.mVibration = mVibration;
    }

    public void setmLocationCoordinate(LocationCoordiante mLocationCoordinate) {
        this.mLocationCoordinate = mLocationCoordinate;
    }

    public void setmRadius(int mRadius) {
        this.mRadius = mRadius;
    }

    public void setVibration(boolean vib){
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

    public String getmName() {
        return mName;
    }

    public String getmRingtoneName() {
        return mRingtoneName;
    }

    public boolean ismVibration() {
        return mVibration;
    }

    public Ringtone getmRingtone() {
        return mRingtone;
    }

    public LocationCoordiante getmLocationCoordinate() {
        return mLocationCoordinate;
    }

    public int getRadius() {
        return mRadius;
    }

    public int getTime() {
        return mTime;
    }
}
