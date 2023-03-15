package com.example;

import static org.junit.Assert.assertEquals;

import com.example.myapplication.Utilities;

import org.junit.Test;

public class MetertoMileTest {

    @Test
    public void test1() {
        double meter = 1.1;
        double result = Utilities.toMile(meter);

        assertEquals(0.0006835081, result, 0);
    }

    @Test
    public void test2() {
        double meter = 1;
        double result = Utilities.toMile(meter);

        assertEquals(0.000621371, result, 0);
    }

    @Test
    public void test3() {
        double meter = 0;
        double result = Utilities.toMile(meter);

        assertEquals(0, result, 0);
    }

    @Test
    public void test4() {
        double meter = 2;
        double result = Utilities.toMile(meter);

        assertEquals(0.001242742, result, 0);
    }
}
