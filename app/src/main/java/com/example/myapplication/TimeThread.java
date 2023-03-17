package com.example.myapplication;
public class TimeThread extends Thread{
    private long lastUpdateTime;
    private boolean isRunning;
    private TimeThreadCallback mCallback;
    public TimeThread(TimeThreadCallback callback) {
        lastUpdateTime = System.currentTimeMillis();
        isRunning = true;
        this.mCallback =callback;
    }

    public interface TimeThreadCallback {
        void onGPSLoss(boolean GPSLoss, long timeDifference);
    }

    @Override
    public void run() {
        while (isRunning) {
            long currentTime = System.currentTimeMillis();
            long timeDifference = currentTime - lastUpdateTime;
            if (timeDifference >= 5000) { // 1 minute in milliseconds
                // Do something if time difference is greater than 1 minute
                // For example, send a notification or update the UI
                mCallback.onGPSLoss(true, timeDifference);
            }
            else {
                mCallback.onGPSLoss(false, timeDifference);
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
        lastUpdateTime = System.currentTimeMillis();
    }
}
