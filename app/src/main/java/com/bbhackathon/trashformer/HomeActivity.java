package com.bbhackathon.trashformer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bbhackathon.trashformer.Manager.FirebaseAuthManager;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if(FirebaseAuthManager.getInstance()!=null){
            Toast.makeText(getBaseContext(), FirebaseAuthManager.getInstance().getUser().getEmail(),
                    Toast.LENGTH_SHORT).show();
        }

        Button btnSetting = findViewById(R.id.btnSetting);
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuthManager.getInstance().logout();
                Toast.makeText(getBaseContext(), "登出",
                        Toast.LENGTH_SHORT).show();
                Intent i = new Intent(HomeActivity.this, BeforeLoginActivity.class);
                startActivity(i);
            }
        });

        Button btnEquipment = findViewById(R.id.equipment);
        btnEquipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

    }
}
