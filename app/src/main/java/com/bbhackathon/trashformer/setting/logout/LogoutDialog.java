package com.bbhackathon.trashformer.setting.logout;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bbhackathon.trashformer.R;
import com.bbhackathon.trashformer.login.BeforeLoginActivity;
import com.bbhackathon.trashformer.manager.FirebaseAuthManager;
import com.bbhackathon.trashformer.manager.LoginManager;
import com.bbhackathon.trashformer.setting.SettingActivity;

public class LogoutDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.dialog_logout, null);

        Button btnLogout = (Button) view.findViewById(R.id.btnLogoutConfirm);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SettingActivity) getActivity()).showProgressDialog();
                FirebaseAuthManager.getInstance().logout();
                Toast.makeText(getContext(), "登出", Toast.LENGTH_SHORT).show();
                LoginManager.getInstance(getActivity()).clearRecycleCategory();

                ((SettingActivity) getActivity()).dismissProgressDialog();

                Intent i = new Intent(getActivity(), BeforeLoginActivity.class);
                startActivity(i);

            }
        });

        builder.setView(view);
        return builder.create();
    }
}
