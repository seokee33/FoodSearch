package com.nadu.foodsearch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserInit extends AppCompatActivity {
    private static final String TAG = "UserInitActivity";
    private RadioButton radioButton;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_init);




        Button btn_SignUp = findViewById(R.id.btn_UserInit_Ok);
        btn_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileUpdate();
                finish();
                myStartActivity(MainActivity.class);
            }
        });
        RadioGroup radioGroup = findViewById(R.id.rg_Gender);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButton = (RadioButton) findViewById(checkedId);
            }
        });


    }

    public void profileUpdate() {
        String nickname = ((EditText) findViewById(R.id.et_Name)).getText().toString();
        String age = ((EditText) findViewById(R.id.et_Age)).getText().toString();
        String gender = radioButton.getText().toString();

        if (nickname.length() > 0 && age.length() > 0 && gender.length()>0) {

            FirebaseFirestore db = FirebaseFirestore.getInstance(); // Cloud Firestore 초기화

            UserInfo userInfo = new UserInfo(nickname,age,gender);

            if(user != null){
                db.collection("users").document(user.getUid()).set(userInfo)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully written!");
                                startToast("회원정보를 등록했습니다.");

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                startToast("회원정보 등록에 실패했습니다.");
                                Log.w(TAG, "Error writing document", e);
                            }
                        });
            }
        } else {
            startToast("회원정보를 입력해주세요");
        }
    }

    private void checkUserInit(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("user").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document != null){
                        Log.d(TAG,"DocumentSnapshot data: "+document.getData());
                        myStartActivity(MainActivity.class);
                    }else{
                        Log.d(TAG,"No Such document");
                    }
                }else{
                    Log.d(TAG,"get failed with ",task.getException());
                }
            }
        });
    }
    private void myStartActivity(Class activity){
        Intent intent = new Intent(this,activity);
        finish();
        startActivity(intent);

    }
    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        finish();
        startToast("회원 등록을 해주세요");
    }
}