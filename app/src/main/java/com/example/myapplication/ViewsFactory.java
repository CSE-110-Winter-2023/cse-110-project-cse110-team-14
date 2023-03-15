package com.example.myapplication;

import static java.lang.Math.abs;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.myapplication.model.Friend;

public class ViewsFactory {

    Context context;

    public ViewsFactory(Context context) {
        this.context = context;
    }

    public TextView buildTextView(Friend friend) {

        ConstraintLayout.LayoutParams newLayoutParams1 = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        newLayoutParams1.circleConstraint = R.id.circle1;
        newLayoutParams1.circleRadius = Utilities.calculatePixels(180, context);
        newLayoutParams1.circleAngle = 0;

        TextView newText = new TextView(context);
        newText.setText(friend.getLabel());
        newText.setLayoutParams(newLayoutParams1);
        return newText;

    }

    public ImageView buildNewImage(int resID) {

        ConstraintLayout.LayoutParams newLayoutParams2 = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        newLayoutParams2.circleConstraint = R.id.circle1;
        newLayoutParams2.circleRadius = Utilities.calculatePixels(180, context);
        newLayoutParams2.circleAngle = 0;

        ImageView newImage = new ImageView(context);
        newImage.setImageResource(resID);
        newImage.setLayoutParams(newLayoutParams2);
        return newImage;

    }

    public void changeAngle(View view, double angle) {

        ConstraintLayout.LayoutParams labelLayoutParams = (ConstraintLayout.LayoutParams) view.getLayoutParams();
        labelLayoutParams.circleAngle = (float)angle;
        view.setLayoutParams(labelLayoutParams);

    }

    public float getAngle(View view) {

        ConstraintLayout.LayoutParams layoutParams1 = (ConstraintLayout.LayoutParams) view.getLayoutParams();
        return layoutParams1.circleAngle;

    }

    public void changeDistance(View view, int pixels) {

        ConstraintLayout.LayoutParams labelLayoutParams = (ConstraintLayout.LayoutParams) view.getLayoutParams();
        labelLayoutParams.circleRadius = pixels;
        view.setLayoutParams(labelLayoutParams);

    }

    public int getDistance(View view) {

        ConstraintLayout.LayoutParams layoutParams1 = (ConstraintLayout.LayoutParams) view.getLayoutParams();
        return layoutParams1.circleRadius;

    }

    public boolean isOverLapping(View view1, View view2){

        int[] firstPosition = new int[2];
        int[] secondPosition = new int[2];

        view1.getLocationOnScreen(firstPosition);
        view2.getLocationOnScreen(secondPosition);

        Rect rectFirstView = new Rect(firstPosition[0], firstPosition[1],
                firstPosition[0] + view1.getMeasuredWidth(), firstPosition[1] + view1.getMeasuredHeight());
        Rect rectSecondView = new Rect(secondPosition[0], secondPosition[1],
                secondPosition[0] + view2.getMeasuredWidth(), secondPosition[1] + view2.getMeasuredHeight());

        return rectFirstView.intersect(rectSecondView);
    }

    public int getWidthOverLap(View view1, View view2) {

        int[] firstPosition = new int[2];
        int[] secondPosition = new int[2];

        view1.getLocationOnScreen(firstPosition);
        view2.getLocationOnScreen(secondPosition);

        int absWidthDiff;

        if(abs(firstPosition[0]) <= abs(secondPosition[0])) {
            absWidthDiff = abs(firstPosition[0]) + view1.getMeasuredWidth() - abs(secondPosition[0]);
        } else {
            absWidthDiff = abs(secondPosition[0]) + view2.getMeasuredWidth() - abs(firstPosition[0]);
        }

        return absWidthDiff;
    }

    public int getHeightOverlap(View view1, View view2) {

        int[] firstPosition = new int[2];
        int[] secondPosition = new int[2];

        view1.getLocationOnScreen(firstPosition);
        view2.getLocationOnScreen(secondPosition);

        int absHeightDiff;

        if(abs(firstPosition[1]) <= abs(secondPosition[1])) {
            absHeightDiff = abs(firstPosition[1]) + view1.getMeasuredHeight() - abs(secondPosition[1]);
        } else {
            absHeightDiff = abs(secondPosition[1]) + view2.getMeasuredHeight() - abs(firstPosition[1]);
        }

        return absHeightDiff;
    }


}
