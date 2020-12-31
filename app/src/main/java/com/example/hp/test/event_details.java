package com.example.hp.test;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class event_details extends AppCompatActivity {
    TextView email_detail,Event_title,event_detail_date,
            event_detail_time,event_detail_description,event_detail_pdf,event_detail_image;
    ImageView image_detail;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        email_detail=(TextView) findViewById(R.id.email_detail);
        Event_title=(TextView) findViewById(R.id.Event_title);
        event_detail_date=(TextView) findViewById(R.id.event_detail_date);
        event_detail_time=(TextView) findViewById(R.id.event_detail_time);
        event_detail_description=(TextView) findViewById(R.id.event_detail_description);
        event_detail_pdf=(TextView) findViewById(R.id.event_detail_pdf);
        event_detail_image=(TextView) findViewById(R.id.event_detail_image);

        image_detail=(ImageView) findViewById(R.id.image_detail);
        toolbar = findViewById(R.id.event_include);
        toolbar.setTitle("  Event Details");
        setSupportActionBar(toolbar);



        String tit=getIntent().getStringExtra("Title");
        String des=getIntent().getStringExtra("Details");
        String em=getIntent().getStringExtra("email");
        final String pdf=getIntent().getStringExtra("pdf");
        final String img=getIntent().getStringExtra("img");
        String dat=getIntent().getStringExtra("date");
        String tim=getIntent().getStringExtra("tim");
        Event_title.setText(tit);
        event_detail_description.setText(des);
        event_detail_date.setText(dat);
        event_detail_time.setText(tim);
        email_detail.setText(em);

        if(pdf!=null)
        {
            event_detail_pdf.setText("Click To download Attachment");
            event_detail_pdf.setTextColor(this.getResources().getColor(R.color.Green));
            event_detail_pdf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // int position=recyclerView.getChildLayoutPosition(view);
                    Intent intent=new Intent();
                    intent.setType(Intent.ACTION_VIEW);//denotes that we are going to view something
                    intent.setData(Uri.parse(pdf));
                    startActivity(intent);
                }
            });
        }

        if(img!=null)
        {    Picasso.get().load(img).fit().into(image_detail);
            event_detail_image.setText("Click on Image To download It ");
            event_detail_image.setTextColor(this.getResources().getColor(R.color.Green));
            image_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent();
                    intent.setType(Intent.ACTION_VIEW);//denotes that we are going to view something
                    intent.setData(Uri.parse(img));
                    startActivity(intent);
                }
            });
        }
    }
}
