package com.nadu.foodsearch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        auth = FirebaseAuth.getInstance();

    }

    private void resetPassword(){
        String email = ((TextView)findViewById(R.id.et_Forgot_Email)).getText().toString();
        if(email.length()>0){
            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                startToast("이메일을 보냈습니다.");
                                myStartActivity(Login.class);
                            }
                        }
                    });
        }else{
            startToast("이메일을 입력해 주세요");
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
