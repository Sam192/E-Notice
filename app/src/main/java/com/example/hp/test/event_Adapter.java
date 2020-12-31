package com.example.hp.test;

import android.content.Context;
import android.content.Intent;
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

public class event_Adapter extends RecyclerView.Adapter<event_Adapter.MyViewHolder> {
    Context context;
    ArrayList<event_class> Event;
    private event_Adapter.OnItemClickListner mListner;
    public event_Adapter(Context c,ArrayList<event_class> e){
        context=c;
        Event=e;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.event_item,parent,false));

    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.title.setText(Event.get(position).getTopic());
        holder.date.setText(Event.get(position).getSaveCurrentDate());
        holder.time.setText(Event.get(position).getSaveCurrentTime());
        holder.email.setText(Event.get(position).getEmail());
        holder.dess=Event.get(position).getDiscription();
        holder.pdff=Event.get(position).getPdf();
        holder.imgg=Event.get(position).getImageAddress();
    }
    @Override
    public int getItemCount() {
        return Event.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener,View.OnCreateContextMenuListener,MenuItem.OnMenuItemClickListener{
        TextView title,date,time,email;
        String dess,pdff,imgg,useremail;
        public MyViewHolder(View itemView) {
            super(itemView);
            title=(TextView) itemView.findViewById(R.id.event_Title);
            email=(TextView) itemView.findViewById(R.id.email);
            date=(TextView) itemView.findViewById(R.id.event_date);
            time=(TextView) itemView.findViewById(R.id.event_time);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String tit=title.getText().toString();
                    final String dat=date.getText().toString();
                    final String tim=time.getText().toString();
                    final String des=dess.toString();
                    final String pdf=pdff.toString();
                    final String img=imgg.toString();
                    final String em=email.getText().toString();
                    Log.d("Title",tit);
                    //  int position = recyclerview.getChildLayoutPosition(v);
                    Intent intent = new Intent(context,event_details.class);
                    intent.putExtra("Title",tit);
                    intent.putExtra("Details",des);
                    intent.putExtra("email",em);
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
                useremail = currentuser.getEmail();
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
            MenuItem delete=menu.add(Menu.NONE,1,1,"Delete Event");
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
    public void setOnItemClickListner(event_Adapter.OnItemClickListner listner){
        mListner=listner;
    }

}
