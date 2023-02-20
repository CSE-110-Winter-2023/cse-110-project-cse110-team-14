package com.example;

import static org.junit.Assert.assertEquals;

import com.example.myapplication.DpSpPxConversion;
import com.example.myapplication.MapsActivity;
import com.example.myapplication.UpdateIcon;

import org.junit.Test;
public class AngleCalculationTest {

    @Test
    public void test1() {
        double bearing = UpdateIcon.calculateBearingAngle(0,0,0,0);
        assertEquals(0,bearing,0);
    }

    @Test
    public void test2() {
        double bearing = UpdateIcon.calculateBearingAngle(-0,-0,-0,-0);
        assertEquals(0,bearing,0);
    }

    @Test
    public void test3() {
        double bearing = UpdateIcon.calculateBearingAngle(0,117,0,117);
        assertEquals(0,bearing,0);
    }

    @Test
    public void test4() {
        double bearing = UpdateIcon.calculateBearingAngle(45,90,0,0);
        assertEquals(270,bearing,0);
    }

    @Test
    public void test5() {
        double bearing = UpdateIcon.calculateBearingAngle(270,90,0,0);
        assertEquals(270,bearing,0);
    }

    @Test
    public void test6() {
        double bearing = UpdateIcon.calculateBearingAngle(-270,-90,0,0);
        assertEquals(90,bearing,0);
    }
}
