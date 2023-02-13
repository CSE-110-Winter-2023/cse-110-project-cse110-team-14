package com.example;

import java.lang.Math;
import static org.junit.Assert.assertEquals;

import com.example.myapplication.setOrientation;

import org.junit.Test;

public class setOrientationTest {
    @Test
    public void test1() {
        float[] orientation = new float[3];
        orientation[0] = 0;
        orientation[1] = 0;
        orientation[2] = 0;
        float result = setOrientation.setOrientation(orientation);
        assertEquals(0,result, 0);
    }

    @Test
    public void test2() {
        float[] orientation = new float[3];
        orientation[0] = (float)-Math.PI;
        orientation[1] = 0;
        orientation[2] = 0;
        float result = setOrientation.setOrientation(orientation);
        assertEquals(180,result, 0);
    }

    @Test
    public void test3() {
        float[] orientation = new float[3];
        orientation[0] = (float)-(Math.PI)/2;
        orientation[1] = 0;
        orientation[2] = 0;
        float result = setOrientation.setOrientation(orientation);
        assertEquals(270,result, 0);
    }

    @Test
    public void test4() {
        float[] orientation = new float[3];
        orientation[0] = (float)(Math.PI)/2;
        orientation[1] = 0;
        orientation[2] = 0;
        float result = setOrientation.setOrientation(orientation);
        assertEquals(90,result, 0);
    }

    @Test
    public void test5() {
        float[] orientation = new float[3];
        orientation[0] = (float)(Math.PI)/4;
        orientation[1] = 0;
        orientation[2] = 0;
        float result = setOrientation.setOrientation(orientation);
        assertEquals(45,result, 0);
    }
}
