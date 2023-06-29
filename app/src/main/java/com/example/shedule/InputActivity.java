package com.example.shedule;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class InputActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
    }

    @Override
    public void onResume(){
        super.onResume();

    }

    public void resume(View view) {
        EditText input = findViewById(R.id.Input);
        String str = input.getText().toString();
        if(!str.startsWith("https://")) str = "https://" +str;
        isShedule(str);
        Handler handler = new Handler();
        String finalStr = str;
        handler.postDelayed(() -> {
            if(finalStr.length() == 0 || !isShedule) Toast.makeText(getApplicationContext(), "Invalid input!", Toast.LENGTH_SHORT).show();
            else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FileOutputStream fos = null;
                    try {
                        fos = openFileOutput("Settings.json", MODE_PRIVATE);
                        JSONObject settings = new JSONObject();
                        settings.put("url", finalStr);
                        fos.write(settings.toJSONString().getBytes());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                Intent intent = new Intent(InputActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }, 2500);

    }

            public void isShedule(String url) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Document doc = null;
                        int tries = 3;
                        while (tries-- != 0) {
                            try {
                                doc = Jsoup.connect(url)
                                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.5112.124")
                                        .referrer("http://www.google.com")
                                        .get();
                                break;
                            } catch (IOException e) {
                               e.printStackTrace();
                            }
                        }
                        if(doc != null) {

                            if(doc.select("div.page-header").size() != 0) isShedule = doc.select("div.page-header").get(0).text().contains("Расписание");
                        }

                    }
                }).start();

    }
    boolean isShedule = false;
}
