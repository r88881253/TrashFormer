package com.bbhackathon.trashformer.camera;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.bbhackathon.trashformer.R;
import com.bbhackathon.trashformer.entity.CameraResultEntity;
import com.bbhackathon.trashformer.type.ResultType;

import java.util.ArrayList;

import static com.bbhackathon.trashformer.util.ExtraKey.INTENT_CAMERA_RESULT;

public class CameraResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_result);

        ArrayList<CameraResultEntity> entityList = getIntent().getParcelableArrayListExtra(INTENT_CAMERA_RESULT);

        ResultAnalysis resultAnalysis = new ResultAnalysis();

        ResultType resultType = resultAnalysis.doAnalysis(entityList);

        String cameraResult = "Result : " + resultType.getMemo() + "\n";

        for(CameraResultEntity entity: entityList){
            String text = entity.getText();
            float confident = entity.getConfidence();

            String result = "text:" + text +"\t\t\t" + "confident:" + confident + "\n";
            cameraResult = cameraResult + result;
        }

        TextView textView = findViewById(R.id.cameraResult);
        textView.setText(cameraResult);
    }
}
