package com.bbhackathon.trashformer.equipment;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bbhackathon.trashformer.R;
import com.bbhackathon.trashformer.entity.EquipmentEntity;
import com.bbhackathon.trashformer.manager.LoginManager;

import java.util.List;

public class EquipmentAdapter extends RecyclerView.Adapter<EquipmentAdapter.ViewHolder> {
    private List<EquipmentEntity> mData;
    private Context mContext;
    private EquipmentAdapterCallback equipmentAdapterCallback;

    public EquipmentAdapter(Context context, List<EquipmentEntity> data) {
        this.mContext = context;
        mData = data;

        try {
            this.equipmentAdapterCallback = ((EquipmentAdapterCallback) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement AdapterCallback.");
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.equipment_recyclerview, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if (getDrawable(mData.get(position).getDrawableName()) != null) {
            holder.mImageView.setImageDrawable(getDrawable(mData.get(position).getDrawableName()));
//            holder.mRadioButton.setButtonDrawable(getDrawable(mData.get(position).getDrawableName()));
        }

        if (mData.get(position).isEquipStatus()) {
            holder.mLinearLayout.setBackground(mContext.getResources().getDrawable(R.drawable.equipment_pressed_shape));
        } else {
            holder.mLinearLayout.setBackground(mContext.getResources().getDrawable(R.drawable.equipment_default_shape));
        }

        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!mData.get(position).isEquipStatus()) {

                    for (int i = 0; i < mData.size(); i++) {
                        if (mData.get(i).isEquipStatus()) {

                            mData.get(i).setEquipStatus(false);
                        }
                    }
                    LoginManager.getInstance(mContext).setEquipmentEquip(mData.get(position).getDrawableName());
                    mData.get(position).setEquipStatus(true);

                    notifyDataSetChanged();
                    equipmentAdapterCallback.setEquipment();
                }
//                Toast.makeText(mContext, "Item " + position + " is clicked.", Toast.LENGTH_SHORT).show();
//                if(holder.mRadioButton.isChecked()){
//                    Toast.makeText(mContext, "Item " + position + " is checked.", Toast.LENGTH_SHORT).show();
//                } else{
//                    Toast.makeText(mContext, "Item " + position + " is not checked.", Toast.LENGTH_SHORT).show();
//                }
            }
        });
//        holder.mRadioButton.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                Toast.makeText(mContext, "Item " + position + " is long clicked.", Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        });


//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(mContext, "Item " + position + " is clicked.", Toast.LENGTH_SHORT).show();
//            }
//        });
//        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                Toast.makeText(mContext, "Item " + position + " is long clicked.", Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        //        public RadioButton mRadioButton;
        public LinearLayout mLinearLayout;
        public ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.equipmentImageview);
            mLinearLayout = (LinearLayout) itemView.findViewById(R.id.equipmentLinearLayout);
//            mRadioButton = (RadioButton) itemView.findViewById(R.id.equipmentRadioButton);
        }
    }

    private Drawable getDrawable(String resourceName) {
        Resources resources = mContext.getResources();
        final int resourceId = resources.getIdentifier(resourceName, "drawable",
                mContext.getPackageName());
        return resources.getDrawable(resourceId);
    }
}
