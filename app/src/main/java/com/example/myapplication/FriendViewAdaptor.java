package com.example.myapplication;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.io.Serializable;
import java.util.ArrayList;

public class FriendViewAdaptor implements Serializable {
    private Context context;
    private ConstraintLayout constraintLayout;
    private ArrayList<Friend> friends = new ArrayList<>();
    private ArrayList<TextView> labelView = new ArrayList<>();
    private ArrayList<ImageView> iconView = new ArrayList<>();
    private ArrayList<TextView>  tempLabelView = new ArrayList<>();
    private ArrayList<Boolean> overlaps = new ArrayList<>();

    public FriendViewAdaptor(Context context, ConstraintLayout constraintLayout){
        this.context = context;
        this.constraintLayout = constraintLayout;
    }
    public void addNewView(Friend friend) {
        friends.add(friend);

        ConstraintLayout.LayoutParams newLayoutParams1 = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        newLayoutParams1.circleConstraint = R.id.circle1;
        newLayoutParams1.circleRadius = DpSpPxConversion.calculatePixels(180, context);
        newLayoutParams1.circleAngle = 0;

        ConstraintLayout.LayoutParams newLayoutParams2 = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        newLayoutParams2.circleConstraint = R.id.circle1;
        newLayoutParams2.circleRadius = DpSpPxConversion.calculatePixels(180, context);
        newLayoutParams2.circleAngle = 0;

        ConstraintLayout.LayoutParams newLayoutParams3 = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        newLayoutParams3.circleConstraint = R.id.circle1;
        newLayoutParams3.circleRadius = DpSpPxConversion.calculatePixels(180, context);
        newLayoutParams3.circleAngle = 0;

        TextView newText = new TextView(context);
        newText.setText(friend.getLabel());
        newText.setLayoutParams(newLayoutParams1);
        constraintLayout.addView(newText);
        labelView.add(newText);

        ImageView newImage = new ImageView(context);
        newImage.setImageResource(R.drawable.baseline_location_on_24);
        newImage.setLayoutParams(newLayoutParams2);
        constraintLayout.addView(newImage);
        iconView.add(newImage);

        TextView newTempText = new TextView(context);
        newTempText.setText(friend.getLabel());
        newTempText.setLayoutParams(newLayoutParams3);
        constraintLayout.addView(newTempText);
        tempLabelView.add(newTempText);

        overlaps.add(false);
    }

    public void changeAngle(int i, double angle) {
        TextView thisLabel = labelView.get(i);
        ImageView thisIcon = iconView.get(i);
        TextView thisTempLabel = tempLabelView.get(i);
        if(!overlaps.get(i)){
            thisLabel.setText(friends.get(i).getLabel());
        }
        ConstraintLayout.LayoutParams labelLayoutParams = (ConstraintLayout.LayoutParams) thisLabel.getLayoutParams();
        labelLayoutParams.circleAngle = (float)angle;
        //Toast.makeText(this, ""+lastLat+"   "+markerLat+"   "+bearingAngle, Toast.LENGTH_LONG).show();
        thisLabel.setLayoutParams(labelLayoutParams);

        ConstraintLayout.LayoutParams tempLayoutParams = (ConstraintLayout.LayoutParams) thisTempLabel.getLayoutParams();
        tempLayoutParams.circleAngle = (float)angle;
        thisTempLabel.setLayoutParams(tempLayoutParams);

        ConstraintLayout.LayoutParams iconLayoutParams = (ConstraintLayout.LayoutParams) thisIcon.getLayoutParams();
        iconLayoutParams.circleAngle = (float)angle;
        thisIcon.setLayoutParams(iconLayoutParams);
    }

    public void changeDistance(int i, double distance, int zoomLevel) {
        TextView thisLabel = labelView.get(i);
        ImageView thisIcon = iconView.get(i);
        TextView thisTempLabel = tempLabelView.get(i);
        if(!overlaps.get(i)){
            thisLabel.setText(friends.get(i).getLabel());
        }
        boolean visible = CheckVisibility.checkDistance(zoomLevel, distance);
        if (!visible) {
            thisLabel.setVisibility(View.INVISIBLE);
            thisIcon.setVisibility(View.VISIBLE);
            thisTempLabel.setVisibility(View.INVISIBLE);
        }
        else {
            thisIcon.setVisibility(View.INVISIBLE);
            if(!overlaps.get(i)) {
                thisLabel.setVisibility(View.VISIBLE);
                thisTempLabel.setVisibility(View.INVISIBLE);
            } else {
                thisLabel.setVisibility(View.INVISIBLE);
                thisTempLabel.setVisibility(View.VISIBLE);
            }

            DistanceToDp temp = new DistanceToDp(distance, zoomLevel);
            int dp = temp.calculateDp();
            ConstraintLayout.LayoutParams labelLayoutParams = (ConstraintLayout.LayoutParams) thisLabel.getLayoutParams();
            labelLayoutParams.circleRadius = DpSpPxConversion.calculatePixels(dp, context);
            //Toast.makeText(this, ""+lastLat+"   "+markerLat+"   "+bearingAngle, Toast.LENGTH_LONG).show();
            thisLabel.setLayoutParams(labelLayoutParams);
            checkOverlap(i);
        }
    }

    public TextView getLabel(int i) {
        return labelView.get(i);
    }

    public ImageView getIcon(int i) {
        return iconView.get(i);
    }

