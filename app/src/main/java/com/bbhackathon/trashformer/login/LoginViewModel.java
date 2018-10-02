package com.bbhackathon.trashformer.login;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;

public class LoginViewModel extends ViewModel{

    public ObservableField<String> account = new ObservableField<>("");

    public ObservableField<String> password = new ObservableField<>("");

    public ObservableField<String> getAccount() {
        return account;
    }

    public void setAccount(ObservableField<String> account) {
        this.account = account;
    }

    public ObservableField<String> getPassword() {
        return password;
    }

    public void setPassword(ObservableField<String> password) {
        this.password = password;
    }
}
