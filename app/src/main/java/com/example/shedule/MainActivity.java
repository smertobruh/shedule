package com.example.shedule;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.shedule.model.*;

import android.util.Log;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

//    private AppBarConfiguration appBarConfiguration;
//    private ActivityMainBinding binding;
    GroupShedule gs = null;
    DayPageFragment dayPageFragment;
    WeekPageFragment weekPageFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StringBuilder sb = new StringBuilder();
        for(int i =0; i < fileList().length; i++){
            sb.append(fileList()[i]).append('\n');
        }
        setContentView(R.layout.activity_main);
        getting getting = new getting();
        getting.execute();
        ViewPager2 weekPager = findViewById(R.id.weekpager);
        FragmentStateAdapter weekPageAdapter = new WeekAdapter(this);
        weekPager.setAdapter(weekPageAdapter);
        ViewPager2 dayPager= findViewById(R.id.daypager);
        FragmentStateAdapter dayPageAdapter = new DayAdapter(this);
        dayPager.setAdapter(dayPageAdapter);
    }
    public Week getWeek(int number){
        if(gs == null) return null;
        else return gs.getWeek(number);
    }
//    public Day getDay(int number) {
//        //return gs.
//    }
    class getting extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            File file = getFileStreamPath("savedShedule.json");
            if(file.exists()){
                Object o;
                try {
                    o = new JSONParser().parse(new FileReader(file));
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                try {
                    gs = new GroupShedule(o);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
            File settings = getFileStreamPath("Settings.json");
                JSONObject j = null;
                try {
                    j = (JSONObject) new JSONParser().parse(new FileReader(settings));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                String url = (String) j.get("url");
            System.out.println(url);

            try {
                gs = new GroupShedule(url);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                FileOutputStream fos = null;
                try {
                    fos = openFileOutput("savedShedule.json", MODE_PRIVATE);
                    fos.write(gs.toJSON().toJSONString().getBytes());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return gs.toString();
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(getApplicationContext(), "Shedule saved!", Toast.LENGTH_SHORT).show();
            dayPageFragment.resetLessons();
            //textView.setText("null");
        }
    }
    public void setDayPageFragment(DayPageFragment fragment) {this.dayPageFragment = fragment;}
    public void setWeekPageFragment(WeekPageFragment fragment) {this.weekPageFragment = fragment;}
    public void resetLessons(){
        dayPageFragment.resetLessons();
    }
}