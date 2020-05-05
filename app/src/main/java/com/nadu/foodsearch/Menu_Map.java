package com.nadu.foodsearch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


import android.Manifest;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.primitives.ImmutableDoubleArray;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kyleduo.switchbutton.SwitchButton;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;

import com.naver.maps.map.NaverMap;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Align;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.util.FusedLocationSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.*;
import static androidx.core.content.ContextCompat.getSystemService;
public class Menu_Map extends Fragment implements OnMapReadyCallback, GoogleMap.OnMyLocationChangeListener {
    private View view;
    private String TAG = "Menu_Map";
    private LatLng init_location = new LatLng(35.855602, 128.492213);


    private FirebaseFirestore db;
    private FragmentManager fm;
    private MapFragment mapFragment;
    private GoogleMap googleMap;
    private ArrayList<Marker> shop_Markers = new ArrayList<>();
    private EditText et_Search;
    private LatLng searchLatLng;
    private Button btn_Search;
    private
    boolean setMyLocationEnabled_check = false;
    //////////////////////////
    private GpsTracker gpsTracker;



    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            view = inflater.inflate(R.layout.activity_menu_googlemap,container,false);
        }catch (InflateException e){

        }

        if (!checkLocationServicesStatus()) {

            showDialogForLocationServiceSetting();
        }else {

            checkRunTimePermission();
        }


        et_Search = view.findViewById(R.id.et_Map_Serarch);
        btn_Search = view.findViewById(R.id.btn_Map_Search);
        btn_Search.setOnClickListener(v -> {
            String address = et_Search.getText().toString();
            if (address.length()<=0) {
                return;
            } else {
                GeoCoderGetAddress geoCoderGetAddress = new GeoCoderGetAddress();

                geoCoderGetAddress = getAddressFromGeoCoder(address);
                searchLatLng = new LatLng(geoCoderGetAddress.getX(), geoCoderGetAddress.getY());
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(searchLatLng, 16));
            }
        });


        Button btn_Map_Change = view.findViewById(R.id.btn_Map_Change);
        btn_Map_Change.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(),streetMap.class);
            startActivity(intent);
        });

        fm = getActivity().getFragmentManager();
        mapFragment = (MapFragment)fm.findFragmentById(R.id.map_NormalView);
        mapFragment.getMapAsync(this);


        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(view!= null){
            ViewGroup parent = (ViewGroup)view.getParent();
            if(parent != null){
                parent.removeView(view);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(init_location,16));
        googleMap.setBuildingsEnabled(true);
        googleMap.setMyLocationEnabled(setMyLocationEnabled_check);

        db = FirebaseFirestore.getInstance();
        db.collection("FoodList")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Marker marker = new Marker();
                            marker.setLat((double) document.get("lat"));
                            marker.setLng((double) document.get("lng"));
                            marker.setShop_Name((String) document.get("tv_Shop_Name"));
                            marker.setShop_Food((String) document.get("tv_Shop_Food"));

                            shop_Markers.add(marker);
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.title(marker.getShop_Name())
                                    .snippet(marker.getShop_Food())
                                    .position(new LatLng(marker.getLat(),marker.getLng()));
                            googleMap.addMarker(markerOptions);

                            Log.i(TAG,"이름 : "+marker.getShop_Name()+" 음식 : "+marker.getShop_Food()+" 위도 : "+marker.getLat()+" 경도 : "+marker.getLng());
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }

    @Override
    public void onMyLocationChange(Location location) {
        double d1=location.getLatitude();
        double d2=location.getLongitude();
        Log.e("onMyLocationChange", d1 + "," + d2);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(d1,d2), 16));
    }

    /*
     * ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드입니다.
     */
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if ( permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;


            // 모든 퍼미션을 허용했는지 체크합니다.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if ( check_result ) {
                setMyLocationEnabled_check= true;
                //위치 값을 가져올 수 있음
                ;
            }
            else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[1])) {

                    Toast.makeText(getContext(), "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    getActivity().finish();


                }else {

                    Toast.makeText(getContext(), "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();

                }
            }

        }
    }

    void checkRunTimePermission(){

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            // 3.  위치 값을 가져올 수 있음

            setMyLocationEnabled_check = true;

        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(getContext(), "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

    }

    private GeoCoderGetAddress getAddressFromGeoCoder(String addr){
        GeoCoderGetAddress geoCoderGetAddress = new GeoCoderGetAddress();
        geoCoderGetAddress.setAddr(addr);

        Geocoder geocoder = new Geocoder(getContext());
        List<Address> listAddress;
        try {
            listAddress = geocoder.getFromLocationName(addr,1);
        }catch (IOException e){
            e.printStackTrace();
            geoCoderGetAddress.setHavePoint(false);
            return geoCoderGetAddress;
        }

        if(listAddress.isEmpty()){
            geoCoderGetAddress.setHavePoint(false);
            return geoCoderGetAddress;
        }

        geoCoderGetAddress.setHavePoint(true);
        geoCoderGetAddress.setX(listAddress.get(0).getLatitude());
        geoCoderGetAddress.setY(listAddress.get(0).getLongitude());

        return geoCoderGetAddress;

    }

    public String getCurrentAddress( double latitude, double longitude) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    7);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(getContext(), "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(getContext(), "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }



        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(getActivity(), "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";

        }

        Address address = addresses.get(0);
        return address.getAddressLine(0).toString()+"\n";

    }


    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                        checkRunTimePermission();
                        return;
                    }
                }

                break;
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

}



















