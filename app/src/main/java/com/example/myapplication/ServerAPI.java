package com.example.myapplication;

import android.util.Log;

import androidx.annotation.AnyThread;
import androidx.annotation.WorkerThread;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ServerAPI {
    private volatile static ServerAPI instance = null;

    private OkHttpClient client;

    public ServerAPI() {
        this.client = new OkHttpClient();
    }

    public static ServerAPI provide() {
        if (instance == null) {
            instance = new ServerAPI();
        }
        return instance;
    }

    /**
     * An example of sending a GET request to the server.
     *
     * The /echo/{msg} endpoint always just returns {"message": msg}.
     *
     * This method should can be called on a background thread (Android
     * disallows network requests on the main thread).
     */
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
            friend.setLatitude(jsonObject.getDouble("latitude"));
            friend.setLongitude(jsonObject.getDouble("longitude"));
        } catch (Exception e) {
            e.printStackTrace();
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
