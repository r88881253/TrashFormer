package com.bbhackathon.trashformer.equipment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.bbhackathon.trashformer.R;
import com.bbhackathon.trashformer.base.BaseActivity;
import com.bbhackathon.trashformer.databinding.ActivityEquipmentBinding;
import com.bbhackathon.trashformer.entity.EquipmentEntity;
import com.bbhackathon.trashformer.manager.LoginManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EquipmentActivity extends BaseActivity implements EquipmentAdapterCallback{

    private EquipmentViewPager mViewPager;
    private TabLayout mTabLayout;
    public static int lastPosition = 0;
    private ActivityEquipmentBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_equipment);

        mViewPager = (EquipmentViewPager) findViewById(R.id.equipment_viewPager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                lastPosition = position;
            }
            @Override
            public void onPageScrollStateChanged(int state) {}

        });
        mViewPager.setEnableSwipe(true);

        setupViewPager(mViewPager);

        mTabLayout = (TabLayout) findViewById(R.id.equipment_tabLayout);
        mTabLayout.setupWithViewPager(mViewPager);

        setStatusBar(R.color.btn_login_background_806EE6);

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

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
//        adapter.addFragment(new EquipmentFragment(), "背景");


        ArrayList<EquipmentEntity> headList = new ArrayList<>();
        ArrayList<EquipmentEntity> leftHandList = new ArrayList<>();
        ArrayList<EquipmentEntity> rightHandList = new ArrayList<>();
        ArrayList<EquipmentEntity> feetList = new ArrayList<>();


        Map<String, EquipmentEntity> userEquipMap = LoginManager.getInstance(this).getEquipment();
        for(String key: userEquipMap.keySet()){
            if(key.contains("equipment_h")){
                headList.add(userEquipMap.get(key));
            } else if(key.contains("equipment_r")){
                rightHandList.add(userEquipMap.get(key));
            } else if(key.contains("equipment_l")){
                leftHandList.add(userEquipMap.get(key));
            } else if(key.contains("equipment_f")){
                feetList.add(userEquipMap.get(key));
            }
        }

        EquipmentFragment headEquipmentFragment = EquipmentFragment.newInstance(headList, EquipType.HEAD);
        EquipmentFragment leftHandEquipmentFragment = EquipmentFragment.newInstance(leftHandList, EquipType.LEFT_HAND);
        EquipmentFragment rightHaneEquipmentFragment = EquipmentFragment.newInstance(rightHandList, EquipType.RIGHT_HAND);
        EquipmentFragment feetEquipmentFragment = EquipmentFragment.newInstance(feetList, EquipType.FEET);

        adapter.addFragment(headEquipmentFragment, "頭");
        adapter.addFragment(leftHandEquipmentFragment, "左手");
        adapter.addFragment(rightHaneEquipmentFragment, "右手");
        adapter.addFragment(feetEquipmentFragment, "腳");

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void setEquipment(){
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
}
