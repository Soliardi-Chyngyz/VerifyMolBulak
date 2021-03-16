package com.chyngyz.verifymolbulak.ui.fragments.authenticationByNumberFragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.chyngyz.verifymolbulak.data.User;
import com.google.firebase.firestore.FirebaseFirestore;

public class AuthNumberViewModel extends ViewModel {

    private MutableLiveData<Boolean> navigate = new MutableLiveData<>();

    public LiveData<Boolean> getNavState (){
        return navigate;
    }
}