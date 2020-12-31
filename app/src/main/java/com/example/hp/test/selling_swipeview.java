package com.example.hp.test;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class selling_swipeview extends Home {

    private ViewPager viewPager;
    private Sell_viewPagerAdapter adapter;
    private TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selling_swipeview);





            viewPager =findViewById(R.id.pager);
            adapter =new Sell_viewPagerAdapter(getSupportFragmentManager(),getApplicationContext());
            viewPager.setAdapter(adapter);

            tabLayout=findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(getApplicationContext(),notice_recyclerview.class));
    }
}
