package com.bbhackathon.trashformer.login.fragment;

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

import com.bbhackathon.trashformer.HomeActivity;
import com.bbhackathon.trashformer.Manager.FirebaseAuthManager;
import com.bbhackathon.trashformer.Manager.FirebaseDatabaseManager;
import com.bbhackathon.trashformer.R;
import com.bbhackathon.trashformer.databinding.DialogCreateUserBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.HashMap;
import java.util.Map;

public class CreateUserDialog extends DialogFragment{
    private String TAG = CreateUserDialog.class.getSimpleName();

    View mView;
    private CreateUserViewModel viewmodel;
    private DialogCreateUserBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

//        binding = DataBindingUtil.setContentView(getActivity(), R.layout.dialog_create_user);
        mView = inflater.inflate(R.layout.dialog_create_user, container, false);
        binding = DialogCreateUserBinding.bind(mView);

        viewmodel = ViewModelProviders.of(this).get(CreateUserViewModel.class);

        binding.setViewmodel(viewmodel);
//        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getDialog().setCancelable(false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        binding.btnApply.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                FirebaseAuthManager.getInstance().createUser(viewmodel.getAccount(), viewmodel.getPassword(), new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "createUserWithEmail:success\n"+task.getResult());
                            FirebaseAuthManager.getInstance().login(viewmodel.getAccount(), viewmodel.getPassword(), new LoginOnCompleteListener());

                        }else{
                            Log.d(TAG, "createUserWithEmail:failure\n"+ task.getException());
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle(getString(R.string.app_name))
                                    .setMessage(getString(R.string.add_user_to_database_failed))
                                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            getDialog().dismiss();
                                        }
                                    });
                        }
                    }
                });


//                FirebaseAuthManager.getInstance().createUser(viewmodel.getAccount(), viewmodel.getPassword(), getActivity(), new FirebaseAuthCallback() {
//                    @Override
//                    public void applySuccess() {
//                        FirebaseDatabaseManager.getInstance().addUser(viewmodel.getAccount(), viewmodel.getPassword(), viewmodel.getNickName());
//                    }
//                });
            }
        });

        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
    }

    class LoginOnCompleteListener implements OnCompleteListener<AuthResult>{
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if(task.isSuccessful()){
                Log.d(TAG, "login_background_layer_list:success\n"+task.getResult());
                AddUserListener addUserListener = new AddUserListener(FirebaseAuthManager.getInstance().getUid(), viewmodel.getPassword(), viewmodel.getNickName());
                FirebaseDatabaseManager.getInstance().addUser(addUserListener);
            }else{
                Log.d(TAG, "login_background_layer_list:failure\n"+ task.getException());
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getString(R.string.app_name))
                        .setMessage(getString(R.string.login_failed))
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getDialog().dismiss();
                            }
                        });
            }
        }
    }

    class AddUserListener implements ValueEventListener {
        private String uid;
        private String password;
        private String nickName;

        AddUserListener(String uid, String password, String nickName){
            this.uid = uid;
            this.password = password;
            this.nickName = nickName;
        }

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Map<String, Object> map = new HashMap<>();

            Map<String, String> data = new HashMap<>();
            data.put("password", DigestUtils.sha256Hex(password));
            data.put("nickName", nickName);
            map.put(uid, data);
            dataSnapshot.getRef().updateChildren(map);
            Intent i = new Intent(getActivity(), HomeActivity.class);
            startActivity(i);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Context mContext = getActivity().getBaseContext();
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(mContext.getString(R.string.app_name))
                    .setMessage(mContext.getString(R.string.create_user_failed))
                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
        }
    }
}
