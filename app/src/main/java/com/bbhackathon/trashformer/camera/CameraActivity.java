package com.bbhackathon.trashformer.camera;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bbhackathon.trashformer.BuildConfig;
import com.bbhackathon.trashformer.R;
import com.bbhackathon.trashformer.base.BaseActivity;
import com.bbhackathon.trashformer.entity.CameraResultEntity;
import com.bbhackathon.trashformer.manager.FirebaseDatabaseManager;
import com.bbhackathon.trashformer.manager.LoginManager;
import com.bbhackathon.trashformer.type.ResultType;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CameraActivity extends BaseActivity {

    private String TAG = CameraActivity.class.getSimpleName();

    private final int REQUEST_CAMERA = 1001;
    private final int REQUEST_TAKE_PHOTO = 1002;
    private final int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = 1003;

    private String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        setStatusBar(R.color.leaderboard_background_F04848);
        ((RelativeLayout) findViewById(R.id.cameraRelativeLayout)).setVisibility(View.INVISIBLE);
        initListener();
        dispatchTakePictureIntent();
    }

    private void initListener() {
        ((Button) findViewById(R.id.btnKeepRecognize)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        ((Button) findViewById(R.id.btnBackHome)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO) {
            if (resultCode == RESULT_OK) {
                showProgressDialog();
                showPic();
                FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(getBitmapFromUri());
//                Bitmap source = BitmapFactory.decodeResource(this.getResources(), R.drawable.can2);
//                FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(source);
                cloudLabelDetectTask(image);
            } else {
                onBackPressed();
            }
        }
    }

    class selectTipsTableListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            List<String> tipsTableList = (ArrayList<String>) dataSnapshot.getValue();
            Log.d(TAG, tipsTableList.toString());

            int randomInt = new Random().nextInt(tipsTableList.size());

            if (tipsTableList.get(randomInt) != null) {
                ((TextView) findViewById(R.id.camera_result_tips)).setText(tipsTableList.get(randomInt));
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        ((RelativeLayout) findViewById(R.id.cameraRelativeLayout)).setVisibility(View.INVISIBLE);
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private Bitmap getBitmapFromUri() {
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        try {
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentUri);
            return bitmap;
        } catch (Exception e) {
            Log.e("[Android]", e.getMessage());
            Log.e("[Android]", "目录为：" + contentUri);
            e.printStackTrace();
            return null;
        }
    }

    private void showPic() {
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);

        Bitmap bitmap = null;
        try {
            // 读取uri所在的图片
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentUri);
            ((ImageView) findViewById(R.id.testImg)).setImageBitmap(bitmap);
        } catch (Exception e) {
            Log.e("[Android]", e.getMessage());
            Log.e("[Android]", "目录为：" + contentUri);
            e.printStackTrace();
        }
    }


    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // The request code used in ActivityCompat.requestPermissions()
            // and returned in the Activity's onRequestPermissionsResult()
            String[] PERMISSIONS = {
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.CAMERA
            };

            if (!hasPermissions(this, PERMISSIONS)) {
                ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_CAMERA);
            } else {
                doTakePhoto();
            }
        } else {
            doTakePhoto();
        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            //檢查是否取得權限
//            int cameraPermissionCheck = ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.CAMERA);
//            int storagePermissionCheck = ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//            //沒有權限時
//            if (storagePermissionCheck != PackageManager.PERMISSION_GRANTED || cameraPermissionCheck != PackageManager.PERMISSION_GRANTED) {
//
//                ActivityCompat.requestPermissions(CameraActivity.this,
//                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                        REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION);
//            } else {
//                doTakePhoto();
//            }
//        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "已經拿到CAMERA權限囉!");
                    doTakePhoto();
                }
                //假如拒絕了
                else {
                    Log.d(TAG, "CAMERA權限FAIL");
                }
                break;
            case REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "已經拿到STORAGE權限囉!");
                    doTakePhoto();
                }
                //假如拒絕了
                else {
                    Log.d(TAG, "STORAGE權限FAIL");
                }
                break;

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 拍照新方法[全尺寸]
     */
    private void doTakePhoto() {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePhotoIntent.resolveActivity(getPackageManager()) != null) {
            File newFile = createTakePhotoFile();
            Uri contentUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", newFile);
            Log.i(TAG, "contentUri = " + contentUri.toString());
            List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(takePhotoIntent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                grantUriPermission(packageName, contentUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
            startActivityForResult(takePhotoIntent, REQUEST_TAKE_PHOTO);
        }
    }

    /**
     * @return 拍照之後儲存的檔案
     */
    @NonNull
    private File createTakePhotoFile() {
        File imagePath = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "take_photo");
        if (!imagePath.exists()) {
            imagePath.mkdirs();
        }
        File file = new File(imagePath, "default_image.jpg");
        mCurrentPhotoPath = file.getPath();// 儲存拍照的路徑
        return file;
    }

    private void cloudLabelDetectTask(FirebaseVisionImage image) {
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

                                        ResultType resultType = new ResultAnalysis().doAnalysis((ArrayList) getCloudCameraResult(labels));

                                        SpannableString span;
                                        Boolean isRightRecycleItem;
                                        TextView tipsTitle = (TextView) findViewById(R.id.tipsTitle);
                                        LinearLayout successTipsLinearLayout = (LinearLayout) findViewById(R.id.successTipsLinearLayout);
                                        ImageView successTipsImage = (ImageView) findViewById(R.id.successTipsImage);
                                        if (resultType == ResultType.getResultType(LoginManager.getInstance(getBaseContext()).getRecycleCategory())) {
                                            span = new SpannableString(getString(R.string.camera_result_success_string) + resultType.getMemo() + "！");
                                            ((TextView) findViewById(R.id.camera_result_exp)).setText(getString(R.string.camera_result_exp) + " +50");
                                            ((TextView) findViewById(R.id.tipsTitle)).setText(getString(R.string.follow_me));
                                            isRightRecycleItem = true;

                                            switch (resultType) {
                                                case CAN:
                                                    tipsTitle.setText(getString(R.string.can_tips));
                                                    successTipsImage.setImageResource(R.drawable.instruction_can);
                                                    break;
                                                default:
                                                    tipsTitle.setText(getString(R.string.can_tips));
                                                    successTipsImage.setImageResource(R.drawable.instruction_can);
                                                    break;
                                            }
                                            successTipsLinearLayout.setVisibility(View.VISIBLE);

                                        } else {
                                            successTipsLinearLayout.setVisibility(View.GONE);
                                            FirebaseDatabaseManager.getInstance().selectTipsTable(new selectTipsTableListener());
                                            tipsTitle.setText(getString(R.string.you_know));
                                            span = new SpannableString(getString(R.string.camera_result_failed_string) + resultType.getMemo() + "！");
                                            ((TextView) findViewById(R.id.camera_result_exp)).setText(getString(R.string.camera_result_exp) + " -20 愛心 -0.5");
                                            isRightRecycleItem = false;
                                        }

                                        span.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.inputText_4D555B)), 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        span.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.btn_login_background_806EE6)), 6, span.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        span.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.inputText_4D555B)), span.length(), span.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        ((TextView) findViewById(R.id.camera_result_type)).setText(span);

                                        updateExp(resultType.getMemo(), isRightRecycleItem);

                                        ((RelativeLayout) findViewById(R.id.cameraRelativeLayout)).setVisibility(View.VISIBLE);

                                        String cameraResult = "Result : " + resultType.getMemo() + "\n";

                                        for (CameraResultEntity entity : (ArrayList<CameraResultEntity>) getCloudCameraResult(labels)) {
                                            String text = entity.getText();
                                            float confident = entity.getConfidence();

                                            String result = "text:" + text + "\t\t\t" + "confident:" + confident + "\n";
                                            cameraResult = cameraResult + result;
                                        }


                                        ((TextView) findViewById(R.id.testTextview)).setText(cameraResult);
                                        dismissProgressDialog();
                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Task failed with an exception
                                        Log.d(TAG, "cloudLabelDetectTask failed");
                                        dismissProgressDialog();
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

    private void updateExp(String recycleItem, Boolean isRightRecycleItem) {
        updateExpToDatabaseTask(recycleItem, isRightRecycleItem)
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
                            Log.d("updateExpTask: ", "failed");
                            AlertDialog.Builder builder = new AlertDialog.Builder(CameraActivity.this);
                            builder.setTitle(getString(R.string.app_name))
                                    .setMessage(getString(R.string.something_failed))
                                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                        } else {
                            Log.d("updateExpTask: ", "successed");
                        }
                    }
                });

    }

    private Task<String> updateExpToDatabaseTask(String recycleItem, Boolean isRightRecycleItem) {
        FirebaseFunctions mFunctions;
        mFunctions = FirebaseFunctions.getInstance();

        // Create the arguments to the callable function.
        Map<String, Object> data = new HashMap<>();
        data.put("recycleItem", recycleItem);
        data.put("isRightRecycleItem", isRightRecycleItem);

        return mFunctions
                .getHttpsCallable("updateExp")
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
}
