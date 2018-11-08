package com.bbhackathon.trashformer.manager;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseAuthManager {
    private String TAG = FirebaseAuthManager.class.getSimpleName();

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static FirebaseAuthManager mFirebaseAuthManager;

    private FirebaseAuthManager(){
    }

    public static FirebaseAuthManager getInstance(){
        if(mFirebaseAuthManager == null){
            mFirebaseAuthManager = new FirebaseAuthManager();
        }
        return mFirebaseAuthManager;
    }

    public void login(String email, String password, OnCompleteListener<AuthResult> onCompleteListener){
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(onCompleteListener);
    }

    public FirebaseUser getUser(){
        return mAuth.getCurrentUser();
    }

    public String getUid(){
        if(getUser() == null){
            return "9999";
        }
        else {
            return getUser().getUid();
        }
    }

    public void logout(){
        mAuth.getInstance().signOut();
    }

    public void createUser(String account, String password, OnCompleteListener<AuthResult> onCompleteListener){
        mAuth.createUserWithEmailAndPassword(account, password)
                .addOnCompleteListener(onCompleteListener);
    }

    public void createUser(String account, String password, Activity activity, final FirebaseAuthCallback callback){
        mAuth.createUserWithEmailAndPassword(account, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            callback.applySuccess();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        }

                        // ...
                    }
                });
    }

//        mAuth.signInWithEmailAndPassword(viewmodel.getAccount().get(), viewmodel.getPassword().get())
//                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithEmail:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
//                            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
//                            startActivity(i);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithEmail:failure", task.getException());
//                            Toast.makeText(LoginActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
//                        }
//
//                        // ...
//                    }
//                });
//    }
}
