package com.example.myapplication;

public class CheckVisibility {
    public static boolean checkDistance(int zoomLevel, float distance) {
        if (zoomLevel == 4 && distance > 500) {
            return false;
        }
        else if (zoomLevel == 3 && distance > 250) {
            return false;
        }
        else if (zoomLevel == 2 && distance > 5) {
            return false;
        }
        else if (zoomLevel == 1 && distance > 0.5) {
            return false;
        }
        return true;
    }
}
