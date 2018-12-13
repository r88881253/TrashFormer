package com.bbhackathon.trashformer.present;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bbhackathon.trashformer.R;

public class PresentDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.dialog_present, null);

        TextView getPresentTextView = (TextView) view.findViewById(R.id.getPresentTextView);

        String recycleItem = "聖誕帽";

        SpannableString getPresentString = new SpannableString("獲得 " + recycleItem + " 啦！");
        getPresentString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.btn_login_background_806EE6)), 3, 3 + recycleItem.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        getPresentTextView.setText(getPresentString);

        Button btnLogout = (Button) view.findViewById(R.id.btnReceivePresent);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "領取成功", Toast.LENGTH_SHORT).show();
                getDialog().dismiss();

            }
        });

        builder.setView(view);
        return builder.create();
    }
}
