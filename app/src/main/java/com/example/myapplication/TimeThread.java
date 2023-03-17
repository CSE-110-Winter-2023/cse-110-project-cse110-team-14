package com.example.myapplication;

import android.os.SystemClock;
import android.util.Log;

public class TimeThread extends Thread{
    private long lastUpdateTime;
    private boolean isRunning;
    private TimeThreadCallback mCallback;
    public TimeThread(TimeThreadCallback callback) {
        lastUpdateTime = SystemClock.elapsedRealtime() - 60000;
        isRunning = true;
        this.mCallback = callback;
    }

    public interface TimeThreadCallback {
        void handleGPSLoss(boolean GPSLoss, long timeDifference, boolean GPSConnectivity);
    }

    @Override
    public void run() {
        while (isRunning) {
            long currentTime = SystemClock.elapsedRealtime();
            long timeDifference = currentTime - lastUpdateTime;
            boolean GPSConnectivity = true;
            if(timeDifference >= 5000) {
                GPSConnectivity = false;
            }
            if (timeDifference >= 60000) { // 1 minute in milliseconds
                // Do something if time difference is greater than 1 minute
                // For example, send a notification or update the UI
                mCallback.handleGPSLoss(true, timeDifference, false);
            }
            else {
                mCallback.handleGPSLoss(false, timeDifference, GPSConnectivity);
            }
            // Wait for 1 second before checking again
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopThread() {
        isRunning = false;
    }

    public void updateLastUpdateTime() {
        lastUpdateTime = SystemClock.elapsedRealtime();
    }
}
