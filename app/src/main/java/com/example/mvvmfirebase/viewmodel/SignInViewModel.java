package com.example.mvvmfirebase.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mvvmfirebase.model.SignInUser;
import com.example.mvvmfirebase.repository.SignInRepository;
import com.google.firebase.auth.AuthCredential;

public class SignInViewModel extends AndroidViewModel {
    private SignInRepository repository;
    public LiveData<SignInUser>checkAuthenTicateLiveData;
    public LiveData<String> authenticateUserLiveData;

    public SignInViewModel(@NonNull Application application) {
        super(application);
        repository=new SignInRepository();
    }

    public void checkAuthenticate(){
        checkAuthenTicateLiveData=repository.checkAuthenticationInFirebase();
    }

    //firebase sign in with google
    public void signInWithGoogle(AuthCredential authCredential){
        authenticateUserLiveData = repository.firebaseSiginWithGoogle(authCredential);
    }



}
