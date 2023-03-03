package com.example;

import static org.junit.Assert.assertEquals;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RunWith(AndroidJUnit4.class)
public class IconOrientationTest extends AppCompatActivity implements OnMapReadyCallback {

    private Marker marker;
    private SupportMapFragment map;
    private final LatLng sydney = new LatLng(-33.87,151);

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            marker = map.addMarker(new MarkerOptions()
                    .position(sydney)
                    .anchor(0.5f,0.5f)
                    .rotation(90.0f));
        });
    }

    @Test
    public void test1() {
        assertEquals(1,1);
    }
}
