package com.bbhackathon.trashformer.leaderboard;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bbhackathon.trashformer.R;
import com.bbhackathon.trashformer.equipment.EquipmentAdapter;

import java.util.List;

public class LeaderBoardAdapter  extends RecyclerView.Adapter<LeaderBoardAdapter.ViewHolder>{
    private List<String> mData;
    private Context mContext;

    public LeaderBoardAdapter(Context context, List<String> data) {
        this.mContext = context;
        mData = data;
    }

    @Override
    public LeaderBoardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {

        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_fragment2, null);
        return new LeaderBoardAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(LeaderBoardAdapter.ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Item " + position + " is clicked.", Toast.LENGTH_SHORT).show();
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(mContext, "Item " + position + " is long clicked.", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView mImageView;
        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.leaderboaed_avatar);
        }
    }
}
