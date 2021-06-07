package com.example.mvvmfirebase.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import com.example.mvvmfirebase.model.SignInUser;
import com.example.mvvmfirebase.viewmodel.SignInViewModel;

public class SplashActivity extends AppCompatActivity {

    SignInViewModel signInViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initSplashViewModel();
        checkIfUserAuthenticate();
    }

    private void checkIfUserAuthenticate() {
        signInViewModel.checkAuthenticate();
        signInViewModel.checkAuthenTicateLiveData.observe(this, new Observer<SignInUser>() {
            @Override
            public void onChanged(SignInUser signInUser) {
                if (!signInUser.isAuth){
                    goToSignInActivity();
                }
                else {
                    goToMainActivity();
                }
            }
        });
    }

    private void initSplashViewModel() {

        signInViewModel=new ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(SignInViewModel.class);
    }



    private void goToMainActivity() {
        Intent intent= new Intent(SplashActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void goToSignInActivity() {
        Intent intent= new Intent(SplashActivity.this,SigninActivity.class);
        startActivity(intent);
        finish();
    }
}