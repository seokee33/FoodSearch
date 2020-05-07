package com.nadu.foodsearch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Frag_Good_Second extends Fragment {
    private View view;

    public static Frag_Good_Second newInstance(){
        Frag_Good_Second frag_good_second = new Frag_Good_Second();
        return frag_good_second;
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_good_second,container,false);
        
        return view;
    }
}
