package com.bbhackathon.trashformer.setting.logout;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.bbhackathon.trashformer.HomeActivity;
import com.bbhackathon.trashformer.R;
import com.bbhackathon.trashformer.databinding.DialogCreateUserBinding;
import com.bbhackathon.trashformer.login.BeforeLoginActivity;
import com.bbhackathon.trashformer.login.fragment.CreateUserDialog;
import com.bbhackathon.trashformer.login.fragment.CreateUserViewModel;
import com.bbhackathon.trashformer.manager.FirebaseAuthManager;
import com.bbhackathon.trashformer.manager.FirebaseDatabaseManager;
import com.bbhackathon.trashformer.manager.LoginManager;
import com.bbhackathon.trashformer.setting.SettingActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.HashMap;
import java.util.Map;

public class LogoutDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view =  inflater.inflate(R.layout.dialog_logout, null);

        Button btnLogout = (Button) view.findViewById(R.id.btnLogoutConfirm);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuthManager.getInstance().logout();
                Toast.makeText(getContext(), "登出", Toast.LENGTH_SHORT).show();
//                LoginManager.getInstance().clearRecycleCategory();
                Intent i = new Intent(getActivity(), BeforeLoginActivity.class);
                startActivity(i);
            }
        });

        builder.setView(view);
        return builder.create();
    }
}
