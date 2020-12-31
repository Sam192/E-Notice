package com.example.hp.test;
// used to show notes in sem1 swipe view of notes.java
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class sem1recyclerview extends AppCompatActivity {
    RecyclerView recyclerView;
    FirebaseStorage storage;//used for Upload /download files ex:pdf
    FirebaseDatabase database;//used to store URLS of uploaded files



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sem1);



        storage=FirebaseStorage.getInstance();//returns an object of firebasestorage
        database=FirebaseDatabase.getInstance();//returns an object of firebasedatabase
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("sem1");

        final RecyclerView recyclerView=findViewById(R.id.recyclerview);
        //custom adapter is using now
        //populate the recycler view with item
        //  LinearLayoutManager layoutManager=new LinearLayoutManager(sem1recyclerview.this);
        // layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //recyclerView.setLayoutManager(layoutManager);
        recyclerView.setLayoutManager(new LinearLayoutManager(sem1recyclerview.this));
        NotesItemAdapter notesItemAdapter=new NotesItemAdapter(recyclerView,sem1recyclerview.this,new ArrayList<String>(),new ArrayList<String>());
        //28 minutes
        recyclerView.setAdapter(notesItemAdapter);

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //actually called for individual item at the database reference
                String name=dataSnapshot.getKey();//return the filename
                 String url=dataSnapshot.getValue(String.class);//return url for filename

                ((NotesItemAdapter)recyclerView.getAdapter()).update(name,url);
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

        //def var =
    }
}
