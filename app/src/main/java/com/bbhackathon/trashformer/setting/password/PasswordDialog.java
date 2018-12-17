package com.bbhackathon.trashformer.setting.password;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.bbhackathon.trashformer.HomeActivity;
import com.bbhackathon.trashformer.R;
import com.bbhackathon.trashformer.manager.FirebaseAuthManager;
import com.bbhackathon.trashformer.setting.SettingActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

                String password = ((EditText) getDialog().findViewById(R.id.passwordDialogEditText)).getText().toString();

                ((HomeActivity) getActivity()).showProgressDialog();
                checkPassword(password, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Intent i = new Intent(getActivity(), SettingActivity.class);
                            startActivity(i);
                            ((HomeActivity) getActivity()).dismissProgressDialog();
                            getDialog().dismiss();
                        }
                        else {
                            ((HomeActivity) getActivity()).dismissProgressDialog();
                            ((HomeActivity) getActivity()).showAlertDialog("密碼錯誤");

                        }
                    }
                });


            }
        });

        builder.setView(view);
        return builder.create();
    }

    private void checkPassword(String password, OnCompleteListener<Void> checkPasswordCallback){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Get auth credentials from the user for re-authentication. The example below shows
        // email and password credentials but there are multiple possible providers,
        // such as GoogleAuthProvider or FacebookAuthProvider.
        AuthCredential credential = EmailAuthProvider
                .getCredential(FirebaseAuthManager.getInstance().getUser().getEmail(), password);

        // Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(checkPasswordCallback);

    }
}
