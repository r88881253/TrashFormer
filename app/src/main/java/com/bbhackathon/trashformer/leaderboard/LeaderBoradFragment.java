package com.bbhackathon.trashformer.leaderboard;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bbhackathon.trashformer.R;
import com.bbhackathon.trashformer.equipment.EquipmentAdapter;

import java.util.ArrayList;
import java.util.List;

public class LeaderBoradFragment extends Fragment {

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

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add("123");
        }


        LeaderBoardAdapter mAdapter = new LeaderBoardAdapter(getActivity(), list);
        RecyclerView mRecyclerView =rootView.findViewById(R.id.leaderboardRecyclerview);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }
}
