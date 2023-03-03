package com.example.myapplication;

import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.maps.model.Marker;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class UpdateIcon {

    /**
     * updateIconOrientation Method
     */
    public static void updateIconOrientation(ArrayList<Marker> markerList, ArrayList<View> markerView, float azimuth, double lastLat, double lastLong, final int DEFAULT_ZOOM) {
        if(markerList.size() > 0) {
            for(int i = 0; i < markerList.size(); i++) {
                Marker curMarker = markerList.get(i);
                double markerLat = curMarker.getPosition().latitude;
                double markerLong = curMarker.getPosition().longitude;
                float bearingAngle = (float)calculateBearingAngle(lastLat, lastLong, markerLat, markerLong);
                double markerDistance = calculateDistance(lastLat, lastLong, markerLat, markerLong);
                View locationIconView = markerView.get(i);
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) locationIconView.getLayoutParams();
                layoutParams.circleAngle = bearingAngle-azimuth;
                //Toast.makeText(this, ""+lastLat+"   "+markerLat+"   "+bearingAngle, Toast.LENGTH_LONG).show();
                locationIconView.setLayoutParams(layoutParams);
                if(markerDistance < 33*DEFAULT_ZOOM) {
                    locationIconView.setVisibility(View.INVISIBLE);
                } else {
                    locationIconView.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    /**
     * calculateBearingAngle Method
     */
    public static double calculateBearingAngle(double lat1, double long1, double lat2, double long2) {
        double dLong = Math.toRadians(long2 - long1);
        double lat1R = Math.toRadians(lat1);
        double lat2R = Math.toRadians(lat2);
        double y = Math.sin(dLong) * Math.cos(lat2R);
        double x = Math.cos(lat1R) * Math.sin(lat2R) - Math.sin(lat1R) * Math.cos(lat2R) * Math.cos(dLong);
        double radian = Math.atan2(y, x);
        double bearing = Math.toDegrees(radian);
        bearing = (bearing + 360) % 360;
        return bearing;
    }

    public static double calculateDistance(double lat1, double long1, double lat2, double long2) {
        double R = 6378137; // Earth’s mean radius in meter
        double dLat = Math.toRadians(lat2 - lat1);
        double dLong = Math.toRadians(long2 - long1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLong / 2) * Math.sin(dLong / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c;
        return d; // returns the distance in meter
    }
}
