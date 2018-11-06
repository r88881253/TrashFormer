package com.bbhackathon.trashformer.login;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.bbhackathon.trashformer.HomeActivity;
import com.bbhackathon.trashformer.R;
import com.bbhackathon.trashformer.databinding.ActivityLoginBinding;
import com.bbhackathon.trashformer.login.fragment.CreateUserDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String TAG = LoginActivity.class.getSimpleName();

    private ActivityLoginBinding binding;

    private LoginViewModel viewmodel;

    private EditText accountEditText;
    private EditText passwordEditText;

    private String account;
    private String passowrd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
//        viewmodel = ViewModelProviders.of(this).get(LoginViewModel.class);
        viewmodel = new LoginViewModel();
        binding.setViewmodel(viewmodel);

        mAuth = FirebaseAuth.getInstance();

        initView();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }


    public void initView(){
//        account = viewmodel.getAccount().get();
//        passowrd = viewmodel.getPassword().get();

        binding.btnCreateAccount.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                CreateUserDialog dialog = new CreateUserDialog();
                dialog.show(getSupportFragmentManager(), "dialog");

            }
        });


        //登入帳號
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signInWithEmailAndPassword(viewmodel.getAccount().get(), viewmodel.getPassword().get())
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                    Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                                    startActivity(i);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }

                                // ...
                            }
                        });
            }
        });
    }

    public void login(String account, String passowrd){
        mAuth.signInWithEmailAndPassword(account, passowrd)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(i);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

//    public void onRegister(View view) {
//        account = accountEditText.getText().toString();
//        passowrd = passwordEditText.getText().toString();
//    }
//
//
//    public void onLogin(View view) {
//        account = accountEditText.getText().toString();
//        passowrd = passwordEditText.getText().toString();
//        mAuth.signInWithEmailAndPassword(account, passowrd)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithEmail:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
//                            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
//                            startActivity(i);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithEmail:failure", task.getException());
//                            Toast.makeText(LoginActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
//                        }
//
//                        // ...
//                    }
//                });
//    }

    private void updateUI(FirebaseUser user){


        if(user!=null){
            Toast.makeText(LoginActivity.this, "已登入",
                    Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(LoginActivity.this, "未登入",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
