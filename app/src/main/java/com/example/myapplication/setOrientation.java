package com.example.myapplication;


public class setOrientation {

    /**
     * setOrientation Method
     * Calculates the azimuth number for rotation.
     */
    public static float setOrientation(float[] orientation) {
        float azimuth;
        azimuth = (float)Math.toDegrees(orientation[0]);
        azimuth = (azimuth + 360) % 360;
        return azimuth;
    }
}
