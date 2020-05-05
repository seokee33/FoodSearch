package com.nadu.foodsearch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";
    private Button btn_SignUp;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        btn_SignUp = findViewById(R.id.btn_signup);
        btn_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

    }

    private void signUp(){

        String email = ((EditText)findViewById(R.id.et_SignUp_Email)).getText().toString();
        String password = ((EditText)findViewById(R.id.et_SignUp_password)).getText().toString();
        String passwordCheck = ((EditText)findViewById(R.id.et_SignUp_Password_Check)).getText().toString();

        if(email.length()>0 && password.length()>0 && passwordCheck.length()>0){
            if(password.equals(passwordCheck)){
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    startToast("회원가입 완료");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    myStartActivity(UserInit.class);
                                } else {
                                    if(task.getException() != null){
                                        startToast(task.getException().toString());
                                    }

                                }
                            }
                        });
            }else{
                startToast("비밀번호가 일치하지 않습니다.");
            }
        }else{
            startToast("이메일 또는 비밀번호를 입력해 주세요");
        }


    }


    private void startToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
    private void myStartActivity(Class activity){
        Intent intent = new Intent(this, activity);
        finish();
        startActivity(intent);

    }

    @Override
    public void onBackPressed() {
        myStartActivity(Login.class);
    }
}