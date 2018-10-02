package com.bbhackathon.trashformer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bbhackathon.trashformer.Manager.FirebaseAuthManager;
import com.bbhackathon.trashformer.Manager.LoginManager;
import com.bbhackathon.trashformer.login.LoginActivity;
import com.google.firebase.auth.FirebaseUser;

public class BeforeLoginActivity extends AppCompatActivity {

    LoginManager mLoginmanager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_login);

        mLoginmanager = new LoginManager(this.getApplicationContext());

        FirebaseUser user = FirebaseAuthManager.getInstance().getUser();

        if(user != null){
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }
}
