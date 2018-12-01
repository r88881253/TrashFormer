package com.bbhackathon.trashformer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bbhackathon.trashformer.entity.CameraResultEntity;
import com.bbhackathon.trashformer.leaderboard.LeaderBoardActivity;
import com.bbhackathon.trashformer.login.BeforeLoginActivity;
import com.bbhackathon.trashformer.manager.FirebaseAuthManager;
import com.bbhackathon.trashformer.camera.CameraResultActivity;
import com.bbhackathon.trashformer.databinding.ActivityHomeBinding;
import com.bbhackathon.trashformer.equipment.EquipmentActivity;
import com.bbhackathon.trashformer.setting.SettingActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.cloud.FirebaseVisionCloudDetectorOptions;
import com.google.firebase.ml.vision.cloud.label.FirebaseVisionCloudLabel;
import com.google.firebase.ml.vision.cloud.label.FirebaseVisionCloudLabelDetector;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionLabelDetector;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.bbhackathon.trashformer.util.ExtraKey.INTENT_CAMERA_RESULT;

public class HomeActivity extends AppCompatActivity {
    private final String TAG = HomeActivity.class.getSimpleName();

    private final int REQUEST_CAMERA = 1001;
    private final int REQUEST_TAKE_PHOTO = 1002;
    private final int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = 1003;
    private ActivityHomeBinding mBinding;

    private String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        if (FirebaseAuthManager.getInstance() != null) {
            Toast.makeText(getBaseContext(), FirebaseAuthManager.getInstance().getUser().getEmail(),
                    Toast.LENGTH_SHORT).show();
        }


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
                Intent i = new Intent(HomeActivity.this, SettingActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
//        ObjectAnimator progressAnimator = ObjectAnimator.ofInt(findViewById(R.id.homeProgressBar), "progressbar", 100, 40);
//        progressAnimator.setDuration(3500);
//        progressAnimator.setInterpolator(new LinearInterpolator());
//        progressAnimator.start();
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
//                visionLabelDetectTask(source);
            }
        }

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

    private Bitmap getBitmapFromUri(Uri uri) {
        try {
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
            Log.e("[Android]", e.getMessage());
            Log.e("[Android]", "目录为：" + uri);
            e.printStackTrace();
            return null;
        }
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
                doTakePhoto();
            }
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
                    doTakePhoto();
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
                    doTakePhoto();
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

    //拍照，小尺寸
    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String f = System.currentTimeMillis() + ".jpg";
        Uri fileUri = Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory("").getPath() + f));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); //指定圖片存放位置，指定後，在onActivityResult裏得到的Data將為null
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);

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

    private void visionLabelDetectTask(Bitmap source) {
        FirebaseVisionLabelDetector detector = FirebaseVision.getInstance().getVisionLabelDetector();
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(source);

//         Or, to set the minimum confidence required:
//         FirebaseVisionLabelDetectorOptions options = new FirebaseVisionLabelDetectorOptions.Builder()
//                                .setConfidenceThreshold(0.8f)
//                                .build();
//        FirebaseVisionLabelDetector detector = FirebaseVision.getInstance()
//                .getVisionLabelDetector(options);

        Task<List<FirebaseVisionLabel>> result = detector.detectInImage(image)
                .addOnSuccessListener(
                        new OnSuccessListener<List<FirebaseVisionLabel>>() {
                            @Override
                            public void onSuccess(List<FirebaseVisionLabel> labels) {

                                Intent intent = new Intent(HomeActivity.this, CameraResultActivity.class);
                                intent.putParcelableArrayListExtra(INTENT_CAMERA_RESULT, (ArrayList) getVisionCameraResult(labels));
                                startActivity(intent);
                                // Task completed successfully
                                // ...
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
}
