package com.nadu.foodsearch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ViewPagerAdapterGood extends FragmentPagerAdapter {
    public ViewPagerAdapterGood(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return Frag_good_First.newInstance();
            case 1:
                return Frag_Good_Second.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "랜덤 추천";
            case 1:
                return "검색 추천";
            default:
                return null;
        }
    }
}
