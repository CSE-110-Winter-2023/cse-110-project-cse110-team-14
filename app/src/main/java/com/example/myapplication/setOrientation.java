package com.example.myapplication;


public class setOrientation {

    public static float setOrientation(float[] orientation) {
        float azimuth;
        azimuth = (float)Math.toDegrees(orientation[0]);
        azimuth = (azimuth + 360) % 360;
        return azimuth;
    }
}
