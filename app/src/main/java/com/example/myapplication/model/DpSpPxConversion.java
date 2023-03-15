package com.example.myapplication.model;

import android.content.Context;

public class DpSpPxConversion {

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
