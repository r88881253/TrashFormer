package com.bbhackathon.trashformer.login.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bbhackathon.trashformer.Manager.FirebaseAuthCallback;
import com.bbhackathon.trashformer.Manager.FirebaseAuthManager;
import com.bbhackathon.trashformer.Manager.FirebaseDatabaseManager;
import com.bbhackathon.trashformer.R;
import com.bbhackathon.trashformer.databinding.DialogCreateUserBinding;

public class CreateUserDialog extends DialogFragment{

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
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().setCancelable(false);

        binding.btnApply.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FirebaseAuthManager.getInstance().createUser(viewmodel.getAccount(), viewmodel.getPassword(), getActivity(), new FirebaseAuthCallback() {
                    @Override
                    public void applySuccess() {
                        FirebaseDatabaseManager.getInstance().addUser(viewmodel.getAccount(), viewmodel.getPassword(), viewmodel.getNickName());
                    }
                });
            }
        });

        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
    }
}
