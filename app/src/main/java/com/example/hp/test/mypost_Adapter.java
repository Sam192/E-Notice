package com.example.hp.test;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class mypost_Adapter extends  RecyclerView.Adapter<mypost_Adapter.MyViewHolder> {
    Context context;
    ArrayList<mypost_user> mypost_user;

    private mypost_Adapter.OnItemClickListner mListner;

    public mypost_Adapter(Context c, ArrayList<mypost_user> p) {
        context = c;
        mypost_user = p;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.mypost_cardview,parent,false));
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.Price.setText(mypost_user.get(position).getPrice());
        holder.Discription.setText(mypost_user.get(position).getDiscription());
        Picasso.get().load(mypost_user.get(position).getImageAddress()).fit().into(holder.image);
    }
    @Override
    public int getItemCount() {
        return mypost_user.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener,View.OnCreateContextMenuListener,MenuItem.OnMenuItemClickListener{
        TextView Price,Discription;
        ImageView  image;

        public MyViewHolder(View itemView) {
            super(itemView);
            Price=(TextView) itemView.findViewById(R.id.Price);
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
    public void setOnItemClickListner(mypost_Adapter.OnItemClickListner listner){
        mListner=listner;
    }


}

