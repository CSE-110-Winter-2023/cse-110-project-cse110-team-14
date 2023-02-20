package com.example;

import static org.junit.Assert.assertEquals;

import com.example.myapplication.MapsActivity;

import org.junit.Test;
public class DistanceCalculationTest {
    @Test
    public void test1() {
        double distance = MapsActivity.calculateDistance(0,0,0,0);
        assertEquals(0,distance,0);
    }

    @Test
    public void test2() {
        double distance = MapsActivity.calculateDistance(-0,-0,-0,-0);
        assertEquals(0,distance,0);
    }

    @Test
    public void test3() {
        double distance = MapsActivity.calculateDistance(90,0,90,0);
        assertEquals(0,distance,0);
    }

    @Test
    public void test4() {
        double distance = MapsActivity.calculateDistance(90,60,60,60);
        assertEquals(3339584.723798207,distance,0);
    }

    @Test
    public void test5() {
        double distance = MapsActivity.calculateDistance(0,0,60,60);
        assertEquals(8407124.886903487,distance,0);
    }
}
