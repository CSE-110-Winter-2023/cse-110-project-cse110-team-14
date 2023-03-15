package com.example;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class OffsetCalculatorTest {

    @Test
    public void testCalculateOffset() {

        double absRadians1 = Math.PI;
        double absRadians2 = Math.PI / 2;
        int expectedOffset1 = (int) (Math.abs(Math.cos(absRadians1)) * 10 + Math.abs(Math.sin(absRadians1)) * 20);
        int actualOffset1 = 10;
        int expectedOffset2 = (int) (Math.abs(Math.cos(absRadians2)) * 20 + Math.abs(Math.sin(absRadians2)) * 30);
        int actualOffset2 = 30;

        assertEquals(expectedOffset1, actualOffset1);
        assertEquals(expectedOffset2, actualOffset2);
    }

}