//public class Menu_Map extends Fragment implements OnMapReadyCallback, GoogleMap.OnMyLocationChangeListener {
//    private View view;
//    private String TAG = "Menu_Map";
//
//
//
//    private FirebaseFirestore db;
//    private FragmentManager fm;
//    private MapFragment mapFragment;
//    private GoogleMap googleMap;
//    private ArrayList<Marker> shop_Markers = new ArrayList<>();
//
//    boolean setMyLocationEnabled_check = false;
//    //////////////////////////
//    private GpsTracker gpsTracker;
//
//
//
//    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
//    private static final int PERMISSIONS_REQUEST_CODE = 100;
//    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
//
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        try {
//            view = inflater.inflate(R.layout.activity_menu__map,container,false);
//        }catch (InflateException e){
//
//        }
//
//        if (!checkLocationServicesStatus()) {
//
//            showDialogForLocationServiceSetting();
//        }else {
//
//            checkRunTimePermission();
//        }
//        fm = getActivity().getFragmentManager();
//        mapFragment = (MapFragment)fm.findFragmentById(R.id.map_NormalView);
//        mapFragment.getMapAsync(this);
//
//        final TextView tv_thisMap = (TextView)view.findViewById(R.id.tv_thisMap);
//        SwitchButton switchButton = (SwitchButton)view.findViewById(R.id.sb_Map_Change);
//        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    tv_thisMap.setText("스트리트지도");
//                }else{
//                    tv_thisMap.setText("일반지도");
//                    mapFragment = (MapFragment)fm.findFragmentById(R.id.map_NormalView);
//                }
//            }
//        });
//        return view;
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if(view!= null){
//            ViewGroup parent = (ViewGroup)view.getParent();
//            if(parent != null){
//                parent.removeView(view);
//            }
//        }
//    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        this.googleMap = googleMap;
//        LatLng init_location = new LatLng(35.855602, 128.492213);
//        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(init_location,16));
//
//        googleMap.setMyLocationEnabled(setMyLocationEnabled_check);
//
//        db = FirebaseFirestore.getInstance();
//        db.collection("FoodList")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Marker marker = new Marker();
//                                marker.setLat((double) document.get("lat"));
//                                marker.setLng((double) document.get("lng"));
//                                marker.setShop_Name((String) document.get("tv_Shop_Name"));
//                                marker.setShop_Food((String) document.get("tv_Shop_Food"));
//
//                                shop_Markers.add(marker);
//                                MarkerOptions markerOptions = new MarkerOptions();
//                                markerOptions.title(marker.getShop_Name())
//                                        .snippet(marker.getShop_Food())
//                                        .position(new LatLng(marker.getLat(),marker.getLng()));
//                                googleMap.addMarker(markerOptions);
//
//                                Log.i(TAG,"이름 : "+marker.getShop_Name()+" 음식 : "+marker.getShop_Food()+" 위도 : "+marker.getLat()+" 경도 : "+marker.getLng());
//                            }
//                        } else {
//                            Log.w(TAG, "Error getting documents.", task.getException());
//                        }
//                    }
//                });
//    }
//
//    @Override
//    public void onMyLocationChange(Location location) {
//        double d1=location.getLatitude();
//        double d2=location.getLongitude();
//        Log.e("onMyLocationChange", d1 + "," + d2);
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(d1,d2), 16));
//    }
//
//    /*
//     * ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드입니다.
//     */
//    @Override
//    public void onRequestPermissionsResult(int permsRequestCode,
//                                           @NonNull String[] permissions,
//                                           @NonNull int[] grandResults) {
//
//        if ( permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {
//
//            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면
//
//            boolean check_result = true;
//
//
//            // 모든 퍼미션을 허용했는지 체크합니다.
//
//            for (int result : grandResults) {
//                if (result != PackageManager.PERMISSION_GRANTED) {
//                    check_result = false;
//                    break;
//                }
//            }
//
//
//            if ( check_result ) {
//                setMyLocationEnabled_check= true;
//                //위치 값을 가져올 수 있음
//                ;
//            }
//            else {
//                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.
//
//                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[0])
//                        || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[1])) {
//
//                    Toast.makeText(getContext(), "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
//                    getActivity().finish();
//
//
//                }else {
//
//                    Toast.makeText(getContext(), "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();
//
//                }
//            }
//
//        }
//    }
//
//    void checkRunTimePermission(){
//
//        //런타임 퍼미션 처리
//        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
//        int hasFineLocationPermission = ContextCompat.checkSelfPermission(getContext(),
//                Manifest.permission.ACCESS_FINE_LOCATION);
//        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(getContext(),
//                Manifest.permission.ACCESS_COARSE_LOCATION);
//
//
//        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
//                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
//
//            // 2. 이미 퍼미션을 가지고 있다면
//            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)
//
//
//            // 3.  위치 값을 가져올 수 있음
//
//            setMyLocationEnabled_check = true;
//
//        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.
//
//            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
//            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[0])) {
//
//                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
//                Toast.makeText(getContext(), "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
//                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
//                ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS,
//                        PERMISSIONS_REQUEST_CODE);
//
//
//            } else {
//                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
//                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
//                ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS,
//                        PERMISSIONS_REQUEST_CODE);
//            }
//
//        }
//
//    }
//
//
//    public String getCurrentAddress( double latitude, double longitude) {
//
//        //지오코더... GPS를 주소로 변환
//        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
//
//        List<Address> addresses;
//
//        try {
//
//            addresses = geocoder.getFromLocation(
//                    latitude,
//                    longitude,
//                    7);
//        } catch (IOException ioException) {
//            //네트워크 문제
//            Toast.makeText(getContext(), "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
//            return "지오코더 서비스 사용불가";
//        } catch (IllegalArgumentException illegalArgumentException) {
//            Toast.makeText(getContext(), "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
//            return "잘못된 GPS 좌표";
//
//        }
//
//
//
//        if (addresses == null || addresses.size() == 0) {
//            Toast.makeText(getActivity(), "주소 미발견", Toast.LENGTH_LONG).show();
//            return "주소 미발견";
//
//        }
//
//        Address address = addresses.get(0);
//        return address.getAddressLine(0).toString()+"\n";
//
//    }
//
//
//    //여기부터는 GPS 활성화를 위한 메소드들
//    private void showDialogForLocationServiceSetting() {
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//        builder.setTitle("위치 서비스 비활성화");
//        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
//                + "위치 설정을 수정하실래요?");
//        builder.setCancelable(true);
//        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int id) {
//                Intent callGPSSettingIntent
//                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
//            }
//        });
//        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int id) {
//                dialog.cancel();
//            }
//        });
//        builder.create().show();
//    }
//
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        switch (requestCode) {
//
//            case GPS_ENABLE_REQUEST_CODE:
//
//                //사용자가 GPS 활성 시켰는지 검사
//                if (checkLocationServicesStatus()) {
//                    if (checkLocationServicesStatus()) {
//
//                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
//                        checkRunTimePermission();
//                        return;
//                    }
//                }
//
//                break;
//        }
//    }
//
//    public boolean checkLocationServicesStatus() {
//        LocationManager locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
//
//        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
//                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//    }
//
//
//}



































//public class Menu_Map extends Fragment implements OnMapReadyCallback {
//    private View view;
//    private String TAG = "Menu_Map";
//
//    private ArrayList<Marker> arrayList;
//    private FirebaseFirestore db;
//
//    private FragmentManager fm;
//    private MapFragment mapFragment;
//
//
//    //현위치
//    private FusedLocationSource locationSource;
//    private NaverMap naverMap;
//    private InfoWindow infoWindow;
//    private static final int ACCESS_LOCATION_PERMISSION_REQUEST_CODE = 100;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.activity_menu__map, container, false);
//
//        fm = getFragmentManager();
//        mapFragment = (MapFragment)fm.findFragmentById(R.id.map_Naver);
//        if (mapFragment == null) {
//            mapFragment = MapFragment.newInstance();
//            fm.beginTransaction().add(R.id.map_Naver, mapFragment).commit();
//        }
//
//        mapFragment.getMapAsync(this);
//
//        return view;
//    }
//
//
//    @Override
//    public void onMapReady(@NonNull final NaverMap naverMap) {
//        this.naverMap = naverMap;
//        locationSource = new FusedLocationSource(getActivity(),ACCESS_LOCATION_PERMISSION_REQUEST_CODE);
//        naverMap.setLocationSource(locationSource);
//        UiSettings uiSettings = naverMap.getUiSettings();
//        uiSettings.setLocationButtonEnabled(true);
//
//        LatLng initialPosition = new LatLng(35.857420, 128.485549);
//        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(initialPosition).animate(CameraAnimation.Fly);
//        naverMap.moveCamera(cameraUpdate);
//        naverMap.setMinZoom(5.0);
//
//        db = FirebaseFirestore.getInstance();
//        db.collection("FoodList")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                com.naver.maps.map.overlay.Marker marker = new com.naver.maps.map.overlay.Marker();
//                                marker.setPosition(new LatLng((double)document.get("lat"),(double)document.get("lng")));
//                                marker.setMap(naverMap);
//                                marker.setCaptionText((String) document.get("tv_Shop_Name"));
//                                marker.setCaptionAligns(Align.Top);
//                                marker.setWidth(50);
//                                marker.setHeight(80);
//                                Log.d(TAG, document.getId() + " => " + document.getData());
//                            }
//                        } else {
//                            Log.w(TAG, "Error getting documents.", task.getException());
//                        }
//                    }
//                });
//
//
//
//
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        switch (requestCode){
//            case ACCESS_LOCATION_PERMISSION_REQUEST_CODE:
//                locationSource.onRequestPermissionsResult(requestCode,permissions,grantResults);
//                return;
//        }
//    }
//
//    public void dbRead(){
//        db.collection("FoodList")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                com.naver.maps.map.overlay.Marker marker = new com.naver.maps.map.overlay.Marker();
//                                marker.setPosition(new LatLng((double)document.get("lat"),(double)document.get("lng")));
//                                marker.setMap(naverMap);
//                                marker.setCaptionText((String) document.get("tv_Shop_Name"));
//                                marker.setCaptionAligns(Align.Top);
//                                marker.setWidth(50);
//                                marker.setHeight(80);
//                                Log.d(TAG, document.getId() + " => " + document.getData());
//                            }
//                        } else {
//                            Log.w(TAG, "Error getting documents.", task.getException());
//                        }
//                    }
//                });
//    }
//}
