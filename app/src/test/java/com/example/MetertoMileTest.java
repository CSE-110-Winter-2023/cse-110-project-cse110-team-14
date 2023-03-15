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

}
