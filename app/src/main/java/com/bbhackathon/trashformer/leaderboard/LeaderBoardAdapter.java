package com.bbhackathon.trashformer.leaderboard;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bbhackathon.trashformer.R;
import com.bbhackathon.trashformer.entity.UserRankEntity;
import com.bbhackathon.trashformer.manager.FirebaseAuthManager;

import org.apache.commons.codec.binary.StringUtils;

import java.util.Arrays;
import java.util.List;

public class LeaderBoardAdapter extends RecyclerView.Adapter<LeaderBoardAdapter.ViewHolder> {
    private Context mContext;
    private List<UserRankEntity> mData;
    private int userPosition;
    private List<String> rankList = Arrays.asList("一", "二", "三", "四", "五", "六", "七", "八", "九", "十",
            "十一", "十二", "十三", "十四", "十五", "十六", "十七", "十八", "十九", "二十", "二十一", "二十二", "二十三",
            "二十四", "二十五", "二十六", "二十七", "二十八", "二十九", "三十", "三十一", "三十二", "三十三", "三十四",
            "三十五", "三十六", "三十七", "三十八", "三十九", "四十");

    public LeaderBoardAdapter(Context context, List<UserRankEntity> data, int userPosition) {
        this.mContext = context;
//        mData = Arrays.asList(new UserRankEntity("kent", 20), new UserRankEntity("kent", 20),new UserRankEntity("kent", 20),new UserRankEntity("kent", 20),new UserRankEntity("kent", 20),new UserRankEntity("kent", 20),new UserRankEntity("kent", 20));
        mData = data;
        this.userPosition = userPosition;
    }

    @Override
    public LeaderBoardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_fragment2, null);
        return new LeaderBoardAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(LeaderBoardAdapter.ViewHolder holder, final int position) {
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(mContext, "Item " + position + " is long clicked.", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        if (position < 41) {
            ((TextView) holder.itemView.findViewById(R.id.rank)).setText("第" + rankList.get(position).toString() + "名");
        }

        View leftview = (View) holder.itemView.findViewById(R.id.leaderboardRecyclerviewLeftView);
        View rightView = (View) holder.itemView.findViewById(R.id.leaderboardRecyclerviewRightView);
        if (StringUtils.equals(mData.get(position).getUid(), FirebaseAuthManager.getInstance().getUid())) {
            if (position % 2 == 0) {
                leftview.setBackground(mContext.getResources().getDrawable((R.color.btn_login_background_806EE6)));
                rightView.setBackground(mContext.getResources().getDrawable((R.color.btn_login_background_806EE6)));
            } else {
                leftview.setBackground(mContext.getResources().getDrawable((R.color.yellow_background_F6C946)));
                rightView.setBackground(mContext.getResources().getDrawable((R.color.yellow_background_F6C946)));
            }
        } else {
            leftview.setBackground(mContext.getResources().getDrawable(R.color.white));
            rightView.setBackground(mContext.getResources().getDrawable(R.color.white));
        }

        ImageView mImageView = (ImageView) holder.itemView.findViewById(R.id.leaderboaed_avatar);
        if (position % 2 == 0) {
            mImageView.setImageDrawable(holder.itemView.getContext().getResources().getDrawable(R.drawable.avatar_icon_sunglow));
        } else {
            mImageView.setImageDrawable(holder.itemView.getContext().getResources().getDrawable(R.drawable.avatar_icon_slate));
        }

        if (mData.size() != 0) {
            TextView mLevelTextview = (TextView) holder.itemView.findViewById(R.id.leaderboardLevel);
            mLevelTextview.setText(String.valueOf(mData.get(position).getLevel()));

            TextView mMonsterNameTextView = (TextView) holder.itemView.findViewById(R.id.leaderboardMonsterName);
            mMonsterNameTextView.setText(mData.get(position).getMonsterName());

            TextView mNickNameTextView = (TextView) holder.itemView.findViewById(R.id.leaderboardNickName);
            mNickNameTextView.setText(mData.get(position).getName());
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);

        }
    }
}
