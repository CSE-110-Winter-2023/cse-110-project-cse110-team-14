package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.VisibleForTesting;

import java.util.UUID;

public class FirstOpened {
    final String START = "Start";
    final String NAME_KEY = "name";
    final String UID_KEY = "uid";
    final String PRIVATE_KEY = "private";
    final String UID_GENERATED_Key = "uidGenerated";
    private Activity mainAct;
    private Context mainCon;
    private SharedPreferences checkStart;
    private AlertDialog.Builder dialogBuilder;

    private SharedPreferences.Editor editor;
    private AlertDialog dialog;
    private EditText name;
    private Button save;
    private String inputName;
    private String UID;
    private String privateKey;

    public FirstOpened(Activity act, Context context){
        mainAct = act;
        mainCon = context;
        checkStart = mainAct.getSharedPreferences(START, 0);
        editor = checkStart.edit();
        checkFirst();
    }

    public String getName(){
        return checkStart.getString(NAME_KEY, "");
    }

    public String getUID(){
        return checkStart.getString(UID_KEY, "");
    }
    public String getPrivateKey(){
        return checkStart.getString(PRIVATE_KEY, "");
    }
    public boolean isUidGenerated() { return checkStart.getBoolean(UID_GENERATED_Key, false); }

    @VisibleForTesting
    public void setName(String name){
        editor.putString(NAME_KEY, name);
    }
    @VisibleForTesting
    public void setUID(){
        editor.putString(UID_KEY, createUID());
    }

    private void checkFirst(){
        if(checkStart.getBoolean("first", true)) {
            Log.d("open new","1st");
            Dialog();
        }
        else{
            TextView uid = mainAct.findViewById(R.id.uid);
            uid.setText("UID: " + getUID());
        }
        //uncomment this to test for new opening
        //checkStart.edit().putBoolean("first", true).commit();
    }

    private void Dialog(){
        dialogBuilder = new AlertDialog.Builder(mainCon);
        final View namePopUp = mainAct.getLayoutInflater().inflate(R.layout.namepopup, null);
        name = (EditText) namePopUp.findViewById(R.id.name);
        save = (Button) namePopUp.findViewById(R.id.save);

        dialogBuilder.setView(namePopUp);
        dialog = dialogBuilder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputName = name.getText().toString();
                if(checkName(inputName)){
                    checkStart.edit().putBoolean("first", false).commit();
                    editor.putString(NAME_KEY, inputName);

                    UID = createUID();
                    editor.putString(UID_KEY, UID);
                    privateKey = createUID();
                    editor.putString(PRIVATE_KEY, privateKey);
                    editor.putBoolean(UID_GENERATED_Key, true);
                    editor.apply();

                    dialog.dismiss();
                    Toast.makeText(namePopUp.getContext(), "Saved!", Toast.LENGTH_LONG).show();

                    TextView uid = mainAct.findViewById(R.id.uid);
                    uid.setText("UID: " + getUID());
                } else {
                    Toast.makeText(namePopUp.getContext(), "Please input a correct name", Toast.LENGTH_LONG).show();
                    dialog.show();
                }
            }
        });
    }

    public static boolean checkName(String name){
        //return false if name contains something else than
        //alphabets or whitespaces
        for (int i = 0; i < name.length(); i++) {
            char ch = name.charAt(i);
            if (!Character.isLetter(ch) && !Character.isWhitespace(ch)) {
                return false;
            }
        }
        return true;
    }

    private String createUID(){
        return UUID.randomUUID().toString();
    }
}
