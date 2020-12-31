package com.example.hp.test;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class sem2 extends Fragment {
    RecyclerView recyclerView;
    FirebaseStorage storage;//used for Upload /download files ex:pdf
    FirebaseDatabase database;//used to store URLS of uploaded files



    public sem2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      //  return inflater.inflate(R.layout.fragment_sem2, container, false);
        View view= inflater.inflate(R.layout.fragment_sem2, container, false);
        storage=FirebaseStorage.getInstance();//returns an object of firebasestorage
        // database=FirebaseDatabase.getInstance();//returns an object of firebasedatabase
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("sem2");
        recyclerView =(RecyclerView)view.findViewById(R.id.recyclerview);
        //custom adapter is using now
        //populate the recycler view with item
        //  LinearLayoutManager layoutManager=new LinearLayoutManager(sem1recyclerview.this);
        // layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //recyclerView.setLayoutManager(layoutManager);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        sem2_itemAdapter notesItemAdapter=new sem2_itemAdapter(recyclerView,getContext(),new ArrayList<String>(),new ArrayList<String>());
        //28 minutes
        recyclerView.setAdapter(notesItemAdapter);

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //actually called for individual item at the database reference
                String name=dataSnapshot.getKey();//return the filename
                String url=dataSnapshot.getValue(String.class);//return url for filename

                ((sem2_itemAdapter)recyclerView.getAdapter()).update(name,url);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;
    }

}


