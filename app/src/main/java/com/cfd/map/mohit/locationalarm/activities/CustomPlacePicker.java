package com.cfd.map.mohit.locationalarm.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.cfd.map.mohit.locationalarm.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class CustomPlacePicker extends AppCompatActivity implements OnMapReadyCallback {

    MapFragment mapFragment;
    private GoogleMap mMap;
    PlaceAutocompleteFragment autocompleteFragment;
    Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_place_picker);
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                marker.setPosition(place.getLatLng());
                mMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
            }

            @Override
            public void onError(Status status) {

            }
        });
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        mMap = map;
        marker = map.addMarker(new MarkerOptions()
                .position(new LatLng(-34, 151))
                .title("Marker"));
        map.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                LatLng lat = map.getCameraPosition().target;
                marker.setPosition(lat);
            }
        });
        if (ActivityCompat.checkSelfPermission(CustomPlacePicker.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
        Log.d("permission", "already have permission");

    }

    public void setLocation(View view) {
        LatLng pos = marker.getPosition();
        Intent intent = new Intent();
        intent.putExtra("latitude", pos.latitude);
        intent.putExtra("longitude", pos.longitude);
        setResult(RESULT_OK, intent);
        finish();
    }
}
