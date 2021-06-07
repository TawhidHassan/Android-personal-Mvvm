package com.example.mvvmfirebase.repository;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.mvvmfirebase.model.ContactUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class ContactRepository {

    private FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();

    private StorageReference storageReference= FirebaseStorage.getInstance().getReference();
    private FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();

    public MutableLiveData<String>insertContactFirebase(final ContactUser user, Uri uri){

        final String currentUser= firebaseAuth.getCurrentUser().getUid();

        final MutableLiveData<String> insertResultLiveData= new MutableLiveData<>();

        final StorageReference image_path= storageReference.child("profile_image").child(currentUser).child(user.contactId+".jpg");

        image_path.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                image_path.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Map<String,String> contactMap= new HashMap<>();
                        contactMap.put("contact_Id",user.contactId);
                        contactMap.put("contact_Name",user.contactName);
                        contactMap.put("contact_Image",uri.toString());
                        contactMap.put("contact_Phone",user.contactPhone);
                        contactMap.put("contact_Email",user.contactEmail);
                        contactMap.put("contact_Search",user.contactId);

                        //now put this data in firebase....
                        firebaseFirestore.collection("ContactList").document(currentUser).collection("User")
                                .document(user.contactId).set(contactMap)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        insertResultLiveData.setValue("Upload Successfully");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                insertResultLiveData.setValue(e.toString());
                            }
                        });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                insertResultLiveData.setValue(e.toString());
            }
        });


        return insertResultLiveData;
    }

}
