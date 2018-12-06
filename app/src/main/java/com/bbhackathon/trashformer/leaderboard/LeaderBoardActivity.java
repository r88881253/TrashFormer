package com.bbhackathon.trashformer.leaderboard;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.bbhackathon.trashformer.R;
import com.bbhackathon.trashformer.base.BaseActivity;
import com.bbhackathon.trashformer.entity.UserEntity;
import com.bbhackathon.trashformer.entity.UserProfileTable;
import com.bbhackathon.trashformer.equipment.EquipmentViewPager;
import com.bbhackathon.trashformer.manager.FirebaseAuthManager;
import com.bbhackathon.trashformer.manager.FirebaseDatabaseManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeaderBoardActivity extends BaseActivity {

    private String TAG = LeaderBoardActivity.class.getSimpleName();

    private EquipmentViewPager mViewPager;
    private TabLayout mTabLayout;
    public static int lastPosition = 0;

    LeaderBoradFragment leaderBoradFragment;
    RecyclerSummaryFragment recyclerSummaryFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);


        initView();
        selectUserTableData();
        setStatusBar(R.color.leaderboard_background_F04848);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView() {
        leaderBoradFragment = new LeaderBoradFragment();
        recyclerSummaryFragment =  new RecyclerSummaryFragment();

        mViewPager = (EquipmentViewPager) findViewById(R.id.leaderboard_viewPager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                lastPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }

        });
        mViewPager.setEnableSwipe(true);

        setupViewPager(mViewPager);

        mTabLayout = (TabLayout) findViewById(R.id.leaderboard_tabLayout);
        mTabLayout.setupWithViewPager(mViewPager);
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(leaderBoradFragment, "排行榜");
        adapter.addFragment(recyclerSummaryFragment, "回收統計");

        viewPager.setAdapter(adapter);
    }

    private void selectUserTableData() {
        FirebaseDatabaseManager.getInstance().selectUserTable(new SelectUserTableListener());
//        FirebaseDatabaseManager.getInstance().selectUserProfileTable(FirebaseAuthManager.getInstance().getUid(), new SelectUserDataListener());
    }

    class SelectUserTableListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Map<String, Object> userTableMap = (HashMap<String, Object>) dataSnapshot.getValue();
            Log.d(TAG, userTableMap.toString());

            Gson gson = new Gson();
            JsonElement jsonElement = gson.toJsonTree(userTableMap.get(FirebaseAuthManager.getInstance().getUid()));
            UserEntity userEntity = gson.fromJson(jsonElement, UserEntity.class);

            UserProfileTable userProfileTable = userEntity.getUserProfileTable() ;
            if(userProfileTable != null){
                recyclerSummaryFragment.setRecycleAmount(userProfileTable);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    }

    class SelectUserDataListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            UserProfileTable userProfile = dataSnapshot.getValue(UserProfileTable.class);
            if(userProfile != null){
                recyclerSummaryFragment.setRecycleAmount(userProfile);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.d(TAG, "The read failed: " + databaseError.getCode());
        }
    }

    private void setView(){


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
