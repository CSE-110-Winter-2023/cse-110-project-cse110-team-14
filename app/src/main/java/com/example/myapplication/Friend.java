package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.myapplication.model.MeterToMile;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "friends")
public class Friend {

    @PrimaryKey
    @SerializedName("public_code")
    @NonNull
    public String public_code;

    @SerializedName("label")
    @NonNull
    public String label;

    @SerializedName("latitude")
    public double latitude;

    @SerializedName("longitude")
    public double longitude;

    @SerializedName(value = "version")
    public int version;

    @SerializedName("distance")
    public double distance;

    @SerializedName("relative_angle")
    public double relativeAngle;

    public Friend(String public_code, String label, double latitude, double longitude, int version){
        this.public_code = public_code;
        this.label = label;
        this.latitude = latitude;
        this.longitude = longitude;
        this.version = version;
    }

    public void setPublic_code(String public_code) {
        this.public_code = public_code;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getPublic_code() {
        return this.public_code;
    }

    public String getLabel() {
        return this.label;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public int getVersion() {
        return this.version;
    }

    public double calculateDistance(LatLng location) {
        double R = 6378137; // Earth’s mean radius in meter
        double dLat = Math.toRadians(latitude - location.latitude);
        double dLong = Math.toRadians(longitude - location.longitude);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(location.latitude)) * Math.cos(Math.toRadians(latitude)) *
                        Math.sin(dLong / 2) * Math.sin(dLong / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        distance = R * c;
        distance = MeterToMile.toMile(distance); //meter to mile
        return distance;
    }

    public double calculateRelativeAngle(double lat, double lng, float azimuth) {
        double dLong = Math.toRadians(longitude - lng);
        double lat1R = Math.toRadians(lat);
        double lat2R = Math.toRadians(latitude);
        double y = Math.sin(dLong) * Math.cos(lat2R);
        double x = Math.cos(lat1R) * Math.sin(lat2R) - Math.sin(lat1R) * Math.cos(lat2R) * Math.cos(dLong);
        double radian = Math.atan2(y, x);
        double bearing = Math.toDegrees(radian);
        bearing = (bearing + 360) % 360;
        relativeAngle = bearing - azimuth;
        return relativeAngle;
    }
}
