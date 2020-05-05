package com.nadu.foodsearch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "LoginActivity";
    private BackPressHandler backPressHandler = new BackPressHandler(this);

    private FirebaseAuth mAuth;

    private SignInButton btn_Login_Google;
    private GoogleApiClient googleApiClient;
    private static final int REQ_SIGN_GOOGLE = 100;

    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.btn_Login).setOnClickListener(onClickListener);
        findViewById(R.id.tv_forgot_password).setOnClickListener(onClickListener);
        findViewById(R.id.tv_SignUp).setOnClickListener(onClickListener);


        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,googleSignInOptions)
                .build();

        mAuth = FirebaseAuth.getInstance();
        btn_Login_Google = findViewById(R.id.btn_Login_Google);
        btn_Login_Google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, REQ_SIGN_GOOGLE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) { //구글 로그인 결과값 받는 곳
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQ_SIGN_GOOGLE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount();
                resultLogin(account);
            }
        }
    }

    private void resultLogin(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Login.this,"로그인성공",Toast.LENGTH_SHORT).show();

                            user = FirebaseAuth.getInstance().getCurrentUser();
                            checkUserInit();
                        }else{
                            Toast.makeText(Login.this,"로그인실패",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // 구글로그인
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_Login:
                    login();
                    break;
                case R.id.tv_forgot_password:
                    myStartActivity(ForgotPassword.class);
                    break;
                case R.id.tv_SignUp:
                    myStartActivity(SignUp.class);
                    break;
            }
        }
    };

    private void login(){
        String email = ((EditText)findViewById(R.id.et_Login_Email)).getText().toString();
        String password = ((EditText)findViewById(R.id.et_Login_Passowrd)).getText().toString();

        if(email.length()>0 && password.length()>0){

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                FirebaseUser user = mAuth.getCurrentUser();
                                showToast("로그인 완료");
                                myStartActivity(MainActivity.class);
                            } else {
                                if(task.getException() != null){
                                    showToast(task.getException().toString());
                                }
                            }
                        }
                    });
        }else{
            showToast("이메일 또는 비밀번호를 입력해 주세요");
        }
    }


    private void showToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }
    private void myStartActivity(Class Activity){
        Intent intent = new Intent(this,Activity);
        finish();
        startActivity(intent);

    }

    @Override
    public void onBackPressed() {
        backPressHandler.onBackPressed("한번더 클릭시 종료됩니다",3000);
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
                        myStartActivity(UserInit.class);
                    }
                }else{
                    Log.d(TAG,"get failed with ",task.getException());
                }
            }
        });
    }

}
