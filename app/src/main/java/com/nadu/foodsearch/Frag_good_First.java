package com.nadu.foodsearch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Frag_good_First extends Fragment implements Serializable {
    private View view;
    private ArrayList<FoodList> arrayList;
    TextView textView;
    public static Frag_good_First newInstance(){
        Frag_good_First frag_good_first = new Frag_good_First();
        return frag_good_first;
    }

    private Spinner sp_City;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_good_first,container,false);


        return view;
    }
}
