package com.example.myapplication;

import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

public class ZoomObserver {
    private TextView c1;
    private TextView c2;
    private TextView c3;
    private TextView c4;

    //zoom level 1 = most zoomed in
    //zoom level 4 = most zoomed out
    private int zoomLevel;
    private int defaultZoom = 2;


    public ZoomObserver(TextView c1, TextView c2, TextView c3, TextView c4){
        this.c1 = c1;
        this.c2 = c2;
        this.c3 = c3;
        this.c4 = c4;
        zoomLevel = defaultZoom;
        this.setRings();
        this.setText();
    }
    public void zoomIn(){
        if(zoomLevel != 1){
            this.setZoomLevel(this.zoomLevel-1);
            this.setRings();
            this.setText();
        }
        return;
    }

    public void zoomOut(){
        if(zoomLevel != 4){
            this.setZoomLevel(this.zoomLevel+1);
            this.setRings();
            this.setText();
        }
        return;
    }

    private void setRings(){
        if(this.zoomLevel == 1){
            this.setMargin(c4, 0, 0, 0, 0);
            this.setMargin(c3, 0, 0, 0, 0);
            this.setMargin(c2, 0, 0, 0, 0);
        } else if (this.zoomLevel == 2) {
            this.setMargin(c4, 0, 0, 0, 0);
            this.setMargin(c3, 0, 0, 0, 0);
            this.setMargin(c2, 250, 250, 250, 250);
        } else if (this.zoomLevel == 3) {
            this.setMargin(c4, 0, 0, 0, 0);
            this.setMargin(c3, 200, 200, 200, 200);
            this.setMargin(c2, 200, 200, 200, 200);
        } else {
            this.setMargin(c4, 150, 150, 150, 150);
            this.setMargin(c3, 150, 150, 150, 150);
            this.setMargin(c2, 150, 150, 150, 150);
        }
    }

    private void setText(){
        if(this.zoomLevel == 1){
            c1.setText("\n" + "        1mi");
            c2.setText("");
            c3.setText("");
            c4.setText("");
        } else if (this.zoomLevel == 2) {
            c1.setText("\n" + "        10mi");
            c2.setText("  1mi");
            c3.setText("");
            c4.setText("");
        } else if (this.zoomLevel == 3) {
            c1.setText("\n" + "        500mi");
            c2.setText("   10mi");
            c3.setText("");
            c4.setText("");
        } else {
            c1.setText("\n" + "     1000mi+");
            c2.setText("\n" + "  500mi");
            c3.setText("10mi");
            c4.setText("");
        }
    }

    private void setMargin(TextView view, int left, int top, int right, int bottom){
        ConstraintLayout.LayoutParams margin = (ConstraintLayout.LayoutParams) view.getLayoutParams();
        margin.setMargins(left, top, right, bottom);
        view.setLayoutParams(margin);
    }

    private void setZoomLevel(int level){
        this.zoomLevel = level;
    }

    public int getZoomLevel(){
        return this.zoomLevel;
    }
}
