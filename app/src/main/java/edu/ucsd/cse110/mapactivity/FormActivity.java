package edu.ucsd.cse110.mapactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class FormActivity extends AppCompatActivity {

    private String field;
    private String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
    }
}