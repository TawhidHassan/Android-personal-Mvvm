package com.example.mvvmfirebase.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mvvmfirebase.model.SignInUser;
import com.example.mvvmfirebase.repository.SignInRepository;

public class SignInViewModel extends AndroidViewModel {
    private SignInRepository repository;
    public LiveData<SignInUser>checkAuthenTicateLiveData;

    public SignInViewModel(@NonNull Application application) {
        super(application);
        repository=new SignInRepository();
    }

    public void checkAuthenticate(){
        checkAuthenTicateLiveData=repository.checkAuthenticationInFirebase();
    }

}
