package com.bbhackathon.trashformer.camera;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.bbhackathon.trashformer.entity.CameraResultEntity;
import com.bbhackathon.trashformer.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static com.bbhackathon.trashformer.util.ExtraKey.INTENT_CAMERA_RESULT;

public class CameraResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_result);

        ArrayList<CameraResultEntity> entityList = getIntent().getParcelableArrayListExtra(INTENT_CAMERA_RESULT);

        String cameraResult = "";

        String jsonString = new Gson().toJson(entityList, List.class);

        for(CameraResultEntity entity: entityList){
            String text = entity.getText();
            String entityId = entity.getEntityId();
            float confident = entity.getConfidence();

            String result = "text:" + text +"\t\t\t" + "entityId:" + entityId +"\t\t\t" + "confident:" + confident + "\n";
            cameraResult = cameraResult + result;
        }

        TextView textView = findViewById(R.id.cameraResult);
        textView.setText(cameraResult);
    }
}
