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

import com.bbhackathon.trashformer.R;
import com.bbhackathon.trashformer.chooseRecycleCategory.ChooseRecycleCategoryActivity;
import com.bbhackathon.trashformer.databinding.DialogCreateUserBinding;
import com.bbhackathon.trashformer.manager.FirebaseAuthManager;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.functions.HttpsCallableResult;

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
                                    .setMessage(getString(R.string.something_failed))
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

    private void addUserToDatabase(String nickname){
        addUserToDatabaseTask(nickname)
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Exception e = task.getException();
                            if (e instanceof FirebaseFunctionsException) {
                                FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                                FirebaseFunctionsException.Code code = ffe.getCode();
                                Object details = ffe.getDetails();
                            }
                            Log.d("addUserToDatabaseTask: ","failed");
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle(getString(R.string.app_name))
                                    .setMessage(getString(R.string.something_failed))
                                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            getDialog().dismiss();
                                        }
                                    });
                        }  else{
                            Log.d("addUserToDatabaseTask: ","successed");
                            startChooseRecycleItem();
                        }
                    }
                });

    }

    private Task<String> addUserToDatabaseTask(String text) {
        FirebaseFunctions mFunctions;
        mFunctions = FirebaseFunctions.getInstance();

        // Create the arguments to the callable function.
        Map<String, Object> data = new HashMap<>();
        data.put("text", text);
        data.put("push", true);

        return mFunctions
                .getHttpsCallable("addUserToDatabase")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        // This continuation runs on either success or failure, but if the task
                        // has failed then getResult() will throw an Exception which will be
                        // propagated down.
                        String result = (String) task.getResult().getData();
                        return result;
                    }
                });
    }

    private void startChooseRecycleItem(){
        Intent i = new Intent(getActivity(), ChooseRecycleCategoryActivity.class);
        startActivity(i);
    }

    class LoginOnCompleteListener implements OnCompleteListener<AuthResult>{
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if(task.isSuccessful()){
                Log.d(TAG, "login_background_layer_list:success\n"+task.getResult());
//                AddUserListener addUserListener = new AddUserListener(FirebaseAuthManager.getInstance().getUid(), viewmodel.getPassword(), viewmodel.getNickName());
//                FirebaseDatabaseManager.getInstance().addUser(addUserListener);
                addUserToDatabase(viewmodel.getNickName());
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
