package com.nadu.foodsearch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Menu_List extends Fragment implements Serializable {
    private View view;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<FoodList> arrayListAll;
    private GridLayoutManager layoutManager;

    private String TAG = "Menu_List Food db";

    private EditText et_SearchShop;
    private String shop;

    private Fragment fragment;

    static RequestQueue requestQueue;
    private ShopList shopList;
    private Thread thread;
    private boolean isThread = true;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_menu__list,container,false);

        et_SearchShop = view.findViewById(R.id.et_SearchShop);
        view.findViewById(R.id.btn_Search).setOnClickListener(onClickListener);
        view.findViewById(R.id.btn_Location).setOnClickListener(onClickListener);

        layoutManager = new GridLayoutManager(getContext(),2);
        recyclerView = view.findViewById(R.id.rv_Food_List);
        recyclerView.setHasFixedSize(true);
        int spanCount = 2; // 3 columns
        int spacing = 10; // 50px
        boolean includeEdge = true;
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        recyclerView.setLayoutManager(layoutManager);


        requestQueue = Volley.newRequestQueue(getContext());
        arrayListAll = new ArrayList<FoodList>();
        request("http://35.225.6.115/db.php");
        adapter = new FoodListAdapter(arrayListAll, getContext());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        return view;
    }

    private void readDB(ArrayList<FoodList> arrayList){
        adapter = new FoodListAdapter(arrayList, getContext());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void request(String urlStr){
        StringRequest request = new StringRequest(
                Request.Method.GET,
                urlStr,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG,"응답 -> "+response);

                        processResponse(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG,"에러 -> "+ error.toString());
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                return params;
            }
        };

        request.setShouldCache(false);
        requestQueue.add(request);

    }

    public void processResponse(String response){
        Gson gson = new Gson();

        shopList = gson.fromJson(response, ShopList.class);

        for(FoodList foodList : shopList.shopListResult.foodList){
            Log.i(TAG,"이름 : "+foodList.getShopname());
            arrayListAll.add(foodList);
        }
        readDB(arrayListAll);


    }

    private ArrayList<FoodList> searchFood(ArrayList<FoodList> arrayListAll ,String Shop){
        ArrayList<FoodList> arrayList = new ArrayList<>();
        for(FoodList foodList : arrayListAll){
            if (foodList.getShopname().equals(shop)){
                arrayList.add(foodList);
            }
        }

        if(arrayList.size()<=0){
            Toast.makeText(getContext(),"일치하는 가게가 없습니다.",Toast.LENGTH_SHORT).show();
            return arrayListAll;
        }
        return arrayList;
    }

    private ArrayList<FoodList> readFood(ArrayList<FoodList> arrayList, String food){
        ArrayList<FoodList> foodListsArr = new ArrayList<>();
        for(FoodList foodList : arrayList){
            if(foodList.getShopfood().equals(food)){
                foodListsArr.add(foodList);
            }
        }
        return foodListsArr;
    }

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_Search:
                    shop = et_SearchShop.getText().toString();
                    readDB(searchFood(arrayListAll,et_SearchShop.getText().toString()));
                    et_SearchShop.setHint("검색");
                    break;
                case R.id.btn_Location:
                    Intent intent = new Intent(getActivity(),ShopMyLocation.class);
                    startActivity(intent);

            }
        }
    };
}