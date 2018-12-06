package com.bbhackathon.trashformer.chooseRecycleCategory;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.bbhackathon.trashformer.HomeActivity;
import com.bbhackathon.trashformer.R;
import com.bbhackathon.trashformer.base.BaseActivity;
import com.bbhackathon.trashformer.databinding.ActivityChooseRecycleCategoryBinding;
import com.bbhackathon.trashformer.manager.FirebaseAuthManager;
import com.bbhackathon.trashformer.manager.LoginManager;
import com.bbhackathon.trashformer.type.ResultType;

public class ChooseRecycleCategoryActivity extends BaseActivity {

    private ActivityChooseRecycleCategoryBinding binding;

    //0:無 1:寶特瓶 2:鐵鋁罐 3:電池 4:玻璃瓶
    private ResultType selectCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_choose_recycle_category);

        initView();
        initListener();
        setStatusBar(R.color.yellow_background_F6C946);
    }

    private void initView() {
        uncheckLinearLayout();

        String recycleCategory = LoginManager.getInstance().getRecycleCategory();
        if (recycleCategory != "") {
            check(ResultType.getResultType(recycleCategory));
        }
    }

    private void initListener() {

        binding.selectBottleLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectCategory = ResultType.BOTTLE;
                check(selectCategory);
            }
        });

        binding.selectCanLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectCategory = ResultType.CAN;
                check(selectCategory);
            }
        });

        binding.selectBatteryLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectCategory = ResultType.BATTERY;
                check(selectCategory);
            }
        });

        binding.selectGlassLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectCategory = ResultType.GLASS;
                check(selectCategory);
            }
        });


        binding.btnChooseRecycleCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectCategory != null && selectCategory != ResultType.UNKNOWN){
                    LoginManager.getInstance().setRecycleCategory(selectCategory.getMemo());
                    startActivity(new Intent(ChooseRecycleCategoryActivity.this, HomeActivity.class));
                }
            }
        });
    }

    //0:無 1:寶特瓶 2:鐵鋁罐 3:電池 4:玻璃瓶
    private void check(ResultType category) {
        selectCategory = category;
        switch (category) {
            case BOTTLE:
                uncheckLinearLayout();
                binding.selectBottleLinearLayout.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.dialog_pressed_shape));
                break;
            case CAN:
                uncheckLinearLayout();
                binding.selectCanLinearLayout.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.dialog_pressed_shape));
                break;
            case BATTERY:
                uncheckLinearLayout();
                binding.selectBatteryLinearLayout.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.dialog_pressed_shape));
                break;
            case GLASS:
                uncheckLinearLayout();
                binding.selectGlassLinearLayout.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.dialog_pressed_shape));
                break;
            case UNKNOWN:
            default:
                uncheckLinearLayout();
                break;
        }
    }

    private void uncheckLinearLayout() {
        binding.selectBottleLinearLayout.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.dialog_default_shape));
        binding.selectCanLinearLayout.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.dialog_default_shape));
        binding.selectBatteryLinearLayout.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.dialog_default_shape));
        binding.selectGlassLinearLayout.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.dialog_default_shape));
    }

    @Override
    public void onBackPressed() {
        FirebaseAuthManager.getInstance().logout();
        super.onBackPressed();
    }
}
