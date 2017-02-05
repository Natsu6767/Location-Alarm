package com.cfd.map.mohit.locationalarm.activities;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Mohit on 2/2/2017.
 */

public class GeoAlarm {

    private String mName, mRingtoneName;
    private boolean mVibration, on_off;
    private String mRingtone;
    private LocationCoordiante mLocationCoordinate;
    private int mRadius;
    private int mId;

    public int getmId() {
        return mId;
    }

    public GeoAlarm() {

    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public GeoAlarm(String name, LocationCoordiante locationCoordinate, boolean vibration,
                    String ringtone, String ringtoneName, int radius) {
        mName = name;
        mLocationCoordinate = locationCoordinate;
        mVibration = vibration;
        mRingtone = ringtone;
        mRingtoneName = ringtoneName;
        mRadius = radius;
    }

    //Setter Methods
    public void setName(String name) {
        mName = name;
    }

    public void setRingtone(String name, String value) {
        mRingtoneName = name;
        mRingtone = value;

    }

    public void setLocationCoordinate(LocationCoordiante locationCoordinate) {
        mLocationCoordinate = locationCoordinate;
    }

    public void setVibration(boolean vib) {
        mVibration = vib;
    }

    public void setRadius(int r) {
        mRadius = r;
    }

    public void setStatus(boolean status) {
        on_off = status;
    }


    //Getter Methods
    public String getName() {
        return mName;
    }

    public String getLocationCoordinate() {

        return ("" + mLocationCoordinate.latitude + ", " + mLocationCoordinate.longitude);
    }

    public boolean getVibration() {
        return mVibration;
    }

    public String getRingtoneUri() {
        return mRingtone;
    }

    public String getRingtoneName() {
        return mRingtoneName;
    }

    public LocationCoordiante getmLocationCoordinate() {
        return mLocationCoordinate;
    }

    public int getRadius() {
        return mRadius;
    }

    public LatLng getLatLang() {
        return new LatLng(getmLocationCoordinate().getLatitude(), getmLocationCoordinate().getLongitude());
    }

    public boolean getStatus() {
        return on_off;
    }
}
