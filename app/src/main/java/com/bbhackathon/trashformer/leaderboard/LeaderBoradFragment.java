package com.bbhackathon.trashformer.leaderboard;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bbhackathon.trashformer.R;
import com.bbhackathon.trashformer.entity.UserRankEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LeaderBoradFragment extends Fragment {
    private String TAG = LeaderBoradFragment.class.getSimpleName();

    private List<UserRankEntity> mList;
    private LeaderBoardAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.leaderboard_fragment, container, false);

        mList = new ArrayList<>();


        mAdapter = new LeaderBoardAdapter(getActivity(), mList, -1);
        RecyclerView mRecyclerView = rootView.findViewById(R.id.leaderboardRecyclerview);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

    public void setView(Map<Integer, List<UserRankEntity>> leaderboardResult) {
        List<UserRankEntity> result = new ArrayList<>();

        Set<Integer> keySet = leaderboardResult.keySet();
        Iterator<Integer> iter = keySet.iterator();
        while (iter.hasNext()) {
            Integer key = iter.next();

            List<UserRankEntity> sameLevelList = new ArrayList<>();
            for (UserRankEntity userRankEntity : leaderboardResult.get(key)) {
                if (leaderboardResult.get(key) != null) {
                    sameLevelList.add(new UserRankEntity(userRankEntity.getName(), userRankEntity.getMonsterName(), key, userRankEntity.getExp(), userRankEntity.getUid()));
                }
            }

            // Sorting
            Collections.sort(sameLevelList, new Comparator<UserRankEntity>() {
                @Override
                public int compare(UserRankEntity userRankEntity1, UserRankEntity userRankEntity2) {
                    Integer user1Exp = userRankEntity1.getExp();
                    Integer user2Exp = userRankEntity2.getExp();
                    // 降序排序
                    return user2Exp.compareTo(user1Exp);
                }
            });

            for (UserRankEntity userRankEntity : sameLevelList) {
                result.add(userRankEntity);
            }

            Log.d(TAG, key + ":" + leaderboardResult.get(key).toString());
        }
        updateData(result);
    }

    private void updateData(List<UserRankEntity> list) {
        if(mList != null){
            mList.clear();
            mList.addAll(list);
        }
        mAdapter.notifyDataSetChanged(); //刷新
    }
}
