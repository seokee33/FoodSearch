package com.nadu.foodsearch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;
import com.kyleduo.switchbutton.SwitchButton;

public class streetMap extends AppCompatActivity implements OnStreetViewPanoramaReadyCallback  {
    private BackPressHandler backPressHandler = new BackPressHandler(this);

    private LatLng init_location = new LatLng(35.855602, 128.492213);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_street_map);


        Button btn_Close = findViewById(R.id.btn_StreetMap_Close);
        btn_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        StreetViewPanoramaFragment streetViewPanoramaFragment =
                (StreetViewPanoramaFragment) getFragmentManager()
                        .findFragmentById(R.id.map_streetView);
        streetViewPanoramaFragment.getStreetViewPanoramaAsync(this);
    }

    @Override
    public void onBackPressed() {
        backPressHandler.onBackPressed("한번더 클릭시 종료됩니다",3000);
    }

    @Override
    public void onStreetViewPanoramaReady(StreetViewPanorama panorama) {
        panorama.setPosition(init_location);
    }
}
