package com.example.shedule;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.shedule.model.Week;

import java.text.SimpleDateFormat;

public class WeekPageFragment extends Fragment {
    MainActivity act;
    private int weekNumber;
    private Week week;
    public static WeekPageFragment newInstance(int page) {
        WeekPageFragment fragment = new WeekPageFragment();
        Bundle args=new Bundle();
        args.putInt("num", page);
        fragment.setArguments(args);
        return fragment;
    }

    public WeekPageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weekNumber = getArguments() != null ? getArguments().getInt("num") : 1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View result=inflater.inflate(R.layout.fragment_week, container, false);
        Button pageHeader = result.findViewById(R.id.week);
        String header = "Неделя " + (weekNumber+1);
        act = (MainActivity) getActivity();
        resetDays();
        pageHeader.setText(header);
        return result;
    }
    public void onResume(){
        super.onResume();
        act.setWeekPageFragment(this);
        resetDays();
        act.resetLessons();
    }
    private void resetDays(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while ((act.getWeek(weekNumber)) == null);
                week = act.getWeek(weekNumber);
                Button[] buttons = new Button[6];
                buttons[0] = act.findViewById(R.id.monday);
                buttons[1] =act.findViewById(R.id.tuesday);
                buttons[2] =act.findViewById(R.id.wednesday);
                buttons[3] =act.findViewById(R.id.thursday);
                buttons[4] =act.findViewById(R.id.friday);
                buttons[5] =act.findViewById(R.id.saturday);
                SimpleDateFormat form = new SimpleDateFormat("E \n MMM \n dd");
                for(int i = 0; i < 6; i++){
                    buttons[i].setText(form.format(week.getDay(i).getDate()));
                }
            }
        });
        thread.start();

    }
    public WeekPageFragment getWeekPageFragment(){return this;}
    public int getPageNumber(){return weekNumber;}
}