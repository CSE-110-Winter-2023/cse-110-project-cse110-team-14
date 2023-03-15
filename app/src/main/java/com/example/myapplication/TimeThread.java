package com.example.myapplication;

import android.os.Bundle;
import android.os.Message;

public class TimeThread extends Thread {
    private long lastUpdateTime;
    private boolean isRunning;
    private boolean runThread;
    private TimeThreadListener listener;
    public interface TimeThreadListener {
        void onVariableUpdate(int timeDifference, boolean runThread);
    }
    public TimeThread() {
        lastUpdateTime = System.currentTimeMillis();
        isRunning = true;
        // Create a new message
    }

    @Override
    public void run() {
        while (isRunning) {
            long currentTime = System.currentTimeMillis();
            long timeDifference = currentTime - lastUpdateTime;

            if (timeDifference >= 10000) { // 1 minute in milliseconds
                // Do something if time difference is greater than 1 minute
                // For example, send a notification or update the UI
                System.out.println("GPS lost");
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
