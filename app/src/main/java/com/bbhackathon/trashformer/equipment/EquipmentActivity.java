package com.bbhackathon.trashformer.equipment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.bbhackathon.trashformer.R;

import java.util.ArrayList;
import java.util.List;

public class EquipmentActivity extends AppCompatActivity {

    private FCViewPager mViewPager;
    private TabLayout mTabLayout;
    public static int lastPosition = 0;
    private Button btnEnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment);

        btnEnable = (Button) findViewById(R.id.btnEnable);

        mViewPager = (FCViewPager) findViewById(R.id.container);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageSelected(int position) {
                lastPosition = position;
            }
            @Override
            public void onPageScrollStateChanged(int state) {}

        });
        mViewPager.setEnableSwipe(false);

        setupViewPager(mViewPager);

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);

        btnEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mViewPager.isEnableSwipe()){
                    mViewPager.setEnableSwipe(false);
                }else{
                    mViewPager.setEnableSwipe(true);
                }
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ViewFragment1(), "Title 1");
        adapter.addFragment(new ViewFragment2(), "Title 2");
        adapter.addFragment(new ViewFragment3(), "Title 3");
        adapter.addFragment(new ViewFragment2(), "Title 4");

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
