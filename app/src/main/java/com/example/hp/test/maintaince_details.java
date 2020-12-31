package com.example.hp.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class maintaince_details extends AppCompatActivity {
    TextView room,date,time,discrip;
    ImageView mainatin_image;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintaince_details);
        room=(TextView) findViewById(R.id.room);
        date=(TextView) findViewById(R.id.date);
        time=(TextView) findViewById(R.id.time);
        discrip=(TextView) findViewById(R.id.discrip);
        mainatin_image=(ImageView) findViewById(R.id.mainatin_image);
        toolbar = findViewById(R.id.maintaince_include);
        toolbar.setTitle("  Maintaince Details");
        setSupportActionBar(toolbar);

        String roo=getIntent().getStringExtra("Title");
        String des=getIntent().getStringExtra("Details");

        final String img=getIntent().getStringExtra("img");
        String dat=getIntent().getStringExtra("date");
        String tim=getIntent().getStringExtra("tim");
        room.setText(roo);
        date.setText(dat);
        time.setText(tim);
        discrip.setText(des);
        Picasso.get().load(img).fit().into(mainatin_image);
    }
}
