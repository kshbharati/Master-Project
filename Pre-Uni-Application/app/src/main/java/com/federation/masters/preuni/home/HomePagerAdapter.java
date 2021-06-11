package com.federation.masters.preuni.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.jetbrains.annotations.NotNull;

public class HomePagerAdapter extends FragmentPagerAdapter {

    private static int NUM_ITEMS=3;
    public HomePagerAdapter(@NonNull @NotNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @NotNull
    @Override
    public Fragment getItem(int position) {
        switch(position)
        {
            case 0:
                return new HomePageFragment();
            case 1:
                return new HomeCourseFragment();
            case 2:
                return new HomeContactFragment();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0:
                return "HOME";
            case 1:
                return "COURSES";
            case 2:
                return "CONTACT";
            default:
                return "";
        }
    }
}
