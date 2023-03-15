package com.example.myapplication;

import static android.content.Context.SENSOR_SERVICE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;

public class UIRotator implements SensorEventListener{
    private float[] mGravity = new float[3];
    private float[] mGeomagnetic = new float[3];
    private float azimuth = 0f;
    private float currentAzimuth = 0f;
    public SensorManager mSensorManager;
    private Activity mainAct;

    public UIRotator(Activity activity){
        mainAct = activity;
        mSensorManager = (SensorManager) mainAct.getSystemService(SENSOR_SERVICE);
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
                azimuth = Utilities.setOrientation(orientation);
            }
        }
        // rotation textviews here
        TextView c1 = mainAct.findViewById(R.id.circle1);
        TextView c2 = mainAct.findViewById(R.id.circle2);
        TextView c3 = mainAct.findViewById(R.id.circle3);
        TextView c4 = mainAct.findViewById(R.id.circle4);

        c1.setRotation((float) -azimuth);
        c2.setRotation((float) -azimuth);
        c3.setRotation((float) -azimuth);
        c4.setRotation((float) -azimuth);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}

    public void unRegisterSensor(){
        mSensorManager.unregisterListener(this);
    }

    public void registerSensor(){
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);
    }
}
