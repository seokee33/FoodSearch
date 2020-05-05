package com.nadu.foodsearch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    private FragmentManager fm;
    private FragmentTransaction ft;
    private Menu_List menu_list;
    private Menu_Map menu_map;
    private Menu_Setting menu_setting;

    private BackPressHandler backPressHandler = new BackPressHandler(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            myStartActivity(Login.class);
        }

        bottomNavigationView = findViewById(R.id.Menu_BottomNavigaion);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.Menu_Map:
                        setFrag(0);
                        break;
                    case R.id.Menu_List:
                        setFrag(1);
                        break;
                    case R.id.Menu_Setting:
                        setFrag(2);
                        break;
                }
                return true;
            }
        });

        menu_list = new Menu_List();
        menu_map = new Menu_Map();
        menu_setting = new Menu_Setting();
        setFrag(0);
    }
    private void setFrag(int n){
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        switch (n){
            case 0:
                ft.replace(R.id.Menu_frame,menu_map);
                ft.commit();
                break;
            case 1:
                ft.replace(R.id.Menu_frame, menu_list);
                ft.commit();
                break;
            case 2:
                ft.replace(R.id.Menu_frame,menu_setting);
                ft.commit();
                break;
        }
    }
    private void startToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
    private void myStartActivity(Class activity){
        Intent intent = new Intent(this, activity);
        finish();
        startActivity(intent);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
    }

    @Override
    public void onBackPressed() {
        backPressHandler.onBackPressed("한번더 클릭시 종료됩니다",3000);
    }
}
