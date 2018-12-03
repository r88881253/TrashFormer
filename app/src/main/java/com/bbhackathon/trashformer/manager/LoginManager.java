package com.bbhackathon.trashformer.manager;

import android.content.Context;
import android.content.SharedPreferences;

public class LoginManager {

    private static LoginManager mLoginManager;
    private String SHARE_PREFERENCE_KEY_RECYCLE_CATEGORY = "SHARE_PREFERENCE_KEY_RECYCLE_CATEGORY";
    private String SHARE_PREFERENCE_KEY_ACCOUNT = "SHARE_PREFERENCE_KEY_ACCOUNT";
    private String SHARE_PREFERENCE_KEY_PASSWORD = "SHARE_PREFERENCE_KEY_PASSWORD";

    private static SharedPreferences sharedPreferences;

    private LoginManager(){}

    public LoginManager(Context context){
        mLoginManager = this;
        sharedPreferences = context.getSharedPreferences("TrashFormer", context.MODE_PRIVATE);
    }

    public static LoginManager getInstance(){
        return mLoginManager;
    }

    public void setRecycleCategory(String recycleCategory){
        sharedPreferences.edit().putString(SHARE_PREFERENCE_KEY_RECYCLE_CATEGORY, recycleCategory).apply();
    }

    public String getRecycleCategory(){
        return sharedPreferences.getString(SHARE_PREFERENCE_KEY_RECYCLE_CATEGORY, "");
    }

    public void clearRecycleCategory(){
        sharedPreferences.edit().remove(SHARE_PREFERENCE_KEY_RECYCLE_CATEGORY).commit();
    }

    public void setUserID(String userID){
        sharedPreferences.edit().putString(SHARE_PREFERENCE_KEY_ACCOUNT, userID).apply();
    }

    public String getUserID(){
        return sharedPreferences.getString(SHARE_PREFERENCE_KEY_ACCOUNT, "");
    }

    public void clearUserID(){
        sharedPreferences.edit().remove(SHARE_PREFERENCE_KEY_ACCOUNT).commit();
    }

    public void setPassword(String userID){
        sharedPreferences.edit().putString(SHARE_PREFERENCE_KEY_PASSWORD, userID).apply();
    }

    public String getPassword(){
        return sharedPreferences.getString(SHARE_PREFERENCE_KEY_PASSWORD, "");
    }

    public void clearPassword(){
        sharedPreferences.edit().remove(SHARE_PREFERENCE_KEY_PASSWORD).commit();
    }
}
