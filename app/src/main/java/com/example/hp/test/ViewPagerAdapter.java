package com.example.hp.test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import static android.support.v4.content.ContextCompat.startActivity;

    public class ViewPagerAdapter extends FragmentPagerAdapter {

    Context context;
    public ViewPagerAdapter(FragmentManager fm,Context context) {
        super(fm);
        this.context=context;
    }

    @Override
    public Fragment getItem(int position) {
        notesfragment notesfragment=new notesfragment();
       // BlankFragment BlankFragment= new BlankFragment();
        position=position+1;
       // Bundle bundle=new Bundle();
       // bundle.putString("message","Fragment :"+position);
      //  notesfragment.setArguments(bundle);


        if(position==1)
        {
            sem1_Notes sem1=new sem1_Notes();
           // Intent readMore = new Intent(context, Upload_notesfile.class);
           // context.startActivity(readMore);
            return sem1;
        }
        if(position==2)
        {
           sem2 sem2=new sem2();
           return sem2;
        }
        if (position==3)
        {
            sem3 sem3=new sem3();
            return sem3;
        }
        if (position==4)
        {
            sem4  sem4=new sem4();
            return sem4;
        }
        if (position==5)
        {
            sem5  sem5=new sem5();
            return sem5;
        }
        if (position==6)
        {
           sem6 sem6=new sem6();
           return sem6;
        }
        return notesfragment;

    }



    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        position=position+1;

       /* if(position==position+1){
        return "F.Y.IT";}
        if(position==position+2){
        return "S.Y.IT";}
        if(position==position+3){
            return "T.Y.IT";
        }*/

    return "sem " +position;}


}




