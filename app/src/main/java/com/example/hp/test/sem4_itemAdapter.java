package com.example.hp.test;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class sem4_itemAdapter extends RecyclerView.Adapter<sem4_itemAdapter.ViewHolder> {
        RecyclerView recyclerView;
        Context context;
        ArrayList<String> items=new ArrayList<>();
        ArrayList<String> urls=new ArrayList<>();
    private DatabaseReference mReference = FirebaseDatabase.getInstance().getReference("sem4");
    private StorageReference storage = FirebaseStorage.getInstance().getReference("sem4");

public void update(String name,String url)
        {
        items.add(name);
        urls.add(url);
        notifyDataSetChanged();//refreshes the recyclerview automatically
        }

public sem4_itemAdapter(RecyclerView recyclerView, Context context, ArrayList<String> items, ArrayList<String> urls) {
        this.recyclerView = recyclerView;
        this.context = context;
        this.items = items;
        this.urls=urls;
        }

@Override
public sem4_itemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {   //to create views for recyclerview item
        /*using object of layoutinflater for create an object itemrecycler.xml and
         storing the view in view */
        //creating view for itemrecycler.xml
        View view= LayoutInflater.from(context).inflate(R.layout.sem3_item,parent,false);
        sem4_itemAdapter.ViewHolder viewHolder= new sem4_itemAdapter.ViewHolder(view);//returns object of viewholder class
        return viewHolder;
        }

@Override
public void onBindViewHolder(sem4_itemAdapter.ViewHolder holder, int position) {
        //in this initialise the element of individual item
        holder.nameofFile.setText(items.get(position));
        }

@Override
public int getItemCount() {// return the no of item
        return items.size();
        }

    public void removeItem(String url) {
        int i = items.indexOf(url);
        items.remove(url);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()

        //database removed
        Log.d("My", url);

        DatabaseReference demo = mReference.child(url);

        demo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        notifyItemRemoved(i);
    }

// viewholder is used to create individual item of recyclerview
// and going to store in cost ponding object
public class ViewHolder extends  RecyclerView.ViewHolder{
    TextView nameofFile;

    String email;

    public ViewHolder(View itemView) {//represts individual list items
        super(itemView);
        //creating object of tetview
        nameofFile=itemView.findViewById(R.id.nameofFile);

        itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {//what happens when you click individual item view

                                            int position=recyclerView.getChildLayoutPosition(view);
                                            Intent intent=new Intent();
                                            intent.setType(Intent.ACTION_VIEW);//denotes that we are going to view something
                                            intent.setData(Uri.parse(urls.get(position)));
                                            context.startActivity(intent);
                                        }
                                    });
        FirebaseAuth auth;
        FirebaseUser currentuser;

        auth = FirebaseAuth.getInstance();
        currentuser = auth.getCurrentUser();
        if (currentuser != null) {
            email = currentuser.getEmail();
        }
        if (email.equals("baigshamshir@gmail.com")){
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Do you want to Delete this data ?").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                removeItem(items.get(getAdapterPosition()));


                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.setTitle("Confirm");
                dialog.show();
                return false;
            }
        });}
    }
}
}
