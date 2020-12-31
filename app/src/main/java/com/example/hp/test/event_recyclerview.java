package com.example.hp.test;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
//import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class event_recyclerview extends Home implements event_Adapter.OnItemClickListner {
    DatabaseReference reference;
    RecyclerView recyclerview;
    ArrayList<event_class> Event;
    event_Adapter adapter;
    FirebaseStorage storage;
    private  ValueEventListener dblistner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_recyclerview);

        recyclerview=(RecyclerView)findViewById(R.id.event_recyclerview);

        recyclerview.setLayoutManager(new LinearLayoutManager(event_recyclerview.this));
        storage=FirebaseStorage.getInstance();
        reference= FirebaseDatabase.getInstance().getReference().child("event");

        com.getbase.floatingactionbutton.FloatingActionButton fab_event=findViewById(R.id.fab_event);
        fab_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent f = new Intent(event_recyclerview.this, upload_event.class);
                startActivity(f);
            }
        });
        Event=new ArrayList<>();
        adapter= new event_Adapter(event_recyclerview.this,Event);

        recyclerview.setAdapter(adapter);

        adapter.setOnItemClickListner(event_recyclerview.this);
      /*  FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if(task.isSuccessful())
                {
                    // Toast.makeText(getApplicationContext(),"Successfull to get Token",Toast.LENGTH_LONG).show();

                    if(task.getResult()!=null)
                    {
                        String token = task.getResult().getToken();
                        saveToken(token);
                        Log.i("token",token);
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                }
            }
        });*/
        dblistner=reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Event.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    event_class n= dataSnapshot1.getValue(event_class.class);
                    n.setmKey(dataSnapshot1.getKey());
                    Event.add(n);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(event_recyclerview.this,"opsss.....something is wrong",Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void saveToken(String token)
    {
        // String email=currentUser.getEmail();
        user_token userToken=new user_token(token);
        DatabaseReference dbtoken=FirebaseDatabase.getInstance().getReference("Event_token");
        dbtoken.child(token).setValue(userToken).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(),"token is saved",Toast.LENGTH_LONG).show();
                }else
                {
                    Toast.makeText(getApplicationContext(),"Failed to save token",Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    @Override
    public void OnItemClick(int position) {

    }

    @Override
    public void onDeleteClick(int position) {
        final event_class selectedItem =Event.get(position);
        final String selectedKey =selectedItem.getmKey();
        StorageReference imgref=storage.getReferenceFromUrl(selectedItem.getImageAddress());
        imgref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                StorageReference pdfref=storage.getReferenceFromUrl(selectedItem.getPdf());
                pdfref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
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
