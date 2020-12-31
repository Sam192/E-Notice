package com.example.hp.test;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class maintaince_Adapter extends RecyclerView.Adapter<maintaince_Adapter.MyViewHolder> {
    Context context;
    ArrayList<maintainence_class> maintain;

    private maintaince_Adapter.OnItemClickListner mListner;

    public maintaince_Adapter(Context c,ArrayList<maintainence_class> m)
    {
        context=c;
        maintain=m;

    }
    @NonNull
    @Override
    public maintaince_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.maintaince_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull maintaince_Adapter.MyViewHolder holder, int position) {
        holder.room_no.setText(maintain.get(position).getRoom());
        holder.date.setText(maintain.get(position).getSaveCurrentDate());
        holder.time.setText(maintain.get(position).getSaveCurrentTime());
        holder.imag=maintain.get(position).getImageAddress();
        holder.dis=maintain.get(position).getDiscription();

    }

    @Override
    public int getItemCount() {
        return maintain.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener,View.OnCreateContextMenuListener,MenuItem.OnMenuItemClickListener{
        TextView room_no,date,time;
        String dis,imag,email;
        public MyViewHolder(final View itemView) {
            super(itemView);
            room_no=(TextView) itemView.findViewById(R.id.room_no);
            date=(TextView) itemView.findViewById(R.id.maintaince_date);
            time=(TextView) itemView.findViewById(R.id.maintaince_time);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String room=room_no.getText().toString();
                    final String dat=date.getText().toString();
                    final String tim=time.getText().toString();
                    final String des=dis.toString();
                    final String img=imag.toString();
                    Log.d("Title",room);
                    //  int position = recyclerview.getChildLayoutPosition(v);
                    Intent intent = new Intent(context,maintaince_details.class);
                    intent.putExtra("Title",room);
                    intent.putExtra("Details",des);

                    intent.putExtra("img",img);
                    intent.putExtra("date",dat);
                    intent.putExtra("tim",tim);
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
            itemView.setOnLongClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
            }
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

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem delete=menu.add(Menu.NONE,1,1,"Delete Post");
            delete.setOnMenuItemClickListener(this);
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



    }
    public interface OnItemClickListner{
        void OnItemClick(int position);

        void onDeleteClick(int position);
    }
    public void setOnItemClickListner(maintaince_Adapter.OnItemClickListner listner){
        mListner=listner;
    }
}
