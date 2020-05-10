package com.nadu.foodsearch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class Menu_good extends Fragment {
    private View view;
    private FragmentPagerAdapter fragmentPagerAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_menu_good, container, false);
        ViewPager viewPager = view.findViewById(R.id.vp_good);
        fragmentPagerAdapter = new ViewPagerAdapterGood(getActivity().getSupportFragmentManager());

        TabLayout tabLayout = view.findViewById(R.id.tab_Layout_Good);
        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    private void myStartActivity(Class activity){
        Intent intent = new Intent(getActivity(), activity);
        getActivity().finish();
        startActivity(intent);
    }

}
