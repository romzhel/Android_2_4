package com.example.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private final String PREF_URL = "url";
    private Requester requester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initGui();
        initWebDisplaying();
        loadPreferences();
    }

    public void initGui() {
        findViewById(R.id.btn_url_open).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = ((TextInputEditText) findViewById(R.id.url_input)).getEditableText().toString();
                requester.run(url);
            }
        });

        findViewById(R.id.btn_url_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePreferences();
            }
        });
    }

    private void initWebDisplaying() {
        requester = new Requester(new Requester.OnMyAsyncTaskListener() {
            @Override
            public void onResultReady(final String result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (result != null) {
                            Log.d("myApp", "OK");
                            ((WebView) findViewById(R.id.web_view)).loadData(result,
                                    "text/html; charset=utf8", "utf-8");
                        } else {
                            Toast.makeText(MainActivity.this,
                                    getResources().getString(R.string.invalid_URL), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });
    }

    private void savePreferences() {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_URL, ((TextInputEditText) findViewById(R.id.url_input)).
                getEditableText().toString());
        editor.commit();
    }

    private void loadPreferences() {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        String url = sharedPreferences.getString(PREF_URL, getResources().getString(R.string.home_page));
        requester.run(url);
    }
}
