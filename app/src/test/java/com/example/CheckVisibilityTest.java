package com.example;

import static org.junit.Assert.assertEquals;

import com.example.myapplication.CheckVisibility;

import org.junit.Test;

public class CheckVisibilityTest {

    @Test
    public void test1() {
        int zoomLevel = 4;
        double distance = 500;
        boolean result = CheckVisibility.checkDistance(zoomLevel,distance);

        assertEquals(true, result);
    }

    @Test
    public void test2() {
        int zoomLevel = 3;
        double distance = 501;
        boolean result = CheckVisibility.checkDistance(zoomLevel,distance);

        assertEquals(false, result);
    }

    @Test
    public void test3() {
        int zoomLevel = 1;
        double distance = 1.1;
        boolean result = CheckVisibility.checkDistance(zoomLevel,distance);

        assertEquals(false, result);
    }

    @Test
    public void test4() {
        int zoomLevel = 1;
        double distance = 0.5;
        boolean result = CheckVisibility.checkDistance(zoomLevel,distance);

        assertEquals(true, result);
    }

    @Test
    public void test5() {
        int zoomLevel = 2;
        double distance = 10;
        boolean result = CheckVisibility.checkDistance(zoomLevel,distance);

        assertEquals(true, result);
    }

    @Test
    public void test6() {
        int zoomLevel = 4;
        double distance = 1000;
        boolean result = CheckVisibility.checkDistance(zoomLevel,distance);

        assertEquals(true, result);
    }
}
