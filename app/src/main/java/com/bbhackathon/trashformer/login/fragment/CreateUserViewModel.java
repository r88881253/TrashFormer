package com.bbhackathon.trashformer.login.fragment;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class CreateUserViewModel extends ViewModel{

    private MutableLiveData<String> account = new MutableLiveData<>();

    private MutableLiveData<String> password = new MutableLiveData<>();

    private MutableLiveData<String> nickName = new MutableLiveData<>();

    public String getAccount(){
        return account.getValue();
    }

    public String getPassword(){
        return password.getValue();
    }

    public String getNickName(){
        return nickName.getValue();
    }

    public void setAccount(String account){
        this.account.setValue(account);
    }

    public void setPassword(String password){
        this.password.setValue(password);
    }
    public void setNickName(String nickName){
        this.nickName.setValue(nickName);
    }

}
