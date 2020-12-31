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

public class lost_item_Adapter extends RecyclerView.Adapter<lost_item_Adapter.MyViewHolder> {
  Context context;
  ArrayList <Upload> lost_image;
    private lost_item_Adapter.OnItemClickListner mListner;
  public lost_item_Adapter(Context c,ArrayList<Upload> L)
  {
      context=c;
      lost_image=L;
  }


    @Override
    public lost_item_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return  new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.lost_found_item_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(lost_item_Adapter.MyViewHolder holder, int position) {
        holder.lost_discription.setText(lost_image.get(position).getName());
        Picasso.get().load(lost_image.get(position).getImageUrl()).fit().into(holder.lost_image);
    }

    @Override
    public int getItemCount() {
        return lost_image.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener,View.OnCreateContextMenuListener,MenuItem.OnMenuItemClickListener{
        TextView  lost_discription;
        ImageView lost_image;
        String email;

        public MyViewHolder(View itemView) {
            super(itemView);
            lost_discription=(TextView) itemView.findViewById(R.id.lost_discription);
            lost_image=(ImageView) itemView.findViewById(R.id.lost_image);
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
    public void setOnItemClickListner(lost_item_Adapter.OnItemClickListner listner){
        mListner=listner;
    }
}
