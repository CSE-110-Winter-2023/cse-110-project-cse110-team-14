package com.example.myapplication;

import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

import android.content.Context;

public class Utilities {

    public static int calculateOffset(int absWidthDiff, int absHeightDiff, double absRadians) {

        int temp = (int) (abs(cos(absRadians)) * absWidthDiff + abs(sin(absRadians)) * absHeightDiff);
        return temp;

    }

    public static float setOrientation(float[] orientation) {
        float azimuth;
        azimuth = (float)Math.toDegrees(orientation[0]);
        azimuth = (azimuth + 360) % 360;
        return azimuth;
    }

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

    public static double toMile(double meter) {
        double mile = meter * 0.000621371;
        return mile;
    }

    public static int calculatePixels(int dp, Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        int pixels = (int) (dp * scale + 0.5f);
        return pixels;
    }

    public static int spToPx(float sp, Context context) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        int pixels = (int) (sp * scale + 0.5f);
        return pixels;
    }
}
