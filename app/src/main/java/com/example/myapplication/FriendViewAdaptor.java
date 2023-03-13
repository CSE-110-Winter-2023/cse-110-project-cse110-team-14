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
    }

    public void changeAngle(int i, double angle) {
        TextView thisLabel = labelView.get(i);
        ImageView thisIcon = iconView.get(i);
        thisLabel.setText(friends.get(i).getLabel());
        ConstraintLayout.LayoutParams labelLayoutParams = (ConstraintLayout.LayoutParams) thisLabel.getLayoutParams();
        labelLayoutParams.circleAngle = (float)angle;
        //Toast.makeText(this, ""+lastLat+"   "+markerLat+"   "+bearingAngle, Toast.LENGTH_LONG).show();
        thisLabel.setLayoutParams(labelLayoutParams);

        ConstraintLayout.LayoutParams iconLayoutParams = (ConstraintLayout.LayoutParams) thisIcon.getLayoutParams();
        iconLayoutParams.circleAngle = (float)angle;
        thisIcon.setLayoutParams(iconLayoutParams);
    }

    public void changeDistance(int i, double distance, int zoomLevel) {
        TextView thisLabel = labelView.get(i);
        ImageView thisIcon = iconView.get(i);
        thisLabel.setText(friends.get(i).getLabel());
        boolean visible = CheckVisibility.checkDistance(zoomLevel, distance);
        if (!visible) {
            thisLabel.setVisibility(View.INVISIBLE);
            thisIcon.setVisibility(View.VISIBLE);
        }
        else {
            thisLabel.setVisibility(View.VISIBLE);
            thisIcon.setVisibility(View.INVISIBLE);

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
        if(view1.getVisibility() == View.VISIBLE){
            for(int i = viewNumber + 1; i < labelView.size(); i++) {
                var view2 = labelView.get(i);
                if(view2.getVisibility() == View.VISIBLE) {
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

                        /*
                        Log.d("View1 width", String.valueOf(view1.getMeasuredWidth()));
                        Log.d("View1 height",String.valueOf(view1.getMeasuredHeight()));
                        Log.d("View2 width", String.valueOf(view2.getMeasuredWidth()));
                        Log.d("View2 height",String.valueOf(view2.getMeasuredHeight()));

                        Log.d("View1 width", String.valueOf(firstPosition[0]));
                        Log.d("View1 height",String.valueOf(firstPosition[1]));
                        Log.d("View2 width", String.valueOf(secondPosition[0]));
                        Log.d("View2 height",String.valueOf(secondPosition[1])); */

                        if (tf) {
                            //offset
                            Log.d("overlap", "overlapped");
                        } else {
                            Log.d("NO overlap", "not overlapped");
                        }
                    }
                }
            }
        }
    }
    private void offSet(){

    }
}
