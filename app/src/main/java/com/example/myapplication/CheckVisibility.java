package com.example.myapplication;

public class CheckVisibility {
    public static boolean checkDistance(int zoomLevel, double distance) {
        if (zoomLevel == 4 && distance > 1000) {
            return false;
        }
        else if (zoomLevel == 3 && distance > 500) {
            return false;
        }
        else if (zoomLevel == 2 && distance > 10) {
            return false;
        }
        else if (zoomLevel == 1 && distance > 1) {
            return false;
        }
        return true;
    }
}
