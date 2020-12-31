package com.example.hp.test;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import static java.security.AccessController.getContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class donate extends Fragment implements donate_Adapter.OnItemClickListner {

    DatabaseReference reference;
    RecyclerView recyclerview;
    ArrayList<donate_image> list;
    donate_Adapter  adapter;
    FirebaseStorage storage;
    private  ValueEventListener dblistner;

    public donate() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_donate, container, false);

        recyclerview=(RecyclerView) view.findViewById(R.id.selling_recyclerview);

        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        storage=FirebaseStorage.getInstance();
        reference= FirebaseDatabase.getInstance().getReference().child("donate");
        FloatingActionButton fab1=(FloatingActionButton)view.findViewById(R.id.fab_sell);
        FloatingActionButton fab2=(FloatingActionButton)view.findViewById(R.id.fab_donate);
        fab1.setTitle("Sell");
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent f = new Intent(getContext(), upload_sellingImage.class);
                startActivity(f);
            }
        });
        fab2.setTitle("Donate");
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent f = new Intent(getContext(), upload_donate_images.class);
                startActivity(f);
            }
        });
        list=new ArrayList<donate_image>();
        adapter= new donate_Adapter(getContext(),list);

        recyclerview.setAdapter(adapter);
        adapter.setOnItemClickListner(donate.this);
        dblistner=reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    donate_image p= dataSnapshot1.getValue(donate_image.class);
                    p.setKey(dataSnapshot1.getKey());
                    list.add(p);
                }
               adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(),"opsss.....something is wrong",Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    @Override
    public void OnItemClick(int position) {

    }

    @Override
    public void onDeleteClick(int position) {
        donate_image selectedItem =list.get(position);
        final String selectedKey =selectedItem.getKey();
        StorageReference imgref=storage.getReferenceFromUrl(selectedItem.getImageAddress());
        imgref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                reference.child(selectedKey).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(),"Successfully Deleted",Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(),"Deletion Failed",Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"Deletion Failed",Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        reference.removeEventListener(dblistner);

    }
}
