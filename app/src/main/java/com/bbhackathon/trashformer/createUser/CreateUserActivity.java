package com.bbhackathon.trashformer.createUser;

import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bbhackathon.trashformer.R;
import com.bbhackathon.trashformer.base.BaseActivity;
import com.bbhackathon.trashformer.chooseRecycleCategory.ChooseRecycleCategoryActivity;
import com.bbhackathon.trashformer.databinding.ActivityCreateUserBinding;
import com.bbhackathon.trashformer.login.fragment.CreateUserViewModel;
import com.bbhackathon.trashformer.manager.FirebaseAuthManager;
import com.google.android.gms.common.util.Strings;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.HashMap;
import java.util.Map;

public class CreateUserActivity extends BaseActivity {

    private static String TAG = CreateUserActivity.class.getSimpleName();

    private CreateUserViewModel viewmodel;
    private ActivityCreateUserBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_user);
        viewmodel = ViewModelProviders.of(this).get(CreateUserViewModel.class);
        binding.setViewmodel(viewmodel);

        initListner();
        setStatusBar(R.color.yellow_background_F6C946);
    }

    private void initListner(){

        binding.btnCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Strings.isEmptyOrWhitespace(viewmodel.getAccount()) && !Strings.isEmptyOrWhitespace(viewmodel.getPassword()) && !Strings.isEmptyOrWhitespace(viewmodel.getNickName())){
                    createUser(viewmodel.getAccount(), viewmodel.getPassword(), viewmodel.getNickName());
                }

            }
        });
    }

    private void createUser(final String account, final String password, final String nickName){
        FirebaseAuthManager.getInstance().createUser(account, password, new OnCompleteListener<AuthResult>(){
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "createUserWithEmail:success\n"+task.getResult());
                    FirebaseAuthManager.getInstance().login(account, password, new LoginOnCompleteListener(nickName));

                }else{
                    Log.d(TAG, "createUserWithEmail:failure\n"+ task.getException());
                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateUserActivity.this);
                    builder.setTitle(getString(R.string.app_name))
                            .setMessage(getString(R.string.add_user_to_database_failed))
                            .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                   dialog.dismiss();
                                }
                            });
                }
            }
        });
    }

    class LoginOnCompleteListener implements OnCompleteListener<AuthResult>{
        String nickName;

        public LoginOnCompleteListener(String nickName) {
            this.nickName = nickName;
        }

        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if(task.isSuccessful()){
                Log.d(TAG, "login_background_layer_list:success\n"+task.getResult());
//                AddUserListener addUserListener = new AddUserListener(FirebaseAuthManager.getInstance().getUid(), viewmodel.getPassword(), viewmodel.getNickName());
//                FirebaseDatabaseManager.getInstance().addUser(addUserListener);
                addUserToDatabase(nickName);
            }else{
                Log.d(TAG, "login_background_layer_list:failure\n"+ task.getException());
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateUserActivity.this);
                builder.setTitle(getString(R.string.app_name))
                        .setMessage(getString(R.string.login_failed))
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
            }
        }
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(CreateUserActivity.this);
                            builder.setTitle(getString(R.string.app_name))
                                    .setMessage(getString(R.string.add_user_to_database_failed))
                                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                        }  else{
                            Log.d("addUserToDatabaseTask: ","successed");
                            startChooseRecycleItem();
                        }
                    }
                });

    }

    private void startChooseRecycleItem(){
        Intent i = new Intent(this, ChooseRecycleCategoryActivity.class);
        startActivity(i);
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

}
