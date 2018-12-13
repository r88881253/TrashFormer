package com.bbhackathon.trashformer.manager;

import com.bbhackathon.trashformer.entity.UserProfileTable;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
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

    public void selectUserProfileTable(String uid, ValueEventListener valueEventListener){
        DatabaseReference ref = mDatabase.getReference().child("UserTable/" + uid + "/UserProfileTable");
        ref.addValueEventListener(valueEventListener);
    }

    public void selectUserTable(ValueEventListener valueEventListener){
        DatabaseReference ref = mDatabase.getReference().child("UserTable/");
        ref.addValueEventListener(valueEventListener);
    }

    public void selectMissionTable(String uid, ValueEventListener valueEventListener){
        DatabaseReference ref = mDatabase.getReference().child("UserTable/" + uid + "/UserMissionTable");
        ref.addValueEventListener(valueEventListener);
    }

    public void selectTipsTable(ValueEventListener valueEventListener){
        DatabaseReference ref = mDatabase.getReference().child("TipsTable/");
        ref.addListenerForSingleValueEvent(valueEventListener);
    }

    public void updateRecycleAmount(String uid, String recycleCategory){
        final DatabaseReference usersRef = mDatabase.getReference().child("UserTable/"+ uid + "/UserProfileTable/" + recycleCategory);

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long recycleAmount =(long) dataSnapshot.getValue();
                usersRef.setValue(recycleAmount + 1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void updateAddExp(String uid){
        final DatabaseReference usersRef = mDatabase.getReference().child("UserTable/"+ uid + "/UserProfileTable");



        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final DatabaseReference expTable = mDatabase.getReference().child("ExpTable");

                UserProfileTable userProfile = dataSnapshot.getValue(UserProfileTable.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
