package com.example;


import static org.junit.Assert.assertEquals;

import androidx.test.core.app.ActivityScenario;

import com.example.myapplication.Utilities;
import com.example.myapplication.activity.MainActivity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class DpPxConversionTest {

    @Test
    public void test1() {
        try(ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                int result = Utilities.calculatePixels(150, activity);
                assertEquals(result, 150);
            });
        }
    }

    @Test
    public void test2() {
        try(ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                int result = Utilities.calculatePixels(300, activity);
                assertEquals(result, 300);
            });
        }
    }

    @Test
    public void test3() {
        try(ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                int result = Utilities.calculatePixels(0, activity);
                assertEquals(result, 0);
            });
        }
    }
}
