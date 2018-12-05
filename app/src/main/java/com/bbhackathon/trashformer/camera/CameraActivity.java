package com.bbhackathon.trashformer.camera;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.bbhackathon.trashformer.BuildConfig;
import com.bbhackathon.trashformer.R;
import com.bbhackathon.trashformer.base.BaseActivity;
import com.bbhackathon.trashformer.entity.CameraResultEntity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.cloud.FirebaseVisionCloudDetectorOptions;
import com.google.firebase.ml.vision.cloud.label.FirebaseVisionCloudLabel;
import com.google.firebase.ml.vision.cloud.label.FirebaseVisionCloudLabelDetector;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.bbhackathon.trashformer.util.ExtraKey.INTENT_CAMERA_RESULT;

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO) {
            if (resultCode == RESULT_OK) {

//                取得小尺寸照片
//                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//                Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/image.jpg");

//                FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(getBitmapFromUri(Uri.parse(mCurrentPhotoPath)));
                Bitmap source = BitmapFactory.decodeResource(this.getResources(), R.drawable.can2);
                cloudLabelDetectTask(source);
            }
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
                                        Intent intent = new Intent(CameraActivity.this, CameraResultActivity.class);
                                        intent.putParcelableArrayListExtra(INTENT_CAMERA_RESULT, (ArrayList) getCloudCameraResult(labels));
                                        startActivity(intent);
                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Task failed with an exception
                                        Log.d(TAG, "cloudLabelDetectTask failed");
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
}
