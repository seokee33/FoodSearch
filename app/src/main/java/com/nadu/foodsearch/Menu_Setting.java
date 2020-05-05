package com.nadu.foodsearch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class Menu_Setting extends Fragment {
    private View view;
    private Button btn_logout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_menu__setting, container, false);
            btn_logout = view.findViewById(R.id.btn_logout);
            btn_logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth.getInstance().signOut();
                    myStartActivity(Login.class);
                }
            });
        return view;
    }

    private void myStartActivity(Class activity){
        Intent intent = new Intent(getActivity(), activity);
        getActivity().finish();
        startActivity(intent);
    }

}
