package com.example.shedule;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.shedule.model.Day;
import com.example.shedule.model.GroupShedule;
import com.example.shedule.model.Lesson;
import com.example.shedule.model.PairedLesson;
import com.example.shedule.model.SingleLesson;
import com.example.shedule.model.Week;

import java.io.FileOutputStream;
import java.io.IOException;

public class DayPageFragment extends Fragment {

    private int dayNumber;
    private LinearLayout ll;
    private CardView[] cardViews;
    MainActivity act;
    public static DayPageFragment newInstance(int page) {
        DayPageFragment fragment = new DayPageFragment();
        Bundle args=new Bundle();
        args.putInt("num", page);
        fragment.setArguments(args);
        return fragment;
    }

    public DayPageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dayNumber = getArguments() != null ? getArguments().getInt("num") : 1;
        act = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View result=inflater.inflate(R.layout.fragment_day, container, false);
        ll = result.findViewById(R.id.lessonList);
        cardViews = new CardView[8];
        return result;
    }
    @Override
    public void onResume() {
        super.onResume();
        act.setDayPageFragment(this);
        Button dayButton = selectDayButton(dayNumber, act);
        dayButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.open_button));
        resetLessons();

    }

    @Override
    public void onPause(){
        super.onPause();
        Button dayButton = selectDayButton(dayNumber, act);
        dayButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.round_button));
    }
    private Button selectDayButton(int pageNumber, MainActivity v){

        switch (pageNumber) {
            case 0:
                return v.findViewById(R.id.monday);
            case 1:
                return v.findViewById(R.id.tuesday);
            case 2:
                return v.findViewById(R.id.wednesday);
            case 3:
                return v.findViewById(R.id.thursday);
            case 4:
                return v.findViewById(R.id.friday);
            case 5:
                return v.findViewById(R.id.saturday);
            default: return null;
        }
    }
    public void resetLessons() {
        ll.removeAllViews();
        if(act.weekPageFragment != null && act.getWeek(act.weekPageFragment.getPageNumber())!= null){
            for (int i = 0; i < 8; i++) {
                CardView cardView = createLessonCard();
                ll.addView(cardView);
                cardViews[i] = cardView;
                TextView textView = cardView.findViewById(R.id.lessonTime);
                textView.setText(selectTime(i));
                Button button = cardView.findViewById(R.id.lessonNumber);
                button.setText("" + (i + 1));
            }
            Day day = act.getWeek(act.weekPageFragment.getPageNumber()).getDay(dayNumber);
            for (int i = 0; i < 8; i++) {
                CardView cardView = cardViews[i];
                Lesson lesson = day.getLesson(i);
                TextView textView = cardView.findViewById(R.id.lessonInfo);
                if (lesson != null) {
                    if(lesson instanceof SingleLesson) textView.setText(formateLesson(lesson));
                    else if (lesson instanceof PairedLesson){
                        appendPairedLessonCard(cardView);
                        SingleLesson subLesson = ((PairedLesson) lesson).getSubgroup(0);
                        textView.setText(formateLesson(subLesson));
                        textView = cardView.findViewById(R.id.pairedLessonInfo);
                        subLesson = ((PairedLesson) lesson).getSubgroup(1);
                        textView.setText(formateLesson(subLesson));
                    }
                }
            }
        }
    }

    private String formateLesson(Lesson lesson){
        StringBuilder sb = new StringBuilder();
        if(lesson.getDiscipline()!=null) sb.append(lesson.getDiscipline()).append("\n");
        if(lesson.getPlace()!= null) sb.append(lesson.getPlace()).append("\n");
        if(lesson.getTeacher()!= null) sb.append(lesson.getTeacher());
        return sb.toString();
    }
    private void appendPairedLessonCard(CardView cardView){
        Context context = getContext();
        cardView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(320)));
        CardView innerCard = new CardView(getActivity());
        LinearLayout.LayoutParams innerCardParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dpToPx(110));
        innerCardParams.setMargins(dpToPx(10), dpToPx(180), dpToPx(10), 0);
        innerCard.setLayoutParams(innerCardParams);

        innerCard.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white500));
        innerCard.setRadius(dpToPx(16));

        TextView textView = new TextView(context);
        textView.setId(R.id.pairedLessonInfo);
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        textView.setPadding(dpToPx(12), dpToPx(8), dpToPx(1), 0);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        textView.setTextColor(ContextCompat.getColor(context, R.color.white));
        innerCard.addView(textView);
        cardView.addView(innerCard);
    }
    private int selectTime(int i){
        switch (i){
            case 0:
                return R.string.lesson0;
            case 1:
                return R.string.lesson1;
            case 2:
                return R.string.lesson2;
            case 3:
                return R.string.lesson3;
            case 4:
                return R.string.lesson4;
            case 5:
                return R.string.lesson5;
            case 6:
                return R.string.lesson6;
            case 7:
                return R.string.lesson7;
            default: return 0;
        }
    }
    private CardView createLessonCard(){
        Context context = getContext();
        CardView cardView = new CardView(context);
        cardView.setLayoutParams(new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(210)));
        cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.black300));
        cardView.setElevation(0f);
        cardView.setRadius(16);

        Button lessonButton = new Button(context);
        lessonButton.setId(R.id.lessonNumber);
        lessonButton.setBackground(ContextCompat.getDrawable(context, R.drawable.round_button));
        CardView.LayoutParams lessonButtonParams = new CardView.LayoutParams(
                dpToPx(60),
                dpToPx(52));
        lessonButtonParams.setMargins(dpToPx(10), 0, 0, 0);
        lessonButton.setLayoutParams(lessonButtonParams);
        lessonButton.setTextColor(ContextCompat.getColor(context, R.color.white));
        lessonButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        lessonButton.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.white500));

        TextView timeTextView = new TextView(context);
        timeTextView.setId(R.id.lessonTime);
        CardView.LayoutParams firstLessonTimeParams = new CardView.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        firstLessonTimeParams.setMargins(dpToPx(80), dpToPx(15), 0, 0);
        timeTextView.setLayoutParams(firstLessonTimeParams);
        timeTextView.setTextColor(ContextCompat.getColor(context, R.color.white));
        timeTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

        //внутренний cardView содержащий информацию о паре
        CardView innerCard = new CardView(getActivity());
        LinearLayout.LayoutParams innerCardParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dpToPx(110));
        innerCardParams.setMargins(dpToPx(10), dpToPx(60), dpToPx(10), 0);
        innerCard.setLayoutParams(innerCardParams);

        innerCard.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white500));
        innerCard.setRadius(dpToPx(16));

        TextView textView = new TextView(context);
        textView.setId(R.id.lessonInfo);
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        textView.setPadding(dpToPx(12), dpToPx(8), dpToPx(1), 0);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        textView.setTextColor(ContextCompat.getColor(context, R.color.white));
        innerCard.addView(textView);

        cardView.addView(lessonButton);
        cardView.addView(timeTextView);
        cardView.addView(innerCard);
        return cardView;
    }
    public int getDayNumber(){return dayNumber;}
    private int dpToPx(int dp){
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}