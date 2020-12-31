package com.example.hp.test;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class Sell_viewPagerAdapter extends FragmentPagerAdapter {

    Context context;
    public Sell_viewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context=context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                BlankFragment tab1=new BlankFragment();
                return tab1;
            case 1:
                donate tab2=new donate();
                return tab2;
            case 2:
                mypost tab3=new mypost();
                return  tab3;
            case 3:
                my_donation tab4=new my_donation();
                return tab4;
        }
        return  null;

    }



    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Sell";
            case 1:
                return "Donate";
            case 2:
                return "My Post";
            case 3:
                return "My Donation";
        }
        return null;

    }
}
