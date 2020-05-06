package com.nadu.foodsearch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
        recyclerView.setLayoutManager(layoutManager);
        arrayListAll = new ArrayList<>();

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
                                foodList.setShop_No(Integer.parseInt(String.valueOf(document.get("Shop_No"))));
                                arrayListAll.add(foodList);
                            }
                            readDB(arrayListAll);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

            et_SearchShop = view.findViewById(R.id.et_SearchShop);

            view.findViewById(R.id.btn_All).setOnClickListener(onClickListener);
            view.findViewById(R.id.btn_Korean).setOnClickListener(onClickListener);
            view.findViewById(R.id.btn_Yangsik).setOnClickListener(onClickListener);
            view.findViewById(R.id.btn_Japan).setOnClickListener(onClickListener);
            view.findViewById(R.id.btn_China).setOnClickListener(onClickListener);
            view.findViewById(R.id.btn_Search).setOnClickListener(onClickListener);
            view.findViewById(R.id.btn_Location).setOnClickListener(onClickListener);
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
                    readDB(readFood(arrayListAll,"한식"));
                    break;
                case R.id.btn_China:
                    readDB(readFood(arrayListAll,"중식"));
                    break;
                case R.id.btn_Japan:
                    readDB(readFood(arrayListAll,"일식"));
                    break;
                case R.id.btn_Yangsik:
                    readDB(readFood(arrayListAll,"양식"));
                    break;
                case R.id.btn_Search:
                    shop = et_SearchShop.getText().toString();
                    readDB(searchFood(arrayListAll,et_SearchShop.getText().toString()));
                    break;
                case R.id.btn_Location:
                    Intent intent = new Intent(getActivity(),ShopMyLocation.class);
                    startActivity(intent);

            }
        }
    };

    private ArrayList<FoodList> searchFood(ArrayList<FoodList> arrayListAll ,String Shop){
        ArrayList<FoodList> arrayList = new ArrayList<>();
        for(FoodList foodList : arrayListAll){
            if (foodList.getTv_Shop_Name().equals(shop)){
                arrayList.add(foodList);
            }
        }
        if(arrayList.size()<=0){
            Toast.makeText(getContext(),"일치하는 가게가 없습니다.",Toast.LENGTH_SHORT).show();
            return arrayListAll;
        }
        return arrayList;
    }

    private void readDB(ArrayList<FoodList> arrayList){
        adapter = new FoodListAdapter(arrayList, getContext());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private ArrayList<FoodList> readFood(ArrayList<FoodList> arrayList, String food){
        ArrayList<FoodList> foodListsArr = new ArrayList<>();
        for(FoodList foodList : arrayList){
            if(foodList.getTv_Shop_Food().equals(food)){
                foodListsArr.add(foodList);
            }
        }
        return foodListsArr;
    }
}