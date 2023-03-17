package com.example.myapplication.activity;

import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.AddFriendDialog;
import com.example.myapplication.FirstOpened;
import com.example.myapplication.model.Friend;
import com.example.myapplication.FriendViewAdaptor;
import com.example.myapplication.LocationService;
import com.example.myapplication.OrientationService;
import com.example.myapplication.R;
import com.example.myapplication.ServerAPI;
import com.example.myapplication.TimeThread;
import com.example.myapplication.UIRotator;
import com.example.myapplication.ZoomObserver;
import com.example.myapplication.model.FriendDao;
import com.example.myapplication.model.FriendDatabase;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements TimeThread.TimeThreadCallback{
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
    private Button save;
    private EditText inputURL;
    private String customizedURL;
    private static final String DEFAULT_SERVER_LINK = "https://socialcompass.goto.ucsd.edu/location/";
    private FriendDao dao;
    private FriendDatabase db;
    private float bearingAngle;
    private float azimuth = 0f;
    private TimeThread timeThread;
    private long timeDifference;
    private boolean GPSLoss = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timeThread = new TimeThread(this);
        timeThread.start();

        this.setUp();
        this.setRingUI();

        db = FriendDatabase.provide(this);
        dao = db.getDao();
        var items = dao.getAll();
        for(Friend l: items){
            friends.add(l);
        }
        //for testing
        for (int i = 0; i < friends.size(); ++i) {
            viewAdaptor.addNewView(friends.get(i));
        }

        executor = Executors.newScheduledThreadPool(1);

        this.setUpAddFriendButton();

        // Schedule the RequestThread task to run every 1 seconds
        this.scheduleRate(0,1);
    }

    private class RequestThread implements Runnable {
        @Override
        public void run() {
            if(open.isUidGenerated()) {
                if (GPSLoss == false) {
                    client = ServerAPI.provide(open.getName(), open.getUID(), open.getPrivateKey());
                    for(int i = 0; i < friends.size(); i++) {
                        client.updateLocation(friends.get(i), customizedURL);
                    }
                    client.uploadLocation(myLocation, customizedURL);
                }
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
        timeThread.stopThread();
    }

    private void setUp(){
        ui = new UIRotator(this);
        open = new FirstOpened(this, this);

        orientationService = OrientationService.singleton(this);
        locationService = LocationService.singleton(this);
        this.reobserveOrientation();
        this.reobserveLocation();

        save = findViewById(R.id.saveURL);
        inputURL = findViewById(R.id.URL);
        customizedURL = DEFAULT_SERVER_LINK;
        viewAdaptor = new FriendViewAdaptor(this, findViewById(R.id.constraintLayout));
        //Toast.makeText(this, open.getUID(), Toast.LENGTH_LONG).show();
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
                AddFriendDialog dialog = new AddFriendDialog(context, client);
                dialog.addNewFriendDialog(friends, viewAdaptor, db, dao);
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

    public void onSaveURLClicked(View v) {

        String url = inputURL.getText().toString();
        if(!url.isEmpty()) {
            customizedURL = url;
            Toast.makeText(this, "URL inputted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Please enter an URL", Toast.LENGTH_SHORT).show();
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
        timeThread.updateLastUpdateTime();
    }

    @VisibleForTesting
    public int getZoomLevel() {
        return zoom.getZoomLevel();
    }

    public void handleGPSLoss(boolean GPSLoss, long timeDifference) {
        this.GPSLoss = GPSLoss;
        this.timeDifference = timeDifference;
        ImageView greenDot = findViewById(R.id.greendot);
        ImageView redDot = findViewById(R.id.reddot);
        TextView connText = findViewById(R.id.conntext);
        // red dot + text if gps lost
        if (GPSLoss) {
            int hr = (int) (timeDifference / (1000*60*60));
            int min = (int) ((timeDifference / (1000*60)) % 60);
            String dcText;
            if (hr > 0) {
                dcText = hr + "h " + min + "m";
            } else{
                dcText = min + "m";
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    connText.setText(dcText);
                    connText.setVisibility(View.VISIBLE);
                    redDot.setVisibility(View.VISIBLE);
                    greenDot.setVisibility(View.INVISIBLE);
                }
            });
        // else green dot
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    connText.setVisibility(View.INVISIBLE);
                    redDot.setVisibility(View.INVISIBLE);
                    greenDot.setVisibility(View.VISIBLE);
                }
            });
        }
    }

}