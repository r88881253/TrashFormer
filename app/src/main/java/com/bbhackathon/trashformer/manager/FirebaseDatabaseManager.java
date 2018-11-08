package com.bbhackathon.trashformer.manager;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    public void addUser(ValueEventListener valueEventListener){
        final DatabaseReference ref = mDatabase.getReference().child("UserTable");
        ref.addValueEventListener(valueEventListener);


    }
}
