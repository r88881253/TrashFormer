package com.bbhackathon.trashformer;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bbhackathon.trashformer.base.BaseActivity;
import com.bbhackathon.trashformer.camera.CameraActivity;
import com.bbhackathon.trashformer.camera.CameraResultActivity;
import com.bbhackathon.trashformer.databinding.ActivityHomeBinding;
import com.bbhackathon.trashformer.entity.CameraResultEntity;
import com.bbhackathon.trashformer.entity.EquipmentEntity;
import com.bbhackathon.trashformer.entity.UserMissionEntity;
import com.bbhackathon.trashformer.entity.UserProfileTable;
import com.bbhackathon.trashformer.equipment.EquipmentActivity;
import com.bbhackathon.trashformer.equipment.drawable.DrawableName;
import com.bbhackathon.trashformer.leaderboard.LeaderBoardActivity;
import com.bbhackathon.trashformer.manager.FirebaseAuthManager;
import com.bbhackathon.trashformer.manager.FirebaseDatabaseManager;
import com.bbhackathon.trashformer.manager.LoginManager;
import com.bbhackathon.trashformer.present.PresentDialog;
import com.bbhackathon.trashformer.setting.password.PasswordDialog;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.cloud.FirebaseVisionCloudDetectorOptions;
import com.google.firebase.ml.vision.cloud.label.FirebaseVisionCloudLabel;
import com.google.firebase.ml.vision.cloud.label.FirebaseVisionCloudLabelDetector;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionLabel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bbhackathon.trashformer.util.ExtraKey.INTENT_CAMERA_RESULT;

public class HomeActivity extends BaseActivity {
    private final String TAG = HomeActivity.class.getSimpleName();

    private final int REQUEST_CAMERA = 1001;
    private final int REQUEST_TAKE_PHOTO = 1002;
    private final int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = 1003;
    private ActivityHomeBinding mBinding;


    private String monsterName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        if (FirebaseAuthManager.getInstance() != null) {
            Toast.makeText(getBaseContext(), FirebaseAuthManager.getInstance().getUser().getEmail(),
                    Toast.LENGTH_SHORT).show();
        }

        hideKeyboard();
        mBinding.homeRelativeLayout.setVisibility(View.INVISIBLE);
        showProgressDialog();
        initView();
        initListner();

