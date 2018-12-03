package com.bbhackathon.trashformer.equipment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.bbhackathon.trashformer.R;
import com.bbhackathon.trashformer.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class EquipmentActivity extends BaseActivity {

    private EquipmentViewPager mViewPager;
    private TabLayout mTabLayout;
    public static int lastPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment);

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
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new EquipmentFragment(), "背景");
        adapter.addFragment(new EquipmentFragment(), "頭");
        adapter.addFragment(new EquipmentFragment(), "左手");
        adapter.addFragment(new EquipmentFragment(), "右手");
        adapter.addFragment(new EquipmentFragment(), "腳");

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
}
