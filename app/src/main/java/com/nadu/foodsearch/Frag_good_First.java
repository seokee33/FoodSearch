package com.nadu.foodsearch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Frag_good_First extends Fragment {
    private View view;

    public static Frag_good_First newInstance(){
        Frag_good_First frag_good_first = new Frag_good_First();
        return frag_good_first;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_good_first,container,false);

        return view;
    }
}
