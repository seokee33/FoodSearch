package com.nadu.foodsearch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;

public class ShopInfo extends AppCompatActivity implements Serializable {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_info);
        Intent intent = getIntent();
//        ArrayList<FoodList> arrayList = (ArrayList<FoodList>) intent.getSerializableExtra("ShopInfo");
//
//        textView = findViewById(R.id.tv_ShopInfo_Name);
//        textView.setText(arrayList);
    }
}
