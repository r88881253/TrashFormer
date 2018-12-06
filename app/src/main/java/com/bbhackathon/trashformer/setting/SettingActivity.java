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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class SettingActivity extends BaseActivity {
    private String TAG = SettingActivity.class.getSimpleName();

    private ActivitySettingBinding mBinding;

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
        String recycleCategory = LoginManager.getInstance().getRecycleCategory();
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

//
//        mBinding.dialogSetting.btnBottle.setOnCheckedChangeListener(new CheckListener());
//        mBinding.dialogSetting.btnCan.setOnCheckedChangeListener(new CheckListener());
//        mBinding.dialogSetting.btnBattery.setOnCheckedChangeListener(new CheckListener());
//        mBinding.dialogSetting.btnGlass.setOnCheckedChangeListener(new CheckListener());

        mBinding.dialogSetting.recyclerItemLinearLayout.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch (checkedId)
                {
                    case R.id.btnBottle: //case mRadioButton0.getId():
                        LoginManager.getInstance().setRecycleCategory(ResultType.BOTTLE.getMemo());
                        break;

                    case R.id.btnCan: //case mRadioButton1.getId():
                        LoginManager.getInstance().setRecycleCategory(ResultType.CAN.getMemo());
                        break;

                    case R.id.btnBattery: //case mRadioButton2.getId():
                        LoginManager.getInstance().setRecycleCategory(ResultType.BATTERY.getMemo());
                        break;

                    case R.id.btnGlass: //case mRadioButton3.getId():
                        LoginManager.getInstance().setRecycleCategory(ResultType.GLASS.getMemo());
                        break;
                }
            }
        });
    }

    //0:無 1:寶特瓶 2:鐵鋁罐 3:電池 4:玻璃瓶
    private void check(ResultType category) {
        switch (category) {
            case BOTTLE:
                uncheckCheckbox();
                selectCategory = ResultType.BOTTLE;
                mBinding.dialogSetting.btnBottle.setChecked(true);
                break;
            case CAN:
                uncheckCheckbox();
                selectCategory = ResultType.CAN;
                mBinding.dialogSetting.btnCan.setChecked(true);
                break;
            case BATTERY:
                uncheckCheckbox();
                selectCategory = ResultType.BATTERY;
                mBinding.dialogSetting.btnBattery.setChecked(true);
                break;
            case GLASS:
                uncheckCheckbox();
                selectCategory = ResultType.GLASS;
                mBinding.dialogSetting.btnGlass.setChecked(true);
                break;
            case UNKNOWN:
            default:
                uncheckCheckbox();
                break;
        }
    }

    private void uncheckCheckbox() {
//        mBinding.dialogSetting.btnBottle.setChecked(false);
//        mBinding.dialogSetting.btnCan.setChecked(false);
//        mBinding.dialogSetting.btnBattery.setChecked(false);
//        mBinding.dialogSetting.btnGlass.setChecked(false);
    }

//    class CheckListener implements CompoundButton.OnCheckedChangeListener{
//        @Override
//        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//            if(isChecked){
//                uncheckCheckbox();
//                compoundButton.setChecked(true);
//            } else{
//                compoundButton.setChecked(true);
//            }
//        }
//    }

    class SelectUserDataListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            UserProfileTable userProfile = dataSnapshot.getValue(UserProfileTable.class);
            if (userProfile != null) {
                Log.d(TAG, userProfile.toString());
                if (userProfile.getNickName() != null) {
                    mBinding.dialogSetting.nickNameEditText.setText(userProfile.getNickName());
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.d(TAG, "The read failed: " + databaseError.getCode());
        }
    }
}
