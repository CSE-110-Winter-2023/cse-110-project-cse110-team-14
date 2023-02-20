package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Matrix;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;

import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.MapsActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;


import com.example.myapplication.databinding.ActivityMapsBinding;

import java.security.cert.PKIXCertPathBuilderResult;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, SensorEventListener {

    private GoogleMap map;
    private ActivityMapsBinding binding;
    private PlacesClient placesClient;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private boolean locationPermissionGranted;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private Location lastKnownLocation;
    private static final int DEFAULT_ZOOM = 15;
    private static final String TAG = MapsActivity.class.getSimpleName();
    private final LatLng defaultLocation = new LatLng(-33.8523341, 151.2106085);
    private float[] mGravity = new float[3];
    private float[] mGeomagnetic = new float[3];
    private float azimuth = 0f;
    private float currentAzimuth = 0f;
    private SensorManager mSensorManager;

    private double lastLat;
    private double lastLong;
    private LatLng lastLocation = new LatLng(lastLat, lastLong);

    private EditText edit;
    private Button mockButton;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText longitude, latitude, name;
    private Button save, cancel;
    private ArrayList<Marker> markerList = new ArrayList<Marker>();
    private ArrayList<ImageView> markerView = new ArrayList<ImageView>();
    private LocationItemDao dao;
    private LocationDatabase db;
    private ConstraintLayout constraintLayout;
    private float bearingAngle;
    private int total_marker_existed = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

        Places.initialize(getApplicationContext(), getString(R.string.maps_api_key));
        placesClient = Places.createClient(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // UI Mock Elements
        edit = findViewById(R.id.mock_degrees);
        mockButton = findViewById(R.id.mock_button);
        constraintLayout = findViewById(R.id.constraint);
        Button addLocation = (Button) findViewById(R.id.addLocation);
        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewLocationDialog();
            }
        });

        db = LocationDatabase.getSingleton(this);
        dao = db.locationItemDao();

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
        /** Add a marker in Sydney and move the camera **/

        // curr pos (32.87, -117.22)
        List<LocationItem> locItems = dao.getAll();
        for(LocationItem l: locItems){
            LatLng newLatLng = new LatLng(l.longitude, l.latitude);
            Marker inputLocationMarker = map.addMarker(new MarkerOptions()
                    .position(newLatLng)
                    .title(l.label));
            markerList.add(inputLocationMarker);
            addNew();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause(){
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onDestroy(){
        db.makeDatabase(getApplicationContext());
        super.onDestroy();
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
            updateLocationUI();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        if (requestCode
                == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (map == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {
                                lastLat = lastKnownLocation.getLatitude();
                                lastLong = lastKnownLocation.getLongitude();
                                lastLocation = new LatLng(lastLat, lastLong);

                                CameraPosition cameraPosition = new CameraPosition.Builder()
                                        .target(lastLocation)
                                        .zoom(DEFAULT_ZOOM)
                                        .bearing(azimuth)
                                        .tilt(0)
                                        .build();
                                map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                            }
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            map.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            map.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        final float alpha = 0.97f;
        synchronized (this) {
            if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                mGravity[0] = alpha * mGravity[0] + (1 - alpha) * sensorEvent.values[0];
                mGravity[1] = alpha * mGravity[1] + (1 - alpha) * sensorEvent.values[1];
                mGravity[2] = alpha * mGravity[2] + (1 - alpha) * sensorEvent.values[2];
            }

            if(sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                mGeomagnetic[0] = alpha * mGeomagnetic[0] + (1 - alpha) * sensorEvent.values[0];
                mGeomagnetic[1] = alpha * mGeomagnetic[1] + (1 - alpha) * sensorEvent.values[1];
                mGeomagnetic[2] = alpha * mGeomagnetic[2] + (1 - alpha) * sensorEvent.values[2];
            }

            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
            if(success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);

                azimuth = setOrientation.setOrientation(orientation);
                getDeviceLocation();
            }
        }
        ImageView imageView = findViewById(R.id.compass);
        imageView.setRotation((float) -azimuth);

        if(markerList.size() > 0) {
            for(int i = 0; i < markerList.size(); i++) {
                Marker curMarker = markerList.get(i);
                double markerLat = curMarker.getPosition().latitude;
                double markerLong = curMarker.getPosition().longitude;
                float bearingAngle = (float)calculateBearingAngle(lastLat, lastLong, markerLat, markerLong);
                double markerDistance = calculateDistance(lastLat, lastLong, markerLat, markerLong);
                ImageView locationIconView = markerView.get(i);
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) locationIconView.getLayoutParams();
                layoutParams.circleAngle = bearingAngle-azimuth;
                //Toast.makeText(this, ""+lastLat+"   "+markerLat+"   "+bearingAngle, Toast.LENGTH_LONG).show();
                locationIconView.setLayoutParams(layoutParams);
                if(markerDistance < 33*DEFAULT_ZOOM) {
                    locationIconView.setVisibility(View.INVISIBLE);
                } else {
                    locationIconView.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}

    // below are two mock methods for UI testing
    public void onClickMock(View v) {
        String degree = edit.getText().toString();
        float deg = Float.valueOf(degree);
        mSensorManager.unregisterListener(this);

        if (lastKnownLocation != null) {
            lastLat = lastKnownLocation.getLatitude();
            lastLong = lastKnownLocation.getLongitude();
            lastLocation = new LatLng(lastLat, lastLong);

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(lastLocation)
                    .zoom(DEFAULT_ZOOM)
                    .bearing(deg)
                    .tilt(0)
                    .build();
            map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }

        ImageView imageView = findViewById(R.id.compass);
        imageView.setRotation(-deg);

        if(markerList.size() > 0) {
            for(int i = 0; i < markerList.size(); i++) {
                Marker curMarker = markerList.get(i);
                double markerLat = curMarker.getPosition().latitude;
                double markerLong = curMarker.getPosition().longitude;
                float bearingAngle = (float)calculateBearingAngle(lastLat, lastLong, markerLat, markerLong);
                double markerDistance = calculateDistance(lastLat, lastLong, markerLat, markerLong);
                ImageView locationIconView = markerView.get(i);
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) locationIconView.getLayoutParams();
                layoutParams.circleAngle = bearingAngle-deg;
                //Toast.makeText(this, ""+lastLat+"   "+markerLat+"   "+bearingAngle, Toast.LENGTH_LONG).show();
                locationIconView.setLayoutParams(layoutParams);
                if(markerDistance < 33*DEFAULT_ZOOM) {
                    locationIconView.setVisibility(View.INVISIBLE);
                } else {
                    locationIconView.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public void onClickCenter(View v) {
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);
    }

    public void onClickDBDelete(View v) {
        dao.nukeTable();
        Toast.makeText(this, "Deleted all Saved Locations", Toast.LENGTH_LONG).show();
    }

    public void createNewLocationDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View locationPopupView = getLayoutInflater().inflate(R.layout.popup, null);
        latitude = (EditText) locationPopupView.findViewById(R.id.latitude);
        longitude = (EditText) locationPopupView.findViewById(R.id.longitude);
        name = (EditText) locationPopupView.findViewById(R.id.name);

        save = (Button) locationPopupView.findViewById(R.id.save);
        cancel = (Button) locationPopupView.findViewById(R.id.cancel);

        dialogBuilder.setView(locationPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text_latitude = latitude.getText().toString();
                String text_longitude = longitude.getText().toString();
                String text_name = name.getText().toString();
                try {
                    double input_latitude = Double.parseDouble(text_latitude);
                    double input_longitude = Double.parseDouble(text_longitude);
                    dialog.dismiss();
                    //correct long/lat values
                    if(-90 <= input_latitude && input_latitude <= 90 && -180 <= input_longitude && input_longitude <= 180) {
                        Toast.makeText(locationPopupView.getContext(), "Saved!", Toast.LENGTH_LONG).show();
                        Marker inputLocationMarker = map.addMarker(new MarkerOptions()
                                .position(new LatLng(input_latitude, input_longitude))
                                .title(text_name));
                        markerList.add(inputLocationMarker);
                        addNew();
                        //insert new markers to database
                        LocationItem newLoc = new LocationItem(input_latitude, input_longitude, text_name);
                        dao.insert(newLoc);
                    } else {
                        Toast.makeText(locationPopupView.getContext(), "Please input a correct latitude and longitude", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(locationPopupView.getContext(), "Please input a number", Toast.LENGTH_LONG).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public static double calculateBearingAngle(double lat1, double long1, double lat2, double long2) {
        double dLong = Math.toRadians(long2 - long1);
        double lat1R = Math.toRadians(lat1);
        double lat2R = Math.toRadians(lat2);
        double y = Math.sin(dLong) * Math.cos(lat2R);
        double x = Math.cos(lat1R) * Math.sin(lat2R) - Math.sin(lat1R) * Math.cos(lat2R) * Math.cos(dLong);
        double radian = Math.atan2(y, x);
        double bearing = Math.toDegrees(radian);
        bearing = (bearing + 360) % 360;
        return bearing;
    }

    public static double calculateDistance(double lat1, double long1, double lat2, double long2) {
        double R = 6378137; // Earth’s mean radius in meter
        double dLat = Math.toRadians(lat2 - lat1);
        double dLong = Math.toRadians(long2 - long1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLong / 2) * Math.sin(dLong / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c;
        return d; // returns the distance in meter
    }

    public void addNew() {
        ImageView locationIconView = findViewById(R.id.locationIcon);
        ImageView view = new ImageView(this);
        view.setImageResource(R.drawable.baseline_location_on_24);
        ConstraintLayout.LayoutParams newLayoutParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        newLayoutParams.circleConstraint = R.id.compass;
        final float scale = getResources().getDisplayMetrics().density;
        int pixels = (int) (150 * scale + 0.5f);
        newLayoutParams.circleRadius = pixels;
        newLayoutParams.circleAngle = bearingAngle-azimuth;
        view.setLayoutParams(newLayoutParams);
        constraintLayout.addView(view);
        markerView.add(view);
    }
}