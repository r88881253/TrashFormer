package com.bbhackathon.trashformer.equipment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bbhackathon.trashformer.R;

import java.util.ArrayList;
import java.util.List;

public class EquipmentFragment extends Fragment {



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.equipment_fragment, container, false);


        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("123");
        }


        EquipmentAdapter mAdapter = new EquipmentAdapter(getContext(), list);
        RecyclerView mRecyclerView = rootView.findViewById(R.id.equipmentRecyclerView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
//        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


        return rootView;
    }
}
