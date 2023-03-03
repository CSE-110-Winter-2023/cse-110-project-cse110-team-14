package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.myapplication.R;

public class MainActivity extends AppCompatActivity {
    private int zoomLevel2 = 90;
    private int zoomLevel3 = 70;
    private int zoomLevel4 = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}