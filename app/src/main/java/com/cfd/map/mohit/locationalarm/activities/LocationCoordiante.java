package com.cfd.map.mohit.locationalarm.activities;

/**
 * Created by Mohit on 2/3/2017.
 */

public class LocationCoordiante {

    public double latitude, longitude;

    public LocationCoordiante(double x, double y) {
        latitude = x;
        longitude = y;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
