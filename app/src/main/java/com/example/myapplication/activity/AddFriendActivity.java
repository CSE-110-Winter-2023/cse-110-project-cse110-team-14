package com.example.myapplication.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Friend;
import com.example.myapplication.R;
import com.example.myapplication.ServerAPI;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

        ServerAPI api = new ServerAPI();
        ExecutorService executor = Executors.newSingleThreadExecutor();



        mAddButton.setOnClickListener(v -> {
            String name = mEditUID.getText().toString();
            executor.submit(() -> {
                try {
                    Friend friend = api.get(name);
                    System.out.println("FriendActivity: " + friend.public_code + " version: " + friend.version);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            if (!name.isEmpty()) {
                Toast.makeText(getApplicationContext(), "UID added", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Please enter a UID", Toast.LENGTH_SHORT).show();
            }

        });

        mCancelButton.setOnClickListener(v -> finish());
    }
}
