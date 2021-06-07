package com.example.mvvmfirebase.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.mvvmfirebase.model.SignInUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInRepository {
    private FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    private SignInUser user= new SignInUser();


    public MutableLiveData<SignInUser>checkAuthenticationInFirebase(){

        MutableLiveData<SignInUser> isAuthenticateLiveData=new MutableLiveData<>();

        FirebaseUser currentUser=firebaseAuth.getCurrentUser();
        if(currentUser==null){
            user.isAuth= false;
            isAuthenticateLiveData.setValue(user);
        }
        else {
            user.uId = currentUser.getUid();
            user.isAuth = true;
            isAuthenticateLiveData.setValue(user);
        }
        return isAuthenticateLiveData;

    }

    //firebase sign in with google
    public MutableLiveData<String>firebaseSiginWithGoogle(AuthCredential authCredential){
        MutableLiveData<String> authMutableLiveData=new MutableLiveData<>();

        firebaseAuth.signInWithCredential(authCredential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                FirebaseUser currentUser =firebaseAuth.getCurrentUser();
                String uId= currentUser.getUid().toString();
                authMutableLiveData.setValue(uId);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                authMutableLiveData.setValue(e.toString());
            }
        });


        return authMutableLiveData;
    }
}
