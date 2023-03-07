package com.example.myapplication;

public class DistanceToDp {
    private boolean first = false;
    private boolean second = false;
    private boolean third = false;
    private boolean forth = false;
    private double distance;
    private int zoomLevel;

    public DistanceToDp(double distance, int zoomLevel) {
        this.distance = distance;
        this.zoomLevel = zoomLevel;
        if(distance <= 1) {
            first = true;
        } else if(distance <= 10) {
            second = true;
        } else if(distance <= 500) {
            third = true;
        } else {
            forth = true;
        }
    }

    public int calculateDp() {
        int temp = 180;
        if(zoomLevel == 1) {
            temp = (int)(distance * 180);
        } else if (zoomLevel == 2) {
            if(first) {
                temp = (int) (distance * 90);
            } else {
                temp = (int) ((distance - 1) * 90 / 9 + 90);
            }
        } else if (zoomLevel == 3) {
            if(first) {
                temp = (int) (distance * 36);
            } else if(second) {
                temp = (int) ((distance - 1) * 72 / 9 + 36);
            } else {
                temp = (int) ((distance - 10) * 72 / 490 + 108);
            }
        } else if (zoomLevel == 4) {
            if(first) {
                temp = (int) (distance * 26);
            } else if(second) {
                temp = (int) ((distance - 1) * 52 / 9 + 26);
            } else if(third){
                temp = (int) ((distance - 10) * 52 / 490 + 78);
            } else if(forth){
                temp = (int) ((distance - 500) * 52 / 500 + 130);
            }
        }
        return temp;
    }
}
