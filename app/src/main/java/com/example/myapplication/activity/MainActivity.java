package com.example.myapplication.activity;

import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.util.Pair;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.AddFriendDialog;
import com.example.myapplication.CheckVisibility;
import com.example.myapplication.DistanceToDp;
import com.example.myapplication.DpSpPxConversion;
import com.example.myapplication.FirstOpened;
import com.example.myapplication.Friend;
import com.example.myapplication.FriendViewAdaptor;
import com.example.myapplication.LocationService;
import com.example.myapplication.OrientationService;
import com.example.myapplication.R;
import com.example.myapplication.ServerAPI;
import com.example.myapplication.UIRotator;
import com.example.myapplication.UpdateIcon;
import com.example.myapplication.ZoomObserver;
import com.example.myapplication.model.LocationItem;
import com.example.myapplication.model.MeterToMile;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {
    final String START = "Start";
    private Button zoomIn;
    private Button zoomOut;
    private ZoomObserver zoom;
    private UIRotator ui;
    private FirstOpened open;
    private ArrayList<Friend> friends = new ArrayList<>();
    private OrientationService orientationService;
    private LocationService locationService;
    private float myOrientation;
    private LatLng myLocation = new LatLng(-30, 117);
    private ScheduledExecutorService executor;
    private ServerAPI client;
    private FriendViewAdaptor viewAdaptor;
    private Context context = this;
    private Button addFriend;
    private FriendDao dao;
    private FriendDatabase db;


    private float bearingAngle;
    private float azimuth = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setUp();
        this.setRingUI();
        ui = new UIRotator(this);
        
        addFriend = findViewById(R.id.addFriendBtn);
        addFriend.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddFriendActivity.class);
            startActivity(intent);
        });

        orientationService = OrientationService.singleton(this);
        this.reobserveOrientation();

        locationService = LocationService.singleton(this);
        this.reobserveLocation();

        viewAdaptor = new FriendViewAdaptor(this, findViewById(R.id.constraintLayout));

        //for testing
        Friend f1 = new Friend("42424242", "abc", 30, -117, 1);
        Friend f2 = new Friend("38383838", "bcd", 25, -117, 1);
        friends.add(f1);
        friends.add(f2);
        for (int i = 0; i < friends.size(); ++i) {
            viewAdaptor.addNewView(friends.get(i));
        }
        db = FriendDatabase.provide(this);
        dao = db.getDao();


        client = ServerAPI.provide();

        executor = Executors.newScheduledThreadPool(1);

        this.setUpAddFriendButton();

        // Schedule the RequestThread task to run every 1 seconds
        this.scheduleRate(0,1);
    }

    private class RequestThread implements Runnable {
        @Override
        public void run() {
            for(int i = 0; i < friends.size(); i++) {
                client.updateLocation(friends.get(i));
            }
        }
    }

    private void scheduleRate(int initial, int period){
        executor.scheduleAtFixedRate(new RequestThread(), initial, period, TimeUnit.SECONDS);
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

    private void setUp(){
        ui = new UIRotator(this);
        open = new FirstOpened(this, this);

        orientationService = OrientationService.singleton(this);
        locationService = LocationService.singleton(this);
        this.reobserveOrientation();
        this.reobserveLocation();

        viewAdaptor = new FriendViewAdaptor(this, findViewById(R.id.constraintLayout));
        client = ServerAPI.provide();
        executor = Executors.newScheduledThreadPool(1);
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

    private void setUpAddFriendButton(){
        addFriend = findViewById(R.id.addFriendBtn);
        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddFriendDialog dialog = new AddFriendDialog(context);
                dialog.addNewFriendDialog(friends, viewAdaptor);
            }
        });
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
            double angle = friends.get(i).calculateRelativeAngle(myLocation.latitude, myLocation.longitude, orientation);
            viewAdaptor.changeAngle(i, angle);
        }
    }

    private void onLocationChanged(Pair<Double, Double> latLong) {
        myLocation = new LatLng(latLong.first, latLong.second);
        for(int i = 0; i < friends.size(); i++) {
            double distance = friends.get(i).calculateDistance(myLocation);
            viewAdaptor.changeDistance(i, distance, zoom.getZoomLevel());
        }
    }

    @VisibleForTesting
    public int getZoomLevel() {
        return zoom.getZoomLevel();
    }

}