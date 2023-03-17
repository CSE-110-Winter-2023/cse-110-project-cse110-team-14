package com.example.myapplication;

import static java.lang.Math.abs;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.myapplication.model.Friend;

import java.io.Serializable;
import java.util.ArrayList;

public class FriendViewAdaptor implements Serializable {
    private Context context;
    private ConstraintLayout constraintLayout;
    private ViewsFactory viewsFactory;
    private ArrayList<Friend> friends = new ArrayList<>();
    private ArrayList<TextView> labelView = new ArrayList<>();
    private ArrayList<ImageView> iconView = new ArrayList<>();
    private ArrayList<TextView>  tempLabelView = new ArrayList<>();
    private ArrayList<Boolean> overlaps = new ArrayList<>();

    public FriendViewAdaptor(Context context, ConstraintLayout constraintLayout){
        this.context = context;
        this.constraintLayout = constraintLayout;
        viewsFactory = new ViewsFactory(context);
    }
    public void addNewView(Friend friend) {
        friends.add(friend);

        TextView newText = viewsFactory.buildTextView(friend);
        constraintLayout.addView(newText);
        labelView.add(newText);

        ImageView newImage = viewsFactory.buildNewImage(R.drawable.baseline_location_on_24);
        constraintLayout.addView(newImage);
        iconView.add(newImage);

        TextView newTempText = viewsFactory.buildTextView(friend);
        constraintLayout.addView(newTempText);
        tempLabelView.add(newTempText);

        overlaps.add(false);
    }

    public void changeAngle(int i, double angle) {
        TextView thisLabel = labelView.get(i);
        ImageView thisIcon = iconView.get(i);
        TextView thisTempLabel = tempLabelView.get(i);
        if(!overlaps.get(i)) {
            thisLabel.setText(friends.get(i).getLabel());
        }

        viewsFactory.changeAngle(thisLabel, angle);
        viewsFactory.changeAngle(thisIcon, angle);
        viewsFactory.changeAngle(thisTempLabel, angle);

    }

