package com.example.hp.test;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

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

public class lost_found extends Home implements lost_item_Adapter.OnItemClickListner{
    DatabaseReference reference;
    RecyclerView recyclerview;
    ArrayList<Upload> lost_image;
    lost_item_Adapter  adapter;
    FirebaseStorage storage;
    private  ValueEventListener dblistner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_found);

       recyclerview=findViewById(R.id.lost);

        recyclerview.setLayoutManager(new LinearLayoutManager(lost_found.this));
        storage=FirebaseStorage.getInstance();
        reference= FirebaseDatabase.getInstance().getReference().child("lost");
        lost_image=new ArrayList<>();
        adapter= new lost_item_Adapter(lost_found.this,lost_image);

        recyclerview.setAdapter(adapter);
        adapter.setOnItemClickListner(lost_found.this);
        dblistner=reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    Upload L= dataSnapshot1.getValue(Upload.class);
                    L.setKey(dataSnapshot1.getKey());
                    lost_image.add(L);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(lost_found.this,"opsss.....something is wrong",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void OnItemClick(int position) {

    }

    @Override
    public void onDeleteClick(int position) {
        Upload selectedItem =lost_image.get(position);
        final String selectedKey =selectedItem.getKey();
        StorageReference imgref=storage.getReferenceFromUrl(selectedItem.getImageUrl());
        imgref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                reference.child(selectedKey).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Successfully Deleted",Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Deletion Failed",Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Deletion Failed",Toast.LENGTH_SHORT).show();

            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        reference.removeEventListener(dblistner);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(getApplicationContext(),notice_recyclerview.class));
    }
}
