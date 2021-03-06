package com.example.mvvmfirebase.dialogue;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.example.mvvmfirebase.R;
import com.example.mvvmfirebase.model.ContactUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailsDialogue extends DialogFragment {
    private CircleImageView circleImageView;
    private TextView idTextView,nameTextView,phoneTextView,emailTextView;
    private List<ContactUser> userList;
    private int position;

    public DetailsDialogue(List<ContactUser> userList, int position) {
        this.userList = userList;
        this.position = position;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        LayoutInflater inflater= getActivity().getLayoutInflater();
        View view= inflater.inflate(R.layout.details_dialogue,null);
        builder.setView(view).setTitle("Contact Details").setIcon(R.drawable.ic_view).setCancelable(true)
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });

        circleImageView= view.findViewById(R.id.detailsImageId);
        idTextView= view.findViewById(R.id.detailsId);
        nameTextView= view.findViewById(R.id.detailsNameId);
        phoneTextView= view.findViewById(R.id.detailsPhoneId);
        emailTextView= view.findViewById(R.id.detailsEmailId);

        Glide.with(view.getContext()).load(userList.get(position).getContactImage()).centerCrop()
                .placeholder(R.drawable.profile).into(circleImageView);
        idTextView.setText("ID: "+userList.get(position).getContactId());
        nameTextView.setText("Name: "+userList.get(position).getContactName());
        phoneTextView.setText("Phone: "+userList.get(position).getContactPhone());
        emailTextView.setText("Email: "+userList.get(position).getContactEmail());

        return builder.create();

    }

}