    public void delete(int i) {
        //More about removing views needed
        labelView.remove(i);
        iconView.remove(i);
    }
    private void checkOverlap(int viewNumber){
        var view1 = labelView.get(viewNumber);
        if(view1.getVisibility() == View.VISIBLE || tempLabelView.get(viewNumber).getVisibility() == View.VISIBLE){
            boolean overallOverlap = false;
            for(int i = viewNumber + 1; i < labelView.size(); i++) {
                var view2 = labelView.get(i);
                if(view2.getVisibility() == View.VISIBLE || tempLabelView.get(i).getVisibility() == View.VISIBLE) {
                    if (viewNumber != i) {
                        int[] firstPosition = new int[2];
                        int[] secondPosition = new int[2];

                        view1.getLocationOnScreen(firstPosition);
                        view2.getLocationOnScreen(secondPosition);

                        Rect rectFirstView = new Rect(firstPosition[0], firstPosition[1],
                                firstPosition[0] + view1.getMeasuredWidth(), firstPosition[1] + view1.getMeasuredHeight());
                        Rect rectSecondView = new Rect(secondPosition[0], secondPosition[1],
                                secondPosition[0] + view2.getMeasuredWidth(), secondPosition[1] + view2.getMeasuredHeight());
                        boolean tf = rectFirstView.intersect(rectSecondView);
                        if (tf) {
                            //offset
                            //Log.d("overlap", "overlapped--"+viewNumber);
                            Log.d("overlap", "overlapped--"+i);
                            overlaps.set(viewNumber, true);
                            overlaps.set(i, true);
                            overallOverlap = true;
                            solveOverlap(viewNumber, i);
                        } else {
                            //Log.d("NO overlap", "not overlapped--"+i);
                            overlaps.set(i, false);
                        }
                    }
                }
            }
            if(!overallOverlap) {
                overlaps.set(viewNumber, false);
                //Log.d("NO overlap", "not overlapped--"+viewNumber);
            }
        }
    }

    private void solveOverlap(int view1, int view2){
        double d1 = friends.get(view1).getDistance();
        double d2 = friends.get(view2).getDistance();

        if(checkLevel(d1) == checkLevel(d2)) {
            offSet(view1, view2);
        } else {
            truncate();
        }
    }

    private int checkLevel(double distance){
        if(distance <= 1){
            return 1;
        } else if(distance <= 10){
            return 2;
        } else if(distance <= 500){
            return 3;
        } else {
            return 4;
        }
    }
    private void offSet(int view1, int view2){

        TextView textView1 = labelView.get(view1);
        TextView textView2 = labelView.get(view2);
        TextView tempView1 = tempLabelView.get(view1);
        TextView tempView2 = tempLabelView.get(view2);

        /*
        tempView1.setText(textView1.getText());
        tempView2.setText(textView2.getText());
        */

        ConstraintLayout.LayoutParams layoutParams1 = (ConstraintLayout.LayoutParams) textView1.getLayoutParams();
        int distance1 = layoutParams1.circleRadius;
        //double angle1 = layoutParams1.circleAngle;
        ConstraintLayout.LayoutParams layoutParams2 = (ConstraintLayout.LayoutParams) textView2.getLayoutParams();
        int distance2 = layoutParams2.circleRadius;
        //double angle2 = layoutParams2.circleAngle;

        if(distance1 <= distance2) {

            if(distance1 > DpSpPxConversion.calculatePixels(25, context)) {

                ConstraintLayout.LayoutParams tempParams1 = (ConstraintLayout.LayoutParams) tempView1.getLayoutParams();
                tempParams1.circleRadius = distance1 - DpSpPxConversion.calculatePixels(25, context);
                tempView1.setLayoutParams(tempParams1);
                ConstraintLayout.LayoutParams tempParams2 = (ConstraintLayout.LayoutParams) tempView2.getLayoutParams();
                tempParams2.circleRadius = distance2;
                tempView2.setLayoutParams(tempParams2);

            } else {

                ConstraintLayout.LayoutParams tempParams1 = (ConstraintLayout.LayoutParams) tempView1.getLayoutParams();
                tempParams1.circleRadius = distance1;
                tempView1.setLayoutParams(tempParams1);
                ConstraintLayout.LayoutParams tempParams2 = (ConstraintLayout.LayoutParams) tempView2.getLayoutParams();
                tempParams2.circleRadius = distance2 + DpSpPxConversion.calculatePixels(25, context);
                tempView2.setLayoutParams(tempParams2);

            }

        } else {

            if(distance2 > DpSpPxConversion.calculatePixels(25, context)) {

                ConstraintLayout.LayoutParams tempParams1 = (ConstraintLayout.LayoutParams) tempView1.getLayoutParams();
                tempParams1.circleRadius = distance1;
                tempView1.setLayoutParams(tempParams1);
                ConstraintLayout.LayoutParams tempParams2 = (ConstraintLayout.LayoutParams) tempView2.getLayoutParams();
                tempParams2.circleRadius = distance2 - DpSpPxConversion.calculatePixels(25, context);
                tempView2.setLayoutParams(tempParams2);

            } else {

                ConstraintLayout.LayoutParams tempParams1 = (ConstraintLayout.LayoutParams) tempView1.getLayoutParams();
                tempParams1.circleRadius = distance1 + DpSpPxConversion.calculatePixels(25, context);
                tempView1.setLayoutParams(tempParams1);
                ConstraintLayout.LayoutParams tempParams2 = (ConstraintLayout.LayoutParams) tempView2.getLayoutParams();
                tempParams2.circleRadius = distance2;
                tempView2.setLayoutParams(tempParams2);

            }

        }
    }
    private void truncate(){

    }
}
