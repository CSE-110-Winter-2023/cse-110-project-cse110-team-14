package com.example.myapplication.model;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

@Entity(tableName = "location_items")
public class LocationItem {
    @PrimaryKey(autoGenerate = true)
    public long id = 0;
    public double longitude;
    public double latitude;
    @NonNull
    public String label;


    public LocationItem(double longitude, double latitude, @NonNull String label){
        this.longitude = longitude;
        this.latitude = latitude;
        this.label = label;
    }

    public static List<LocationItem> loadJSON(Context context, String path){
        try{
            InputStream input = context.getAssets().open(path);
            Reader reader = new InputStreamReader(input);
            Gson gson = new Gson();
            Type type = new TypeToken<List<LocationItem>>(){}.getType();
            return gson.fromJson(reader,type);
        } catch (IOException e){
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public String toString() {
        return "TodoListItem{" +
                "longitude='" + longitude + '\'' +
                ", latitude=" + latitude +
                ", label=" + label +
                '}';
    }
}



