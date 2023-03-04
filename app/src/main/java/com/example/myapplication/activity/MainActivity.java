package com.example.myapplication.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Friend;
import com.example.myapplication.LocationService;
import com.example.myapplication.OrientationService;
import com.example.myapplication.R;
import com.example.myapplication.ServerAPI;
import com.example.myapplication.UIRotator;
import com.example.myapplication.ZoomObserver;
import com.example.myapplication.model.LocationItem;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {
    private Button zoomIn;
    private Button zoomOut;
    private ZoomObserver zoom;
    private UIRotator ui;
    private ArrayList<Friend> friends;
    private OrientationService orientationService;
    private LocationService locationService;
    private float myOrientation;
    private LatLng myLocation;
    private ScheduledExecutorService executor;
    private ServerAPI client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setRingUI();
        ui = new UIRotator(this);

        orientationService = OrientationService.singleton(this);
        this.reobserveOrientation();

        locationService = LocationService.singleton(this);
        this.reobserveLocation();

        client = ServerAPI.provide();

        executor = Executors.newScheduledThreadPool(1);

        // Schedule the RequestThread task to run every 3 seconds
        executor.scheduleAtFixedRate(new RequestThread(), 0, 3, TimeUnit.SECONDS);
    }

    private class RequestThread implements Runnable {
        @Override
        public void run() {
            for(int i = 0; i < friends.size(); i++) {
                client.updateLocation(friends.get(i));
            }
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        ui.unRegisterSensor();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ui.registerSensor();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        executor.shutdown();
    }

    private void setRingUI(){
        TextView circle1 = findViewById(R.id.circle1);
        TextView circle2 = findViewById(R.id.circle2);
        TextView circle3 = findViewById(R.id.circle3);
        TextView circle4 = findViewById(R.id.circle4);
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

    public void reobserveOrientation() {
        var orientationData = orientationService.getOrientation();
        orientationData.observe(this, this::onOrientationChanged);
    }

    private void reobserveLocation() {
        var locationData = locationService.getLocation();
        locationData.observe(this, this::onLocationChanged);
    }

    private void onOrientationChanged(Float orientation) {
        myOrientation = orientation;
        for(int i = 0; i < friends.size(); i++) {
            friends.get(i).calculateRelativeAngle(myLocation.latitude, myLocation.longitude, orientation);
        }
    }

    private void onLocationChanged(Pair<Double, Double> latLong) {
        myLocation = new LatLng(latLong.first, latLong.second);
        for(int i = 0; i < friends.size(); i++) {
            friends.get(i).calculateDistance(myLocation);
        }
    }
}