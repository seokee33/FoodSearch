package com.nadu.foodsearch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class LogoPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo_page);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                myStartActivity(MainActivity.class);
            }
        },2500);
    }

    private void myStartActivity(Class activity){
        Intent intent = new Intent(this,activity);
        finish();
        startActivity(intent);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
    }
}
