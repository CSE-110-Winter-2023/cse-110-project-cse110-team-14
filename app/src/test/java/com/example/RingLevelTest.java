package com.example;


import static org.junit.Assert.assertEquals;

import android.widget.Button;

import androidx.test.core.app.ActivityScenario;

import com.example.myapplication.R;
import com.example.myapplication.activity.MainActivity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class RingLevelTest {
    @Test
    public void defaultZoom(){
        try(ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                assertEquals(activity.getZoomLevel(),2);
            });
        }
    }

    @Test
    public void zoomInOnce(){
        try(ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                assertEquals(activity.getZoomLevel(),2);
                Button zoomIn = activity.findViewById(R.id.zoomIn);
                zoomIn.performClick();
                assertEquals(activity.getZoomLevel(),1);
            });
        }
    }

    @Test
    public void zoomOutOnce(){
        try(ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                assertEquals(activity.getZoomLevel(),2);
                Button zoomOut = activity.findViewById(R.id.zoomOut);
                zoomOut.performClick();
                assertEquals(activity.getZoomLevel(),3);
            });
        }
    }

    @Test
    public void zoomInAt1(){
        try(ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                assertEquals(activity.getZoomLevel(),2);
                Button zoomIn = activity.findViewById(R.id.zoomIn);
                zoomIn.performClick();
                zoomIn.performClick();
                assertEquals(activity.getZoomLevel(),1);
            });
        }
    }

    @Test
    public void zoomOutTwice(){
        try(ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                assertEquals(activity.getZoomLevel(),2);
                Button zoomIn = activity.findViewById(R.id.zoomIn);
                zoomIn.performClick();
                zoomIn.performClick();
                assertEquals(activity.getZoomLevel(),1);
                Button zoomOut = activity.findViewById(R.id.zoomOut);
                zoomOut.performClick();
                zoomOut.performClick();
                assertEquals(activity.getZoomLevel(),3);
            });
        }
    }
}
