package com.cfd.map.mohit.locationalarm.activities;

/**
 * Created by Mohit on 2/2/2017.
 */

public class Alarm {

    private String mName;
    private double mLocationCoordinate;
    private boolean mVibration;
    private int mRingtone;

    public Alarm(String name, double locationCoordinate, boolean vibration, int ringtone) {
        mName = name;
        mLocationCoordinate = locationCoordinate;
        mVibration = vibration;
        mRingtone = ringtone;
    }

    //Setter Methods
    public void setName(String name) {
        mName = name;
    }

    public void setLocationCoordinate(double locationCoordinate) {
        mLocationCoordinate = locationCoordinate;
    }

    public void setVibration(boolean vibration) {
        mVibration = vibration;
    }

    public void setRingtone(int ringtone) {
        mRingtone = ringtone;
    }

    //Getter Methods
    public String getName() {
        return mName;
    }

    public double getLocationCoordinate() {
        return mLocationCoordinate;
    }

    public boolean getVibration() {
        return mVibration;
    }

    public int getRingtone() {
        return mRingtone;
    }
}
