package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.myapplication.activity.MainActivity;
import com.example.myapplication.model.LocationItem;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class AddFriendDialog {
    private Context context;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private View locationPopupView;
    private EditText mEditUID;
    private Button mAddButton;
    private Button mCancelButton;

    public AddFriendDialog(Context context){
        this.context = context;
    }

    public void addNewFriendDialog(ArrayList<Friend> friends, FriendViewAdaptor viewAdaptor) {
        dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        locationPopupView = inflater.inflate(R.layout.activity_add_friend, null);
        mEditUID = (EditText) locationPopupView.findViewById(R.id.edit_uid);
        mAddButton = (Button) locationPopupView.findViewById(R.id.add_button);
        mCancelButton = (Button) locationPopupView.findViewById(R.id.cancel_button);
        dialogBuilder.setView(locationPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mEditUID.getText().toString();
                if (!name.isEmpty()) {
                    Toast.makeText(context, "UID added", Toast.LENGTH_SHORT).show();

                    Friend newFriend = new Friend(name, "null", 0, 0, 1);
                    friends.add(newFriend);
                    viewAdaptor.addNewView(newFriend);
                    dialog.dismiss();
                } else {
                    Toast.makeText(context, "Please enter a UID", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
