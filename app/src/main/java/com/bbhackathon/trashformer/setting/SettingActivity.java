package com.bbhackathon.trashformer.setting;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

import com.bbhackathon.trashformer.R;
import com.bbhackathon.trashformer.base.BaseActivity;
import com.bbhackathon.trashformer.databinding.ActivitySettingBinding;
import com.bbhackathon.trashformer.entity.UserProfileTable;
import com.bbhackathon.trashformer.manager.FirebaseAuthManager;
import com.bbhackathon.trashformer.manager.FirebaseDatabaseManager;
import com.bbhackathon.trashformer.manager.LoginManager;
import com.bbhackathon.trashformer.setting.logout.LogoutDialog;
import com.bbhackathon.trashformer.type.ResultType;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.HashMap;
import java.util.Map;

public class SettingActivity extends BaseActivity {
    private String TAG = SettingActivity.class.getSimpleName();

    private ActivitySettingBinding mBinding;
    String originalNickName;

    //0:無 1:寶特瓶 2:鐵鋁罐 3:電池 4:玻璃瓶
    private ResultType selectCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_setting);

        initView();
        initListener();
        hideKeyboard();
        setStatusBar(R.color.btn_login_background_806EE6);
    }

    private void initView() {
        String recycleCategory = LoginManager.getInstance(SettingActivity.this).getRecycleCategory();
        if (recycleCategory != "") {
            check(ResultType.getResultType(recycleCategory));
        }
        FirebaseDatabaseManager.getInstance().selectUserProfileTable(FirebaseAuthManager.getInstance().getUid(), new SelectUserDataListener());
    }

    private void initListener() {
        mBinding.dialogSetting.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogoutDialog dialog = new LogoutDialog();
                dialog.show(getSupportFragmentManager(), "dialog");
            }
        });

        mBinding.dialogSetting.recyclerItemLinearLayout.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.btnBottle: //case mRadioButton0.getId():
                        LoginManager.getInstance(SettingActivity.this).setRecycleCategory(ResultType.BOTTLE.getMemo());
                        break;

                    case R.id.btnCan: //case mRadioButton1.getId():
                        LoginManager.getInstance(SettingActivity.this).setRecycleCategory(ResultType.CAN.getMemo());
                        break;

                    case R.id.btnBattery: //case mRadioButton2.getId():
                        LoginManager.getInstance(SettingActivity.this).setRecycleCategory(ResultType.BATTERY.getMemo());
                        break;

                    case R.id.btnGlass: //case mRadioButton3.getId():
                        LoginManager.getInstance(SettingActivity.this).setRecycleCategory(ResultType.GLASS.getMemo());
                        break;
                }
            }
        });
    }

    //0:無 1:寶特瓶 2:鐵鋁罐 3:電池 4:玻璃瓶
    private void check(ResultType category) {
        switch (category) {
            case BOTTLE:
                selectCategory = ResultType.BOTTLE;
                mBinding.dialogSetting.btnBottle.setChecked(true);
                break;
            case CAN:
                selectCategory = ResultType.CAN;
                mBinding.dialogSetting.btnCan.setChecked(true);
                break;
            case BATTERY:
                selectCategory = ResultType.BATTERY;
                mBinding.dialogSetting.btnBattery.setChecked(true);
                break;
            case GLASS:
                selectCategory = ResultType.GLASS;
                mBinding.dialogSetting.btnGlass.setChecked(true);
                break;
            case UNKNOWN:
            default:
                break;
        }
    }

    class SelectUserDataListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            UserProfileTable userProfile = dataSnapshot.getValue(UserProfileTable.class);
            if (userProfile != null) {
                Log.d(TAG, userProfile.toString());
                if (userProfile.getNickName() != null) {
                    originalNickName = userProfile.getNickName();
                    mBinding.dialogSetting.nickNameEditText.setText(userProfile.getNickName());
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.d(TAG, "The read failed: " + databaseError.getCode());
        }
    }

    @Override
    public void onBackPressed() {
        if (mBinding.dialogSetting.nickNameEditText.getText() != null) {
            String nickName = mBinding.dialogSetting.nickNameEditText.getText().toString();

            if (nickName != originalNickName) {
                updateNickName(nickName);
            }
        }
        super.onBackPressed();
    }

    private void updateNickName(String nickname) {
        updateNickNameTask(nickname)
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        dismissProgressDialog();
                        if (!task.isSuccessful()) {
                            Exception e = task.getException();
                            if (e instanceof FirebaseFunctionsException) {
                                FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                                FirebaseFunctionsException.Code code = ffe.getCode();
                                Object details = ffe.getDetails();
                            }
                            Log.d("updateNickNameTask: ", "failed");
                        } else {
                            Log.d("updateNickNameTask: ", "successed");
                        }
                    }
                });

    }


    private Task<String> updateNickNameTask(String nickName) {
        FirebaseFunctions mFunctions;
        mFunctions = FirebaseFunctions.getInstance();

        // Create the arguments to the callable function.
        Map<String, Object> data = new HashMap<>();
        data.put("nickName", nickName);

        return mFunctions
                .getHttpsCallable("updateNickName")
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
