package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class OrientationService implements SensorEventListener {

    private static OrientationService instance;
    private final SensorManager sensorManager;
    private float[] accelerometerReading;
    private float[] magnetometerReading;
    private MutableLiveData<Float> azimuth;
    private boolean isOn = false;

    private OrientationService(Activity activity) {
        this.azimuth = new MutableLiveData<>();
        this.sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        this.registerSensorListeners();
    }

    private void registerSensorListeners() {
        if(!isOn) {
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_NORMAL);
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public static OrientationService singleton(Activity activity) {
        if (instance == null) {
            instance = new OrientationService(activity);
        }
        return instance;
    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelerometerReading = sensorEvent.values;
        }
        if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            magnetometerReading = sensorEvent.values;
        }
        if (accelerometerReading != null && magnetometerReading != null) {
            onBothSensorDataAvailable();
        }
    }

    private void onBothSensorDataAvailable() {
        // Discount contract checking. Think Design by Contract!
        if (accelerometerReading == null || magnetometerReading == null) {
            throw new IllegalStateException("Both sensors must be available to compute orientation.");
        }

        float[] r = new float[9];
        float[] i = new float[9];
        // Now we do some linear algebra magic using the two sensor readings.
        boolean success = SensorManager.getRotationMatrix(r, i, accelerometerReading, magnetometerReading);
        // Did it work?
        if (success) {
            // Ok we're good to go!
            float[] orientation = new float[3];
            SensorManager.getOrientation(r, orientation);


            this.azimuth.postValue(setOrientation.setOrientation(orientation));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void unregisterSensorListeners() {
        if(isOn) {
            sensorManager.unregisterListener(this);
        }
    }

    public LiveData<Float> getOrientation() {
        return this.azimuth;
    }
}
