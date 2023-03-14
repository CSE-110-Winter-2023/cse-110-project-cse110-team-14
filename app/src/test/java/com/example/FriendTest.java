package com.example;

import static org.junit.Assert.assertEquals;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.myapplication.FirstOpened;
import com.example.myapplication.Friend;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class FriendTest {
    private Friend f;

    @Before
    public void create(){
        f = new Friend("123456789", "Name", 15, -115, 1);
    }

    @Test
    public void testName(){
        String inputTest = "newName";

        f.setLabel(inputTest);
        assertEquals(inputTest, f.getLabel());
    }

    @Test
    public void testLocation(){
        double lat = 16;
        double lont = -10;

        f.setLatitude(lat);
        f.setLongitude(lont);

        assertEquals(lat, f.getLatitude(), 0);
        assertEquals(lont, f.getLongitude(),0);
    }
}
