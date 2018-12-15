package com.bbhackathon.trashformer.equipment;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import com.bbhackathon.trashformer.R;

import java.util.List;

public class EquipmentAdapter extends RecyclerView.Adapter<EquipmentAdapter.ViewHolder> {
    private List<String> mData;
    private Context mContext;

    public EquipmentAdapter(Context context, List<String> data) {
        this.mContext = context;
        mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {

        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.equipment_recyclerview, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if(getDrawable(mData.get(position)) != null) {
//            holder.mImageView.setImageDrawable(getDrawable(mData.get(position)));
            holder.mRadioButton.setButtonDrawable(getDrawable(mData.get(position)));
        }

        holder.mRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext, "Item " + position + " is clicked.", Toast.LENGTH_SHORT).show();
                if(holder.mRadioButton.isChecked()){
                    Toast.makeText(mContext, "Item " + position + " is checked.", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(mContext, "Item " + position + " is not checked.", Toast.LENGTH_SHORT).show();
                }
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

        public RadioButton mRadioButton;

//        public ImageView mImageView;
        public ViewHolder(View itemView) {
            super(itemView);
//            mImageView = (ImageView) itemView.findViewById(R.id.equipmentImageview);
            mRadioButton = (RadioButton) itemView.findViewById(R.id.equipmentRadioButton);
        }
    }

    private Drawable getDrawable(String resourceName){
        Resources resources = mContext.getResources();
        final int resourceId = resources.getIdentifier(resourceName, "drawable",
                mContext.getPackageName());
        return resources.getDrawable(resourceId);
    }
}
