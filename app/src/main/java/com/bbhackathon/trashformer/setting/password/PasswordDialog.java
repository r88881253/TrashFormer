package com.bbhackathon.trashformer.setting.password;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.bbhackathon.trashformer.R;
import com.bbhackathon.trashformer.setting.SettingActivity;

public class PasswordDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view =  inflater.inflate(R.layout.dialog_input_password, null);

        Button btnLogout = (Button) view.findViewById(R.id.btnPasswordConfirm);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), SettingActivity.class);
                startActivity(i);
                getDialog().dismiss();
            }
        });

        builder.setView(view);
        return builder.create();
    }
}
