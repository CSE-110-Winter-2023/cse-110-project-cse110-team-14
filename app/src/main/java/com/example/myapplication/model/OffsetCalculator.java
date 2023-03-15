package com.example.myapplication.model;

import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

import android.util.Log;

import androidx.constraintlayout.widget.ConstraintLayout;

public class OffsetCalculator {

    public static int calculateOffset(int absWidthDiff, int absHeightDiff, double absRadians) {

        int temp = (int) (abs(cos(absRadians)) * absWidthDiff + abs(sin(absRadians)) * absHeightDiff);
        return temp;

    }

}
