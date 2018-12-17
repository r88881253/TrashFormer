package com.bbhackathon.trashformer.present;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bbhackathon.trashformer.R;
import com.bbhackathon.trashformer.entity.EquipmentEntity;
import com.bbhackathon.trashformer.equipment.drawable.DrawableName;
import com.bbhackathon.trashformer.manager.LoginManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PresentDialog extends DialogFragment {

    private Context mContext;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.dialog_present, null);

        mContext = getContext();

        TextView getPresentTextView = (TextView) view.findViewById(R.id.getPresentTextView);

        String recycleItem = "聖誕帽";

        Map<String, EquipmentEntity> currentMap = new HashMap<>(DrawableName.equipNameMap);

        for(String key: LoginManager.getInstance(getContext()).getEquipment().keySet()){
            if(DrawableName.equipNameMap.containsKey(key)){
                currentMap.remove(key);
            }
        }


        if(currentMap.size() > 0){
            List<String> equipmentMapKeyList = new ArrayList<>(currentMap.keySet());
            int randomNumPresent = new Random().nextInt(equipmentMapKeyList.size());
            String randomPresentKey = equipmentMapKeyList.get(randomNumPresent);
            String randomPresentName = currentMap.get(randomPresentKey).getName();

            if(randomPresentName != null){
                recycleItem = randomPresentName;
            }

            LoginManager.getInstance(getContext()).addEquipment(new EquipmentEntity(DrawableName.equipNameMap.get(randomPresentKey).getName(), randomPresentKey, false));

            ImageView presentImageView = (ImageView) view.findViewById(R.id.presentImageView);
            Drawable randomPresentDrawable = getDrawable(randomPresentKey);
            if(randomPresentDrawable != null){
                presentImageView.setImageDrawable(randomPresentDrawable);
            }

            SpannableString getPresentString = new SpannableString("獲得 " + recycleItem + " 啦！");
            getPresentString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.btn_login_background_806EE6)), 3, 3 + recycleItem.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            getPresentTextView.setText(getPresentString);
        } else{
            Toast.makeText(getContext(), "已經沒有裝備可兌換了",
                    Toast.LENGTH_SHORT).show();
        }


        Button btnLogout = (Button) view.findViewById(R.id.btnReceivePresent);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        builder.setView(view);
        return builder.create();
    }

    private Drawable getDrawable(String resourceName){
        Resources resources = mContext.getResources();
        final int resourceId = resources.getIdentifier(resourceName, "drawable",
                mContext.getPackageName());
        return resources.getDrawable(resourceId);
    }
}
