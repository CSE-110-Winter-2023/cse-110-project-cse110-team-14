package com.example.myapplication;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class UpdateCameraLocation {

    public static void updateCameraLocation(double Lat, double Long, float azimuth, GoogleMap map, final int DEFAULT_ZOOM) {
        LatLng lastLocation = new LatLng(Lat, Long);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(lastLocation)
                .zoom(DEFAULT_ZOOM)
                .bearing(azimuth)
                .tilt(0)
                .build();
        map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}
