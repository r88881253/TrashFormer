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
import com.bbhackathon.trashformer.entity.EquipmentEntity;

import java.util.ArrayList;
import java.util.List;

public class EquipmentFragment extends Fragment {

    public final static String EQUIPMENT_LIST = "EQUIPMENT_LIST";
    public final static String EQUIPMENT_TYPE = "EQUIPMENT_TYPE";

    private List<EquipmentEntity> mList;
    private EquipmentAdapter mAdapter;
    private EquipType equipType;

    public EquipmentFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mList = getArguments().getParcelableArrayList(EQUIPMENT_LIST);
        equipType = getArguments().getParcelable(EQUIPMENT_TYPE);
    }

    public static EquipmentFragment newInstance(ArrayList<EquipmentEntity> mList, EquipType equipType) {
        EquipmentFragment equipmentFragment = new EquipmentFragment();

        Bundle args = new Bundle();
        args.putParcelableArrayList(EQUIPMENT_LIST, mList);
        args.putParcelable(EQUIPMENT_TYPE, equipType);

        equipmentFragment.setArguments(args);
        return equipmentFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.equipment_fragment, container, false);


        if(mList == null){
            mList = new ArrayList<>();
            switch (equipType){
                case HEAD:
                    mList.add(new EquipmentEntity("無", "equipment_h_empty", true));
                    break;
                case LEFT_HAND:
                    mList.add(new EquipmentEntity("無", "equipment_l_empty", true));
                    break;
                case RIGHT_HAND:
                    mList.add(new EquipmentEntity("無", "equipment_r_empty", true));
                    break;
                case FEET:
                    mList.add(new EquipmentEntity("無", "equipment_f_empty", true));
                    break;
            }
//            mList.add(new EquipmentEntity("聖誕帽", "equipment_h_xmas_hat", false));
        } else {
            boolean isEmpty = removeEmpty(mList);
            switch (equipType){
                case HEAD:
                    mList.add(0, new EquipmentEntity("無", "equipment_h_empty", isEmpty));
                    break;
                case LEFT_HAND:
                    mList.add(0, new EquipmentEntity("無", "equipment_l_empty", isEmpty));
                    break;
                case RIGHT_HAND:
                    mList.add(0, new EquipmentEntity("無", "equipment_r_empty", isEmpty));
                    break;
                case FEET:
                    mList.add(0, new EquipmentEntity("無", "equipment_f_empty", isEmpty));
                    break;
            }
        }

        mAdapter = new EquipmentAdapter(getActivity(), mList);
        RecyclerView mRecyclerView = rootView.findViewById(R.id.equipmentRecyclerView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
//        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


        return rootView;
    }

    private boolean removeEmpty(List<EquipmentEntity> mList) {

        boolean tempBoolean = true;
        for(int i = 0; i < mList.size(); i++){
            if("無".equals(mList.get(i).getName())){
                if(mList.get(i).isEquipStatus()){
                    tempBoolean = true;
                } else{
                    tempBoolean = false;
                }
                mList.remove(i);
                break;
            }
        }

        return tempBoolean;
    }

    private void updateData(List<EquipmentEntity> list) {
        if(mList != null){
            mList.clear();
            mList.addAll(list);
        }
        mAdapter.notifyDataSetChanged(); //刷新
    }
}
