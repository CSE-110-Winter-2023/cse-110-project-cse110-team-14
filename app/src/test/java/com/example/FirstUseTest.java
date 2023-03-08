package com.example;

import static org.junit.Assert.assertEquals;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;


import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.myapplication.FirstOpened;
import com.example.myapplication.activity.MainActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class FirstUseTest {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    final String NAME_KEY = "name";
    final String UID_KEY = "uid";

    @Before
    public void createSP(){
        Context context = ApplicationProvider.getApplicationContext();
        sp = context.getSharedPreferences("start", Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    @Test
    public void testNameSaved(){
        String inputTest = "test";

        editor.putString(NAME_KEY, inputTest);
        editor.apply();

        String input = sp.getString(NAME_KEY, "");
        assertEquals(inputTest, input);
    }

    @Test
    public void testUIDSaved(){
        String inputTest = "private unique ID";

        editor.putString(UID_KEY, inputTest);
        editor.apply();

        String UID = sp.getString(UID_KEY, "");
        assertEquals(true, UID instanceof String);
    }

    @Test
    public void testCorrectName1(){
        String inputTest = "proper name";

        boolean check = FirstOpened.checkName(inputTest);
        assertEquals(true, check);
    }

    @Test
    public void testCorrectName2(){
        String inputTest = "Simplename";

        boolean check = FirstOpened.checkName(inputTest);
        assertEquals(true, check);
    }

    @Test
    public void testIncorrectName(){
        String inputTest = "proper ** name";

        boolean check = FirstOpened.checkName(inputTest);
        assertEquals(false, check);
    }
}
