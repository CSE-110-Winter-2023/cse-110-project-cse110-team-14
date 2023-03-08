package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Friend;
import com.example.myapplication.FriendViewAdaptor;
import com.example.myapplication.R;

import java.util.ArrayList;

public class AddFriendActivity extends AppCompatActivity {
    private EditText mEditUID;
    private Button mAddButton;
    private Button mCancelButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        mEditUID = findViewById(R.id.edit_uid);
        mAddButton = findViewById(R.id.add_button);
        mCancelButton = findViewById(R.id.cancel_button);

        Intent intent = getIntent();

        mAddButton.setOnClickListener(v -> {
            String name = mEditUID.getText().toString();
            if (!name.isEmpty()) {
                Toast.makeText(getApplicationContext(), "UID added", Toast.LENGTH_SHORT).show();
                Bundle args = intent.getBundleExtra("BUNDLE");
                ArrayList<Friend> friends = (ArrayList<Friend>) args.getSerializable("friend");
                FriendViewAdaptor viewAdaptor = (FriendViewAdaptor) args.getSerializable("view");
                Friend newFriend = new Friend(name, "null", 0, 0, 1);
                friends.add(newFriend);
                viewAdaptor.addNewView(newFriend);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Please enter a UID", Toast.LENGTH_SHORT).show();
            }
        });

        mCancelButton.setOnClickListener(v -> finish());
    }
}
