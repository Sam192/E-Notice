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

public class Notice_Adapter extends RecyclerView.Adapter<Notice_Adapter.MyViewHolder> {
    Context context;
    ArrayList<notice> Notice;
    private Notice_Adapter.OnItemClickListner mListner;

    public Notice_Adapter(Context c,ArrayList<notice> n)
    {
        context=c;
        Notice=n;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.notice_items,parent,false));

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.title.setText(Notice.get(position).getTopic());
        holder.date.setText(Notice.get(position).getSaveCurrentDate());
        holder.time.setText(Notice.get(position).getSaveCurrentTime());
        holder.dess=Notice.get(position).getDiscription();
        holder.pdff=Notice.get(position).getPdf();
        holder.imgg=Notice.get(position).getImageAddress();
    }

    @Override
    public int getItemCount() {
        return Notice.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener,View.OnCreateContextMenuListener,MenuItem.OnMenuItemClickListener{
        TextView title,date,time;
        String dess,pdff,imgg;
        String email;
        public MyViewHolder(View itemView) {
            super(itemView);
            title=(TextView) itemView.findViewById(R.id.notice_Title);
            date=(TextView) itemView.findViewById(R.id.notice_date);
            time=(TextView) itemView.findViewById(R.id.notice_time);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String tit=title.getText().toString();
                    final String dat=date.getText().toString();
                    final String tim=time.getText().toString();
                    final String des=dess.toString();
                    final String pdf=pdff.toString();
                    final String img=imgg.toString();
                    Log.d("Title",tit);
                  //  int position = recyclerview.getChildLayoutPosition(v);
                    Intent intent = new Intent(context,notice_detail.class);
                    intent.putExtra("Title",tit);
                    intent.putExtra("Details",des);
                    intent.putExtra("pdf",pdf);
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
            if (email.equals("baigshamshir@gmail.com")) {
                itemView.setOnLongClickListener(this);
                itemView.setOnCreateContextMenuListener(this);
            }
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
            MenuItem delete=menu.add(Menu.NONE,1,1,"Delete Notice");
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
    public void setOnItemClickListner(Notice_Adapter.OnItemClickListner listner){
        mListner=listner;
    }

}


