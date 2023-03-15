package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.myapplication.model.Friend;
import com.example.myapplication.model.FriendDao;
import com.example.myapplication.model.FriendDatabase;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AddFriendDialog {
    private Context context;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private View locationPopupView;
    private EditText mEditUID;
    private Button mAddButton;
    private Button mCancelButton;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private ServerAPI api;

    public AddFriendDialog(Context context, ServerAPI api){
        this.context = context;
        this.api = api;
    }

    public void addNewFriendDialog(ArrayList<Friend> friends, FriendViewAdaptor viewAdaptor, FriendDatabase db, FriendDao dao) {
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

                // Check if friend is already in the database
                Friend f1 = dao.get(name);
                if (f1 != null) {
                    Toast.makeText(context, "Friend already exists", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Input validation from the server
                Future<Boolean> future = executor.submit(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        try {
                            Friend check = api.get(name);
                            if (check == null) {
                                return false;
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        return true;
                    }
                });
                if (!name.isEmpty()) {
                    Friend newFriend = new Friend(name, "null", 0, 0, 1);
                    try {
                        boolean result = future.get();
                        if (result == true) {
                            dao.upsert(newFriend);
                            friends.add(newFriend);
                            viewAdaptor.addNewView(newFriend);
                            Toast.makeText(context, "UID added", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(context, "Invalid UID", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
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
