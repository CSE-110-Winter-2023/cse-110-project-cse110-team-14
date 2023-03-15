package com.example.myapplication;

import android.util.Log;

import androidx.annotation.AnyThread;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.internal.bind.DefaultDateTypeAdapter;

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
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import okhttp3.Response;

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
    public void updateLocation(Friend friend, String url) {
        // URLs cannot contain spaces, so we replace them with %20.
        String encodedMsg = friend.getPublic_code().replace(" ", "%20");
        String encodedURL = url.replace(" ", "n%20");

        var request = new Request.Builder()
                .url(encodedURL + encodedMsg)
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

    @WorkerThread
    public void uploadLocation(LatLng myLocation, String url) {

        String encodedURL = url.replace(" ", "n%20");

        String postBody = "{\n"
                + "\"private_code\": \"" + privateCode + "\",\n"
                + "\"label\": \"" + myName + "\",\n"
                + "\"latitude\": " + myLocation.latitude + ",\n"
                + "\"longitude\": " + myLocation.longitude + "\n"
                + "}";

        var requestBody = RequestBody.create(postBody, JSON);

        var request = new Request.Builder()
                .url(encodedURL + myUID)
                .method("PUT", requestBody)
                .build();

        try (var response = client.newCall(request).execute()) {

            String responseBody = response.body().string();

        } catch (Exception e) {

            e.printStackTrace();

        }

        firstTime = false;

    }

    @WorkerThread
    public Friend get(String uid) throws Exception {
        String url = "https://socialcompass.goto.ucsd.edu/location/" + uid.replace(" ", "%20");
        var request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .build();
        try(Response response = client.newCall(request).execute()) {
            var body = response.body().string();

            if(!response.isSuccessful()) {
                System.out.println("Response not successful: " + body);
                return null;
            }
            System.out.println("Returning friend: " + body);
            return Friend.fromJSON(body);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @WorkerThread
    public void putNote(Friend friend) {
        final MediaType JSON = MediaType.get("application/json; charset=utf-8");
        Gson gson = new Gson();
        String url = "https://socialcompass.goto.ucsd.edu/location/";
        String publicCode = friend.getPublic_code();
        var body = RequestBody.create("", JSON);
        var request = new Request.Builder()
                .url(url + publicCode)
                .method("PUT", body)
                .build();

        try (var response = client.newCall(request).execute()) {
            assert response.body() != null;
            var b = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