        setStatusBar(R.color.btn_login_background_806EE6);
    }

    private void initView() {
        FirebaseDatabaseManager.getInstance().selectUserProfileTable(FirebaseAuthManager.getInstance().getUid(), new SelectUserDataListener());
        FirebaseDatabaseManager.getInstance().selectMissionTable(FirebaseAuthManager.getInstance().getUid(), new SelectMissionDataListener());
        getEquipment();
    }

    private void getEquipment() {
        Map<String, EquipmentEntity> equipmentEntityMap = LoginManager.getInstance(this).getEquipment();

        for(String key: equipmentEntityMap.keySet()){
            if(equipmentEntityMap.get(key).isEquipStatus()){
                if(key.contains("equipment_h")){
                    if(key.contains("empty")){
                        mBinding.avatarHead.setImageDrawable(getResources().getDrawable(R.drawable.monster_h_empty));
                    }else{
                        mBinding.avatarHead.setImageDrawable(getDrawable("monster_h_" + key.substring(12)));
                    }
                } else if(key.contains("equipment_r")){
                    if(key.contains("empty")){
                        mBinding.avatarRightHand.setImageDrawable(getResources().getDrawable(R.drawable.monster_r_normal));
                    }else{
                        mBinding.avatarRightHand.setImageDrawable(getDrawable("monster_r_" + key.substring(12)));
                    }
                } else if(key.contains("equipment_l")){
                    if(key.contains("empty")){
                        mBinding.avatarLeftHand.setImageDrawable(getResources().getDrawable(R.drawable.monster_l_normal));
                    }else{
                        mBinding.avatarLeftHand.setImageDrawable(getDrawable("monster_l_" + key.substring(12)));
                    }
                } else if(key.contains("equipment_f")){
                    if(key.contains("empty")){
                        mBinding.avatarFeet.setImageDrawable(getResources().getDrawable(R.drawable.monster_f_normal));
                    }else{
                        mBinding.avatarFeet.setImageDrawable(getDrawable("monster_f_" + key.substring(12)));
                    }
                }
            }
        }

    }

    private void initListner() {
        mBinding.btnEquipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, EquipmentActivity.class);
                startActivity(intent);
            }
        });


        mBinding.btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, LeaderBoardActivity.class);
                startActivity(i);
            }

        });


        mBinding.btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
            }

        });

        mBinding.btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PasswordDialog dialog = new PasswordDialog();
                dialog.show(getSupportFragmentManager(), "dialog");
            }
        });

        mBinding.mosterNameEditPen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBinding.monsterName.isEnabled()){
                    mBinding.monsterName.setEnabled(false);

                    Editable monsterNameEditable = mBinding.monsterName.getText();
                    if(monsterNameEditable != null && monsterNameEditable.toString() != monsterName){
                        updateMonsterName(monsterNameEditable.toString());
                        monsterName = monsterNameEditable.toString();
                    }
                } else {
                    mBinding.monsterName.setEnabled(true);
                }
            }
        });

        mBinding.missionImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, EquipmentEntity> currentMap = new HashMap<>(DrawableName.equipNameMap);

                for(String key: LoginManager.getInstance(HomeActivity.this).getEquipment().keySet()){
                    if(DrawableName.equipNameMap.containsKey(key)){
                        currentMap.remove(key);
                    }
                }

                if(currentMap.size() > 0){
                    mBinding.missionImageView.setClickable(false);
                    openMissionGift();
                    PresentDialog dialog = new PresentDialog();
                    dialog.setCancelable(false);
                    dialog.show(getSupportFragmentManager(), "dialog");
                }
                else{
                    showAlertDialog("已經沒有裝備可兌換了");
                }
            }
        });

        mBinding.giftImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, EquipmentEntity> currentMap = new HashMap<>(DrawableName.equipNameMap);

                for(String key: LoginManager.getInstance(HomeActivity.this).getEquipment().keySet()){
                    if(DrawableName.equipNameMap.containsKey(key)){
                        currentMap.remove(key);
                    }
                }
                if(currentMap.size() > 0){
                    mBinding.giftImageView.setClickable(false);
                    reduceGift();
                    PresentDialog dialog = new PresentDialog();
                    dialog.setCancelable(false);
                    dialog.show(getSupportFragmentManager(), "dialog");
                }
                else{
                    showAlertDialog("已經沒有裝備可兌換了");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
//        ObjectAnimator progressAnimator = ObjectAnimator.ofInt(findViewById(R.id.homeProgressBar), "progressbar", 100, 40);
//        progressAnimator.setDuration(3500);
//        progressAnimator.setInterpolator(new LinearInterpolator());
//        progressAnimator.start();
    }

    @Override
    protected void onPause() {
        mBinding.monsterName.setEnabled(false);
        super.onPause();
    }

    class SelectUserDataListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            UserProfileTable userProfile = dataSnapshot.getValue(UserProfileTable.class);
            if (userProfile != null) {
                Log.d(TAG, userProfile.toString());
                if (userProfile.getMonsterName() != null) {
                    mBinding.monsterName.setText(userProfile.getMonsterName());
                    monsterName = userProfile.getMonsterName();
                }
                if (userProfile.getLevel() != 0) {
                    mBinding.monsterLevel.setText(String.valueOf(userProfile.getLevel()));
                }
                mBinding.homeProgressBar.setProgress(Integer.valueOf(userProfile.getExp()));

                setHeartStatus(userProfile.getHeartStatus());

                mBinding.giftNumber.setText(String.valueOf(userProfile.getLevelGiftCount()));
                if (userProfile.getLevelGiftCount() != 0) {
                    mBinding.giftImageView.setImageResource(R.drawable.gift_box_active);
                    mBinding.giftImageView.setClickable(true);
                } else {
                    mBinding.giftImageView.setImageResource(R.drawable.gift_box);
                    mBinding.giftImageView.setClickable(false);
                }


                //            if(userProfile.getMissionGiftCount() != null){mBinding.missionNumber.setText(userProfile.getMissionGiftCount()/);}
            }
            mBinding.homeRelativeLayout.setVisibility(View.VISIBLE);
            dismissProgressDialog();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.d(TAG, "The read failed: " + databaseError.getCode());
            mBinding.homeRelativeLayout.setVisibility(View.VISIBLE);
            dismissProgressDialog();
        }
    }

    class SelectMissionDataListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            UserMissionEntity userMissionEntity = dataSnapshot.getValue(UserMissionEntity.class);
            if (userMissionEntity != null) {
                Log.d(TAG, userMissionEntity.toString());

                if (userMissionEntity.getRecycleType() != null) {
                    mBinding.missionType.setText(userMissionEntity.getRecycleType());
                }

                mBinding.missionCount.setText(userMissionEntity.getRecycleCount() + "/" + userMissionEntity.getRecycleAmt());
                if (userMissionEntity.getRecycleCount() >= userMissionEntity.getRecycleAmt()) {
                    mBinding.missionImageView.setImageResource(R.drawable.scroll_active);
                    mBinding.missionImageView.setClickable(true);
                } else {
                    mBinding.missionImageView.setImageResource(R.drawable.scroll);
                    mBinding.missionImageView.setClickable(false);
                }
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    }

    private void setHeartStatus(float heartCount) {
        initHeartStatus();
        if (heartCount != 0) {
            if (heartCount >= 0.5) {
                mBinding.heart1.setImageResource(R.drawable.heart_half);
            }
            if (heartCount >= 1) {
                mBinding.heart1.setImageResource(R.drawable.heart);
            }
            if (heartCount >= 1.5) {
                mBinding.heart2.setImageResource(R.drawable.heart_half);
            }
            if (heartCount >= 2) {
                mBinding.heart2.setImageResource(R.drawable.heart);
            }
            if (heartCount >= 2.5) {
                mBinding.heart3.setImageResource(R.drawable.heart_half);
            }
            if (heartCount >= 3) {
                mBinding.heart3.setImageResource(R.drawable.heart);
            }
            if (heartCount >= 3.5) {
                mBinding.heart4.setImageResource(R.drawable.heart_half);
            }
            if (heartCount >= 4) {
                mBinding.heart4.setImageResource(R.drawable.heart);
            }
            if (heartCount >= 4.5) {
                mBinding.heart5.setImageResource(R.drawable.heart_half);
            }
            if (heartCount >= 5) {
                mBinding.heart5.setImageResource(R.drawable.heart);
            }

        }

        if(heartCount <= 0){
            mBinding.monsterRelativeLayout.setVisibility(View.INVISIBLE);
        } else{
            mBinding.monsterRelativeLayout.setVisibility(View.VISIBLE);
        }
    }

    private void initHeartStatus() {
        mBinding.heart1.setImageResource(android.R.color.white);
        mBinding.heart2.setImageResource(android.R.color.white);
        mBinding.heart3.setImageResource(android.R.color.white);
        mBinding.heart4.setImageResource(android.R.color.white);
        mBinding.heart5.setImageResource(android.R.color.white);
    }

    private void cloudLabelDetectTask(Bitmap source) {
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(source);
        FirebaseVisionCloudDetectorOptions options =
                new FirebaseVisionCloudDetectorOptions.Builder()
                        .setModelType(FirebaseVisionCloudDetectorOptions.LATEST_MODEL)
                        .setMaxResults(15)
                        .build();
        FirebaseVisionCloudLabelDetector detector = FirebaseVision.getInstance()
                .getVisionCloudLabelDetector(options);


        Task<List<FirebaseVisionCloudLabel>> result =
                detector.detectInImage(image)
                        .addOnSuccessListener(
                                new OnSuccessListener<List<FirebaseVisionCloudLabel>>() {
                                    @Override
                                    public void onSuccess(List<FirebaseVisionCloudLabel> labels) {
                                        Intent intent = new Intent(HomeActivity.this, CameraResultActivity.class);
                                        intent.putParcelableArrayListExtra(INTENT_CAMERA_RESULT, (ArrayList) getCloudCameraResult(labels));
                                        startActivity(intent);
                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Task failed with an exception
                                        // ...
                                    }
                                });

    }

    private List<CameraResultEntity> getCloudCameraResult(List<FirebaseVisionCloudLabel> labels) {

        List<CameraResultEntity> entity = new ArrayList<>();
        for (FirebaseVisionCloudLabel label : labels) {
            entity.add(new CameraResultEntity(label.getLabel(), label.getEntityId(), label.getConfidence()));
        }

        return entity;
    }

    private List<CameraResultEntity> getVisionCameraResult(List<FirebaseVisionLabel> labels) {

        List<CameraResultEntity> entity = new ArrayList<>();
        for (FirebaseVisionLabel label : labels) {
            entity.add(new CameraResultEntity(label.getLabel(), label.getEntityId(), label.getConfidence()));
        }

        return entity;
    }

    public static Bitmap RotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }


    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //檢查是否取得權限
            int cameraPermissionCheck = ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.CAMERA);
            int storagePermissionCheck = ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            //沒有權限時
            if (storagePermissionCheck != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(HomeActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION);
            } else {
//                takePhoto();
//                doTakePhoto();
                startActivity(new Intent(HomeActivity.this, CameraActivity.class));
            }
        } else {
            startActivity(new Intent(HomeActivity.this, CameraActivity.class));
        }

    }

    /**
     * public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
     * 複寫請求授權後執行的方法
     * 第一個參數是請求代碼
     * 第二個參數是請求授權的名稱
     * 第三個參數是請求授權的結果，PERMISSION_GRANTED或PERMISSION_DENTED
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //這個"CUSTOM_NUMBER"就是上述的自訂意義的請求代碼
        // private static final int CUSTOM_NUMBER = 1;
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //do something
                    Toast.makeText(this, "已經拿到CAMERA權限囉!", Toast.LENGTH_SHORT).show();
//                    takePhoto();
//                    doTakePhoto();
                    startActivity(new Intent(HomeActivity.this, CameraActivity.class));
                }
                //假如拒絕了
                else {
                    //do something
                    Toast.makeText(this, "CAMERA權限FAIL", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //do something
                    Toast.makeText(this, "已經拿到STORAGE權限囉!", Toast.LENGTH_SHORT).show();
//                    takePhoto();
//                    doTakePhoto();
                    startActivity(new Intent(HomeActivity.this, CameraActivity.class));
                }
                //假如拒絕了
                else {
                    //do something
                    Toast.makeText(this, "STORAGE權限FAIL", Toast.LENGTH_SHORT).show();
                }
                break;

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void updateMonsterName(String monsterName) {
        updateMonsterNameTask(monsterName)
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Exception e = task.getException();
                            if (e instanceof FirebaseFunctionsException) {
                                FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                                FirebaseFunctionsException.Code code = ffe.getCode();
                                Object details = ffe.getDetails();
                            }
                            Log.d("updateMonsterNameTask: ", "failed");
                            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                            builder.setTitle(getString(R.string.app_name))
                                    .setMessage(getString(R.string.something_failed))
                                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                        } else {
                            Log.d("updateMonsterNameTask: ", "successed");
                        }
                    }
                });

    }

    private Task<String> updateMonsterNameTask(String monsterName) {
        FirebaseFunctions mFunctions;
        mFunctions = FirebaseFunctions.getInstance();

        // Create the arguments to the callable function.
        Map<String, Object> data = new HashMap<>();
        data.put("monsterName", monsterName);

        return mFunctions
                .getHttpsCallable("updateMonsterName")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        // This continuation runs on either success or failure, but if the task
                        // has failed then getResult() will throw an Exception which will be
                        // propagated down.
                        String result = (String) task.getResult().getData();
                        return result;
                    }
                });
    }


    private void openMissionGift() {
        openMissionGiftTask()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Exception e = task.getException();
                            if (e instanceof FirebaseFunctionsException) {
                                FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                                FirebaseFunctionsException.Code code = ffe.getCode();
                                Object details = ffe.getDetails();
                            }
                            Log.d("openMissionGiftTask: ", "failed");
                            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                            builder.setTitle(getString(R.string.app_name))
                                    .setMessage(getString(R.string.something_failed))
                                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                        } else {
                            Log.d("openMissionGiftTask: ", "successed");
                        }
                    }
                });

    }

    private Task<String> openMissionGiftTask() {
        FirebaseFunctions mFunctions;
        mFunctions = FirebaseFunctions.getInstance();

        // Create the arguments to the callable function.
        Map<String, Object> data = new HashMap<>();

        return mFunctions
                .getHttpsCallable("openMissionGift")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        // This continuation runs on either success or failure, but if the task
                        // has failed then getResult() will throw an Exception which will be
                        // propagated down.
                        String result = (String) task.getResult().getData();
                        return result;
                    }
                });
    }

    private void reduceGift() {
        reduceGiftTask()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Exception e = task.getException();
                            if (e instanceof FirebaseFunctionsException) {
                                FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                                FirebaseFunctionsException.Code code = ffe.getCode();
                                Object details = ffe.getDetails();
                            }
                            Log.d("reduceGiftTask: ", "failed");
                            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                            builder.setTitle(getString(R.string.app_name))
                                    .setMessage(getString(R.string.something_failed))
                                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                        } else {
                            Log.d("reduceGiftTask: ", "successed");
                        }
                    }
                });

    }

    private Task<String> reduceGiftTask() {
        FirebaseFunctions mFunctions;
        mFunctions = FirebaseFunctions.getInstance();

        // Create the arguments to the callable function.
        Map<String, Object> data = new HashMap<>();

        return mFunctions
                .getHttpsCallable("reduceGift")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        // This continuation runs on either success or failure, but if the task
                        // has failed then getResult() will throw an Exception which will be
                        // propagated down.
                        String result = (String) task.getResult().getData();
                        return result;
                    }
                });
    }

    /**
     * 讀取照片旋轉角度
     *
     * @param path 照片路徑
     * @return 角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋轉圖片
     *
     * @param angle  被旋轉角度
     * @param bitmap 圖片物件
     * @return 旋轉後的圖片
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        Bitmap returnBm = null;
// 根據旋轉角度，生成旋轉矩陣
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
// 將原始圖片按照旋轉矩陣進行旋轉，並得到新的圖片
            returnBm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bitmap;
        }
        if (bitmap != returnBm) {
            bitmap.recycle();
        }
        return returnBm;
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    private Drawable getDrawable(String resourceName) {
        Resources resources = this.getResources();
        final int resourceId = resources.getIdentifier(resourceName, "drawable",
                this.getPackageName());
        return resources.getDrawable(resourceId);
    }
}
