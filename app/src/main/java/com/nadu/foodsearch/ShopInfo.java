package com.nadu.foodsearch;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;

public class ShopInfo extends AppCompatActivity  {


    private FoodList shopInfo = new FoodList();
    private ArrayList<FoodList> arrayList;
    private ImageView iv_shopInfo_Profile;
    private TextView tv_ShopInfo_Name;
    private TextView tv_ShopInfo_Number;
    private TextView tv_ShopInfo_Time;
    private TextView tv_ShopInfo_Location;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_info);
        Intent intent = getIntent();

        shopInfo = (FoodList) getIntent().getSerializableExtra("ShopInfo");
        iv_shopInfo_Profile = findViewById(R.id.iv_ShopInfo_Profile);
        Glide.with(this)
                .load(shopInfo.getIv_Shop_Profile())
                .into(iv_shopInfo_Profile);

        tv_ShopInfo_Name = findViewById(R.id.tv_ShopInfo_Name);
        tv_ShopInfo_Name.setText(shopInfo.getTv_Shop_Name());

        tv_ShopInfo_Number = findViewById(R.id.tv_ShopInfo_Number);
        tv_ShopInfo_Number.setText("Tel : "+shopInfo.getTv_Shop_Number());

        tv_ShopInfo_Time = findViewById(R.id.tv_ShopInfo_Time);
        tv_ShopInfo_Time.setText("Time : "+shopInfo.getTv_Shop_Time());
    }
}