    public void changeDistance(int i, double distance, int zoomLevel) {
        TextView thisLabel = labelView.get(i);
        ImageView thisIcon = iconView.get(i);
        TextView thisTempLabel = tempLabelView.get(i);
        if(!overlaps.get(i)){
            thisLabel.setText(friends.get(i).getLabel());
        }
        boolean visible = Utilities.checkDistance(zoomLevel, distance);
        if (!visible) {
            thisLabel.setVisibility(View.INVISIBLE);
            thisIcon.setVisibility(View.VISIBLE);
            thisTempLabel.setVisibility(View.INVISIBLE);
        }
        else {
            thisLabel.setVisibility(View.VISIBLE);
            thisIcon.setVisibility(View.INVISIBLE);
            if(!overlaps.get(i)) {
                thisLabel.setVisibility(View.VISIBLE);
                thisTempLabel.setVisibility(View.INVISIBLE);
            } else {
                thisLabel.setVisibility(View.INVISIBLE);
                thisTempLabel.setVisibility(View.VISIBLE);
            }

            DistanceToDp temp = new DistanceToDp(distance, zoomLevel);
            int pixels = Utilities.calculatePixels(temp.calculateDp(), context);
            viewsFactory.changeDistance(thisLabel, pixels);
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

            for(int i = 0; i < labelView.size(); i++) {
                var view2 = labelView.get(i);
                if(view2.getVisibility() == View.VISIBLE || tempLabelView.get(i).getVisibility() == View.VISIBLE) {
                    if (viewNumber != i) {
                        int[] firstPosition = new int[2];
                        int[] secondPosition = new int[2];

                        view1.getLocationOnScreen(firstPosition);
                        view2.getLocationOnScreen(secondPosition);

                        if (viewsFactory.isOverLapping(view1, view2)) {
                            //Log.d("overlap", "overlapped--"+viewNumber);
                            //Log.d("overlap", "overlapped--"+i);
                            overlaps.set(viewNumber, true);
                            overlaps.set(i, true);
                            overallOverlap = true;

                            int absWidthDiff = viewsFactory.getWidthOverLap(view1, view2);
                            int absHeightDiff = viewsFactory.getHeightOverlap(view1, view2);

                            solveOverlap(viewNumber, i, absWidthDiff, absHeightDiff);
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

    private void solveOverlap(int view1, int view2, int absWidthDiff, int absHeightDiff){
        double d1 = friends.get(view1).getDistance();
        double d2 = friends.get(view2).getDistance();

        if(checkLevel(d1) == checkLevel(d2)) {
            offSet(view1, view2, absWidthDiff, absHeightDiff);
        }  else {
            truncate(view1, view2, absWidthDiff);
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
    private void offSet(int view1, int view2, int absWidthDiff, int absHeightDiff){

        TextView textView1 = labelView.get(view1);
        TextView textView2 = labelView.get(view2);
        TextView tempView1 = tempLabelView.get(view1);
        TextView tempView2 = tempLabelView.get(view2);

        tempView1.setText(textView1.getText());
        tempView2.setText(textView2.getText());

        int distance1 = viewsFactory.getDistance(textView1);
        double angle1 = viewsFactory.getAngle(textView1);
        int distance2 = viewsFactory.getDistance(textView2);
        double angle2 = viewsFactory.getAngle(textView2);


        if(distance1 <= distance2) {

            int absDiff = Utilities.calculateOffset(absWidthDiff, absHeightDiff, Math.toRadians(angle1 - 90));

            if(distance1 > absDiff) {

                viewsFactory.changeDistance(tempView1, distance1 - absDiff);
                viewsFactory.changeDistance(tempView2, distance2);

            } else {

                viewsFactory.changeDistance(tempView1, distance1);
                viewsFactory.changeDistance(tempView2, distance2 + absDiff);

            }

        } else {

            int absDiff = Utilities.calculateOffset(absWidthDiff, absHeightDiff, Math.toRadians(angle2 - 90));

            if(distance2 > absDiff) {

                viewsFactory.changeDistance(tempView1, distance1);
                viewsFactory.changeDistance(tempView2, distance2 - absDiff);

            } else {

                viewsFactory.changeDistance(tempView1, distance1 + absDiff);
                viewsFactory.changeDistance(tempView2, distance2);

            }
        }
    }
    private void truncate(int view1, int view2, int diff){

        TextView textView1 = labelView.get(view1);
        TextView textView2 = labelView.get(view2);
        TextView tempView1 = tempLabelView.get(view1);
        TextView tempView2 = tempLabelView.get(view2);

        int num = diff / 20 + 1;
        int size1 = textView1.length();
        int size2 = textView2.length();

        String text1;
        if(num >= size1){
            text1 = textView1.getText().toString().substring(0, 1);
        }
        else{
            text1 = textView1.getText().toString().substring(0, size1 - num);
        }

        String text2;
        if(num >= size2){
            text2 = textView2.getText().toString().substring(0, 1);
        }
        else {
            text2 = textView2.getText().toString().substring(0, size2 - num);
        }

        //Log.d("truncate", "num1: "+view1+", num2: "+view2);
        tempView1.setText(text1);
        tempView2.setText(text2);

        int distance1 = viewsFactory.getDistance(textView1);
        int distance2 = viewsFactory.getDistance(textView2);
        viewsFactory.changeDistance(tempView1, distance1);
        viewsFactory.changeDistance(tempView2, distance2);
    }

    public ArrayList<Friend> getFriends() { return friends; }

    public ArrayList<TextView> getLabelView() {return labelView;}

    public ArrayList<ImageView> getIconView() {return iconView;}

    public ArrayList<TextView> getTempLabelView() {return tempLabelView;}

    public ArrayList<Boolean> getOverlaps() {return overlaps;}
}
