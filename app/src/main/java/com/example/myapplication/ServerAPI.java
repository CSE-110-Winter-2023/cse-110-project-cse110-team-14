package com.example.myapplication;

import android.util.Log;

import androidx.annotation.AnyThread;
import androidx.annotation.WorkerThread;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ServerAPI {
    private volatile static ServerAPI instance = null;

    private OkHttpClient client;
    private String myName;
    private String myUID;
    private String privateCode;
    private boolean firstTime;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    public ServerAPI(String name, String UID, String privateCode) {
        myName = name;
        myUID = UID;
        this.privateCode = privateCode;
        firstTime = true;
        this.client = new OkHttpClient();
    }

    public static ServerAPI provide(String name, String UID, String privateCode) {
        if (instance == null) {
            instance = new ServerAPI(name, UID, privateCode);
        }
        return instance;
    }


    @WorkerThread
    public void updateLocation(Friend friend) {
        // URLs cannot contain spaces, so we replace them with %20.
        String encodedMsg = friend.getPublic_code().replace(" ", "%20");

        var request = new Request.Builder()
                .url("https://socialcompass.goto.ucsd.edu/location/" + encodedMsg)
                .method("GET", null)
                .build();

        try (var response = client.newCall(request).execute()) {

            assert response.body() != null;
            var body = response.body().string();
            JSONObject jsonObject = new JSONObject(body);
            friend.setLabel(jsonObject.getString("label"));
            friend.setLatitude(jsonObject.getDouble("latitude"));
            friend.setLongitude(jsonObject.getDouble("longitude"));

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    public void uploadLocation(LatLng myLocation) {

        String postBody = "{\n"
                + "\"private_code\": \"" + privateCode + "\",\n"
                + "\"label\": \"" + myName + "\",\n"
                + "\"latitude\": " + myLocation.latitude + ",\n"
                + "\"longitude\": " + myLocation.longitude + "\n"
                + "}";

        var requestBody = RequestBody.create(postBody, JSON);

        var request = new Request.Builder()
                .url("https://socialcompass.goto.ucsd.edu/location/" + myUID)
                .method("PUT", requestBody)
                .build();

        try (var response = client.newCall(request).execute()) {

            String responseBody = response.body().string();
            Log.d("MainActivity", responseBody);
            Log.d("MainActivity", myUID);

        } catch (Exception e) {

            e.printStackTrace();

        }

        firstTime = false;

    }


    /**
    @AnyThread
    public Future<String> echoAsync(String msg) {
        var executor = Executors.newSingleThreadExecutor();
        var future = executor.submit(() -> echo(msg));

        // We can use future.get(1, SECONDS) to wait for the result.
        return future;
    }
    **/
}
