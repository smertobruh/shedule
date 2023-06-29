package com.example.shedule;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shedule.model.GroupShedule;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.util.Arrays;

public class StartActivity extends AppCompatActivity {

    SharedPreferences settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    @Override
    public void onResume(){
        super.onResume();
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            String[] files = fileList();
            String x = "Settings.json";
            if(has(files,x) && !haveBadURL()){

                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                startActivity(intent);
            } else {

                Intent intent = new Intent(StartActivity.this, InputActivity.class);
                startActivity(intent);
            }
        }, 2000);

    }
    private boolean has(String[] strings, String x){
        boolean flag = false;
        for(String str : strings) {
            if(str.equals(x)) flag = true;
        }
        return flag;
    }
    private boolean haveBadURL() {
        boolean flag = false;
        File file = getFileStreamPath("Settings.json");
        if(file != null){
            Object o;
            try {
                o = new JSONParser().parse(new FileReader(file));
                JSONObject j = (JSONObject) o;
                if(j.get("badURL")!=null) flag= (boolean)j.get("badURL");
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

        }
        return flag;
    }

}
