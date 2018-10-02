package com.bbhackathon.trashformer.Manager;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class FirebaseDatabaseManager {
    private String TAG = FirebaseDatabaseManager.class.getSimpleName();

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private static FirebaseDatabaseManager mFirebaseDatabaseManager;

    private FirebaseDatabaseManager(){

    }

    public static FirebaseDatabaseManager getInstance(){
        if(mFirebaseDatabaseManager == null){
            mFirebaseDatabaseManager = new FirebaseDatabaseManager();
        }
        return mFirebaseDatabaseManager;
    }

    public void addUser(String email, String password, String nickName){
        DatabaseReference ref = mDatabase.getReference("UserTable");

        Map<String, Object> map = new HashMap<>();

        Map<String, String> data = new HashMap<>();
        data.put("password", password);
        data.put("nickName", nickName);

        map.put(email, data);
        ref.updateChildren(map);
    }
}
