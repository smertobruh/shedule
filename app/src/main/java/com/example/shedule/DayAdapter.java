package com.example.shedule;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class DayAdapter extends FragmentStateAdapter {
    public DayAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return(DayPageFragment.newInstance(position));
    }

    @Override
    public int getItemCount() {
        return 6;
    }
}