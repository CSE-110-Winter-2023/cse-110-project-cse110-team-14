package com.example.myapplication;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

public class FriendViewAdaptor {
    private Context context;
    private ConstraintLayout constraintLayout;
    private ArrayList<TextView> labelView = new ArrayList<>();
    private ArrayList<ImageView> iconView = new ArrayList<>();

    public FriendViewAdaptor(Context context, ConstraintLayout constraintLayout){
        this.context = context;
        this.constraintLayout = constraintLayout;
    }
    public void addNewView(Friend friend) {

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
            labelLayoutParams.circleRadius = DpSpPxConversion.calculatePixels(dp, context);;
            //Toast.makeText(this, ""+lastLat+"   "+markerLat+"   "+bearingAngle, Toast.LENGTH_LONG).show();
            thisLabel.setLayoutParams(labelLayoutParams);
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

}
