package com.example.shedule;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class WeekAdapter extends FragmentStateAdapter {
    public WeekAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return(WeekPageFragment.newInstance(position));
    }

    @Override
    public int getItemCount() {
        return 26;
    }
}