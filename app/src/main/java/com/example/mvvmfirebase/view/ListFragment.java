package com.example.mvvmfirebase.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mvvmfirebase.R;
import com.example.mvvmfirebase.adapter.ContactAdapter;
import com.example.mvvmfirebase.dialogue.DetailsDialogue;
import com.example.mvvmfirebase.model.ContactUser;
import com.example.mvvmfirebase.viewmodel.ContactViewModel;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class ListFragment extends Fragment implements ContactAdapter.ClickInterface {

    private SearchView searchView;
    private RecyclerView recyclerView;
    private ContactAdapter adapter;
    private List<ContactUser> userList= new ArrayList<>();
    //view Model
    private ContactViewModel contactViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewModel();
        setUpRecycle();

        //find Section...
        searchView= view.findViewById(R.id.searchViewId);
        recyclerView= view.findViewById(R.id.recycleViewId);
        adapter= new ContactAdapter(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void setUpRecycle() {
        final AlertDialog dialogue= new SpotsDialog.Builder().setContext(getActivity()).setTheme(R.style.Custom).setCancelable(true).build();
        dialogue.show();
        contactViewModel.show();
        contactViewModel.getContactLiveData.observe(getViewLifecycleOwner(), new Observer<List<ContactUser>>() {
            @Override
            public void onChanged(List<ContactUser> contactUsers) {
                dialogue.dismiss();
                userList= contactUsers;
                adapter.getContactList(userList);
                recyclerView.setAdapter(adapter);
            }
        });
    }

    private void initViewModel() {
        contactViewModel= new ViewModelProvider(getActivity(),ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()))
                .get(ContactViewModel.class);
    }

    @Override
    public void onItemClick(int position) {
//Toast.makeText(getActivity(), ""+position, Toast.LENGTH_SHORT).show();
        openDetailsDialogue(position);
    }



    @Override
    public void onLongItemClick(int position) {
        final String id= userList.get(position).contactId;

        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());

        String[] option= {"Update","Delete"};
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0){

                }
                if(which==1){

                    Toast.makeText(getActivity(), "Delete", Toast.LENGTH_SHORT).show();
                    userList.remove(position);
                    adapter.notifyItemRemoved(position);
                }
            }
        }).create().show();
    }

    private void openDetailsDialogue(int position) {
        DetailsDialogue dialogue= new DetailsDialogue(userList,position);
        dialogue.show(getChildFragmentManager(),"Details Dialogue");
    }
}