package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.ZoomObserver;

public class MainActivity extends AppCompatActivity {

    private TextView circle1;
    private TextView circle2;
    private TextView circle3;
    private TextView circle4;
    private Button zoomIn;
    private Button zoomOut;

    private ZoomObserver zoom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setRingUI();
    }

    private void setRingUI(){
        circle1 = findViewById(R.id.circle1);
        circle2 = findViewById(R.id.circle2);
        circle3 = findViewById(R.id.circle3);
        circle4 = findViewById(R.id.circle4);
        zoom = new ZoomObserver(circle1, circle2, circle3, circle4);

        zoomIn = findViewById(R.id.zoomIn);
        zoomOut = findViewById(R.id.zoomOut);
    }

    public void onZoomInClicked(View v) {
        zoom.zoomIn();

        if(zoom.getZoomLevel() == 1) {
            zoomIn.setAlpha(.5f);
            zoomIn.setClickable(false);
        }
        else{
            zoomOut.setAlpha(1);
            zoomOut.setClickable(true);
        }
    }

    public void onZoomOutClicked(View v) {
        zoom.zoomOut();
        if(zoom.getZoomLevel() == 4) {
            zoomOut.setAlpha(.5f);
            zoomOut.setClickable(false);
        }
        else{
            zoomIn.setAlpha(1);
            zoomIn.setClickable(true);
        }
    }
}