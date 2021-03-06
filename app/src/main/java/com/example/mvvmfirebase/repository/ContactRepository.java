package com.example.mvvmfirebase.repository;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mvvmfirebase.model.ContactUser;
import com.example.mvvmfirebase.model.UpdateUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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


    public MutableLiveData<List<ContactUser>> getDataFromFireStore(){

        String currentUser= firebaseAuth.getCurrentUser().getUid();

        final List<ContactUser> contactList= new ArrayList<>();
        final MutableLiveData<List<ContactUser>> getFireStoreMutableLiveData= new MutableLiveData<>();

        firebaseFirestore.collection("ContactList").document(currentUser).collection("User").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        contactList.clear();
                        for (DocumentSnapshot documentSnapshot: task.getResult()){
                            String id= documentSnapshot.getString("contact_Id");
                            String name= documentSnapshot.getString("contact_Name");
                            String image= documentSnapshot.getString("contact_Image");
                            String phone= documentSnapshot.getString("contact_Phone");
                            String email= documentSnapshot.getString("contact_Email");

                            ContactUser user= new ContactUser(id,name,image,phone,email);
                            contactList.add(user);
                        }

                        getFireStoreMutableLiveData.setValue(contactList);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });




        return getFireStoreMutableLiveData;

    }

    public void deleteDataFirebase(String id) {
        final String currentUser= firebaseAuth.getCurrentUser().getUid();

        StorageReference deleteImage= storageReference.child("profile_image").child(currentUser).child(id+".jpg");

        deleteImage.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                firebaseFirestore.collection("ContactList").document(currentUser).collection("User")
                        .document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });
            }
        });

    }

    public void updateInfoFirebase(UpdateUser user) {
        final String currentUser= firebaseAuth.getCurrentUser().getUid();
        firebaseFirestore.collection("ContactList").document(currentUser)
                .collection("User").document(user.contactId)
                .update("contact_Name",user.contactName,
                        "contact_Phone",user.contactPhone,"contact_Email",user.contactEmail).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    //update Image
    public void updateImageFirebase(final String id, Uri uri){
        final String currentUser= firebaseAuth.getCurrentUser().getUid();

        final StorageReference image_path= storageReference.child("profile_image").child(currentUser).child(id+".jpg");

        image_path.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                image_path.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        firebaseFirestore.collection("ContactList").document(currentUser)
                                .collection("User").document(id).update("contact_Image",uri.toString())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                    }
                                });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public LiveData<List<ContactUser>> searchDataFirebase(String s) {
        String currentUser= firebaseAuth.getCurrentUser().getUid();

        final List<ContactUser> searchList= new ArrayList<>();
        final MutableLiveData<List<ContactUser>> getSearchMutableLiveData= new MutableLiveData<>();

        firebaseFirestore.collection("ContactList").document(currentUser).collection("User").whereEqualTo("contact_Search",s)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(DocumentSnapshot documentSnapshot: task.getResult()){
                    String id= documentSnapshot.getString("contact_Id");
                    String name= documentSnapshot.getString("contact_Name");
                    String image= documentSnapshot.getString("contact_Image");
                    String phone= documentSnapshot.getString("contact_Phone");
                    String email= documentSnapshot.getString("contact_Email");
                    ContactUser user= new ContactUser(id,name,image,phone,email);
                    searchList.add(user);
                }
                getSearchMutableLiveData.setValue(searchList);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });



        return getSearchMutableLiveData;
    }
}
