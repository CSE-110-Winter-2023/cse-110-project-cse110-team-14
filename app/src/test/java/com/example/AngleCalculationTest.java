package com.example;

import static org.junit.Assert.assertEquals;

import com.example.myapplication.MapsActivity;

import org.junit.Test;
public class AngleCalculationTest {

    @Test
    public void test1() {
        double bearing = MapsActivity.calculateBearingAngle(0,0,0,0);
        assertEquals(0,bearing,0);
    }

    @Test
    public void test2() {
        double bearing = MapsActivity.calculateBearingAngle(-0,-0,-0,-0);
        assertEquals(0,bearing,0);
    }

    @Test
    public void test3() {
        double bearing = MapsActivity.calculateBearingAngle(0,117,0,117);
        assertEquals(0,bearing,0);
    }

    @Test
    public void test4() {
        double bearing = MapsActivity.calculateBearingAngle(45,90,0,0);
        assertEquals(270,bearing,0);
    }

    @Test
    public void test5() {
        double bearing = MapsActivity.calculateBearingAngle(270,90,0,0);
        assertEquals(270,bearing,0);
    }

    @Test
    public void test6() {
        double bearing = MapsActivity.calculateBearingAngle(-270,-90,0,0);
        assertEquals(90,bearing,0);
    }
}
