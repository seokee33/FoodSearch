package com.nadu.foodsearch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Menu_List extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<FoodList> arrayListAll;
    private ArrayList<FoodList> arrayListKorean;
    private ArrayList<FoodList> arrayListChina;
    private ArrayList<FoodList> arrayListYangsik;
    private ArrayList<FoodList> arrayListJapan;
    private ArrayList<FoodList> arrayListSearchShop;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseFirestore db;

    private String TAG = "Menu_List Food db";

    private EditText et_SearchShop;
    private String shop;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_menu__list,container,false);

        recyclerView = view.findViewById(R.id.rv_Food_List);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());

        db = FirebaseFirestore.getInstance();

        et_SearchShop = view.findViewById(R.id.et_SearchShop);

        arrayListSearchShop = new ArrayList<>();


        recyclerView.setLayoutManager(layoutManager);
        arrayListAll = new ArrayList<>();
        arrayListKorean = new ArrayList<>();
        arrayListYangsik = new ArrayList<>();
        arrayListChina = new ArrayList<>();
        arrayListJapan = new ArrayList<>();

        db.collection("FoodList")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                FoodList foodList = new FoodList();
                                foodList.setTv_Shop_Name((String) document.get("tv_Shop_Name"));
                                foodList.setTv_Shop_Food((String) document.get("tv_Shop_Food"));
                                foodList.setTv_Shop_Time((String) document.get("tv_Shop_Time"));
                                foodList.setIv_Shop_Profile((String) document.get("iv_Shop_Profile"));
                                foodList.setTv_Shop_Lat((double)document.get("lat"));
                                foodList.setTv_Shop_Lng((double)document.get("lng"));
                                foodList.setTv_Shop_Number((String)document.get("tv_Shop_Number"));
                                arrayListAll.add(foodList);
                                switch (foodList.getTv_Shop_Food()){
                                    case "중식":
                                        arrayListChina.add(foodList);
                                        break;
                                    case "한식":
                                        arrayListKorean.add(foodList);
                                        break;
                                    case "양식":
                                        arrayListYangsik.add(foodList);
                                        break;
                                    case "일식":
                                        arrayListJapan.add(foodList);
                                        break;
                                }
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                                adapter = new FoodListAdapter(arrayListAll, getContext());
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

            view.findViewById(R.id.btn_All).setOnClickListener(onClickListener);
            view.findViewById(R.id.btn_Korean).setOnClickListener(onClickListener);
            view.findViewById(R.id.btn_Yangsik).setOnClickListener(onClickListener);
            view.findViewById(R.id.btn_Japan).setOnClickListener(onClickListener);
            view.findViewById(R.id.btn_China).setOnClickListener(onClickListener);
            view.findViewById(R.id.btn_Search).setOnClickListener(onClickListener);
        return view;
        
    }

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_All:
                    readDB(arrayListAll);
                    break;
                case R.id.btn_Korean:
                    readDB(arrayListKorean);
                    break;
                case R.id.btn_China:
                    readDB(arrayListChina);
                    break;
                case R.id.btn_Japan:
                    readDB(arrayListJapan);
                    break;
                case R.id.btn_Yangsik:
                    readDB(arrayListYangsik);
                    break;
                case R.id.btn_Search:
                    shop = et_SearchShop.getText().toString();
                    arrayListSearchShop = searchFood(shop);
                    readDB(arrayListSearchShop);
                    break;
            }
        }
    };

    private ArrayList<FoodList> searchFood(String Shop){
        ArrayList<FoodList> arrayList = null;
        for(FoodList foodList : arrayListAll){
            if (foodList.getTv_Shop_Name().equals(shop)){
                arrayList.add(foodList);
            }else{
                return arrayListAll;
            }
        }
        return arrayList;
    }

    private void readDB(ArrayList<FoodList> arrayList){
        adapter = new FoodListAdapter(arrayList, getContext());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

}