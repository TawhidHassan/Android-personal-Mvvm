package com.example.mvvmfirebase.viewmodel;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mvvmfirebase.model.ContactUser;
import com.example.mvvmfirebase.repository.ContactRepository;

public class ContactViewModel extends AndroidViewModel {


    private ContactRepository repository;
    public LiveData<String> insertResultLiveData;

    public ContactViewModel(@NonNull Application application) {
        super(application);
        repository=new ContactRepository();
    }

    public void insert(ContactUser user, Uri uri){
        insertResultLiveData= repository.insertContactFirebase(user, uri);
    }
}
