package com.bbhackathon.trashformer.setting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bbhackathon.trashformer.HomeActivity;
import com.bbhackathon.trashformer.R;
import com.bbhackathon.trashformer.login.BeforeLoginActivity;
import com.bbhackathon.trashformer.manager.FirebaseAuthManager;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ((Button) findViewById(R.id.btnLogout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuthManager.getInstance().logout();
                Toast.makeText(getBaseContext(), "登出",
                        Toast.LENGTH_SHORT).show();
                Intent i = new Intent(SettingActivity.this, BeforeLoginActivity.class);
                startActivity(i);
            }
        });
    }
}
