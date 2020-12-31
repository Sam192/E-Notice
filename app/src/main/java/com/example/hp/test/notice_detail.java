package com.example.hp.test;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class notice_detail extends AppCompatActivity {
    TextView notice_detail_title,notice_detail_date,notice_detail_time,
            notice_detail_description,notice_detail_pdf,notice_detail_image;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail);
        notice_detail_title=(TextView) findViewById(R.id.notice_detail_title);
        notice_detail_date=(TextView) findViewById(R.id.notice_detail_date);
        notice_detail_time=(TextView) findViewById(R.id.notice_detail_time);
        notice_detail_description=(TextView) findViewById(R.id.notice_detail_description);
        notice_detail_pdf=(TextView) findViewById(R.id.notice_detail_pdf);
        notice_detail_image=(TextView) findViewById(R.id.notice_detail_image);
        toolbar = findViewById(R.id.notice_include);
        toolbar.setTitle("  Notice Details");
        setSupportActionBar(toolbar);

        String tit=getIntent().getStringExtra("Title");
        String des=getIntent().getStringExtra("Details");
        final String pdf=getIntent().getStringExtra("pdf");
        final String img=getIntent().getStringExtra("img");
        String dat=getIntent().getStringExtra("date");
        String tim=getIntent().getStringExtra("tim");
        notice_detail_title.setText(tit);
        notice_detail_description.setText(des);
        notice_detail_date.setText(dat);
        notice_detail_time.setText(tim);
        if(pdf!=null)
        {
            notice_detail_pdf.setText("Click To download Attachment");
            notice_detail_pdf.setTextColor(this.getResources().getColor(R.color.Green));
            notice_detail_pdf.setOnClickListener(new View.OnClickListener() {
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
        {
            notice_detail_image.setText("Click To download Image");
            notice_detail_image.setTextColor(this.getResources().getColor(R.color.Green));
            notice_detail_image.setOnClickListener(new View.OnClickListener() {
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
