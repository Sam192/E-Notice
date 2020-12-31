package com.example.hp.test;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import com.google.firebase.iid.*;
import com.google.firebase.iid.FirebaseInstanceIdService;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class notice_recyclerview extends Home implements Notice_Adapter.OnItemClickListner {
    DatabaseReference reference;
    RecyclerView recyclerview;
    ArrayList<notice> list;
    Notice_Adapter adapter;
    FirebaseStorage storage;
    private  ValueEventListener dblistner;

    public static final String TEXT="text";
    public static final String SHARED_PREFS="sharedPrefs";
    private String classname;

    public static final String CHANNEL_ID="notice";
    public static final String CHANNEL_NAME="notice";
    public static final String CHANNEL_DESC="notice";
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_recyclerview);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel=new NotificationChannel(CHANNEL_ID,CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        recyclerview=(RecyclerView)findViewById(R.id.notice_recyclerview);

        recyclerview.setLayoutManager(new LinearLayoutManager(notice_recyclerview.this));
        storage=FirebaseStorage.getInstance();

       // classname=getIntent().getStringExtra("class");
        SharedPreferences sharedPreferences=getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        classname=sharedPreferences.getString(TEXT,"");
        Log.d("Tag",classname);

        reference= FirebaseDatabase.getInstance().getReference().child(classname);
        list=new ArrayList<>();
        adapter= new Notice_Adapter(notice_recyclerview.this,list);

        recyclerview.setAdapter(adapter);
        adapter.setOnItemClickListner(notice_recyclerview.this);

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if(task.isSuccessful())
                {
                    // Toast.makeText(getApplicationContext(),"Successfull to get Token",Toast.LENGTH_LONG).show();

                    if(task.getResult()!=null)
                    {
                        String token = task.getResult().getToken();
                        saveToken(token);
                        saveeventToken(token);
                        Log.i("token",token);
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                }
            }
        });

        dblistner=reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    notice n= dataSnapshot1.getValue(notice.class);
                    n.setmKey(dataSnapshot1.getKey());
                    list.add(n);
                }
               adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(notice_recyclerview.this,"opsss.....something is wrong",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveToken(String token) {      // String classname=getIntent().getStringExtra("class");
        // String email=currentUser.getEmail();
        FirebaseAuth auth;
        FirebaseUser currentuser;

        auth = FirebaseAuth.getInstance();
        currentuser = auth.getCurrentUser();
        if (currentuser != null) {
            email = currentuser.getEmail();
        }
        if (email.equals("deybishal999@gmail.com")) {
            user_token userToken = new user_token(token);
            DatabaseReference dbtoken = FirebaseDatabase.getInstance().getReference("Admin_token");
            dbtoken.child(token).setValue(userToken).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "token is saved", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed to save token", Toast.LENGTH_LONG).show();

                    }
                }
            });
        } else {
            user_token userToken = new user_token(token);
            DatabaseReference dbtoken = FirebaseDatabase.getInstance().getReference("Token" + classname);
            dbtoken.child(token).setValue(userToken).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "token is saved", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed to save token", Toast.LENGTH_LONG).show();

                    }
                }
            });
        }
    }

    private void saveeventToken(String token){
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
        final notice selectedItem =list.get(position);
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
        final android.app.AlertDialog.Builder demobuilder = new android.app.AlertDialog.Builder(this);
        demobuilder.setMessage("Do you want to exit?");
        demobuilder.setCancelable(false);
        demobuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        demobuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        android.app.AlertDialog alert = demobuilder.create();
        alert.show();
    }
}
