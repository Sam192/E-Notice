package com.example.hp.test;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class my_donation_adapter extends   RecyclerView.Adapter<my_donation_adapter.MyViewHolder> {
    Context context;
    ArrayList<donate_image> donate_images;
    private my_donation_adapter.OnItemClickListner mListner;
    public my_donation_adapter (Context c,ArrayList<donate_image> p)
    {
        context=c;
        donate_images=p;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.donate_cardview,parent,false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.Discription.setText(donate_images.get(position).getDiscription());
        Picasso.get().load(donate_images.get(position).getImageAddress()).fit().into(holder.image);

    }

    @Override
    public int getItemCount() {
        return donate_images.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener,View.OnCreateContextMenuListener,MenuItem.OnMenuItemClickListener{
        TextView Discription;
        ImageView image;


        public MyViewHolder(View itemView) {
            super(itemView);
            Discription=(TextView) itemView.findViewById(R.id.Discription);
            image=(ImageView) itemView.findViewById(R.id.image);

                itemView.setOnLongClickListener(this);
                itemView.setOnCreateContextMenuListener(this);

        }
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if(mListner!=null)
            {
                int position =getAdapterPosition();
                if(position!=RecyclerView.NO_POSITION)
                {
                    switch (item.getItemId()){
                        case 1:
                            mListner.onDeleteClick(position);
                            return true;
                    }
                }
            }
            return false;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem delete=menu.add(Menu.NONE,1,1,"Delete Post");
            delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            if(mListner!=null)
            {
                int position =getAdapterPosition();
                if(position!=RecyclerView.NO_POSITION)
                {
                    mListner.OnItemClick(position);
                }
            }
            return false;
        }


    }
    public interface OnItemClickListner{
        void OnItemClick(int position);

        void onDeleteClick(int position);
    }
    public void setOnItemClickListner(my_donation_adapter.OnItemClickListner listner){
        mListner=listner;
    }

}