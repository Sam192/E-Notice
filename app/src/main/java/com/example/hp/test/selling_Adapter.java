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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class selling_Adapter extends RecyclerView.Adapter<selling_Adapter.MyViewHolder>{
    Context context;
    ArrayList<Profile_selling> profile_sellings;

    private selling_Adapter.OnItemClickListner mListner;

    public selling_Adapter(Context c,ArrayList<Profile_selling> p)
    {
        context=c;
        profile_sellings=p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.selling_cardview,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.Price.setText(profile_sellings.get(position).getPrice());
        holder.Discription.setText(profile_sellings.get(position).getDiscription());
        Picasso.get().load(profile_sellings.get(position).getImageAddress()).fit().into(holder.image);
    }

    @Override
    public int getItemCount() {
        return profile_sellings.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener,View.OnCreateContextMenuListener,MenuItem.OnMenuItemClickListener{
        TextView Price,Discription;
        ImageView  image;
        String email;

        public MyViewHolder(View itemView) {
            super(itemView);
            Price=(TextView) itemView.findViewById(R.id.Price);
            Discription=(TextView) itemView.findViewById(R.id.Discription);
            image=(ImageView) itemView.findViewById(R.id.image);
            FirebaseAuth auth;
            FirebaseUser currentuser;

            auth = FirebaseAuth.getInstance();
            currentuser = auth.getCurrentUser();
            if (currentuser != null) {
                email = currentuser.getEmail();
            }
            if (email.equals("baigshamshir@gmail.com")){
            itemView.setOnLongClickListener(this);
            itemView.setOnCreateContextMenuListener(this);}
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
    public void setOnItemClickListner(selling_Adapter.OnItemClickListner listner){
        mListner=listner;

    }
}
