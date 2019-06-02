package com.example.myapplication;

import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Requester {
    private final String LOG_TAG = "myApp";
    private OnMyAsyncTaskListener listener;


    public Requester(OnMyAsyncTaskListener onMyAsyncTaskListener) {
        this.listener = onMyAsyncTaskListener;
    }

    public void run(String url) {
        OkHttpClient client = new OkHttpClient();
        Request.Builder builder = new Request.Builder();

        try {
            builder.url(url);
            Request request = builder.build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d(LOG_TAG, "fault");
                    listener.onResultReady(null);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String answer = response.body().string();
                    listener.onResultReady(answer);
                }
            });

        } catch (IllegalArgumentException e) {
            listener.onResultReady(null);
        }
    }

    public interface OnMyAsyncTaskListener {
        void onResultReady(String result);
    }
}
