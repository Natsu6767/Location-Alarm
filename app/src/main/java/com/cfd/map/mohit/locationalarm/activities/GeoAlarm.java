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

    public GeoAlarm(String name, LocationCoordiante locationCoordinate, boolean vibration, Ringtone ringtone, String ringtoneName) {
        mName = name;
        mLocationCoordinate = locationCoordinate;
        mVibration = vibration;
        mRingtone = ringtone;
        mRingtoneName = ringtoneName;
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
