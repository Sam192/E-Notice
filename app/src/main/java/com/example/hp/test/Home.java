package com.example.hp.test;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



public class Home extends AppCompatActivity{

    private   FirebaseAuth firebaseAuth;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    public static final String TEXT="text";
    public static final String SHARED_PREFS="sharedPrefs";
    String email;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        FirebaseAuth auth;
        FirebaseUser currentuser;

        auth = FirebaseAuth.getInstance();
        currentuser = auth.getCurrentUser();
        if (currentuser != null) {
            email = currentuser.getEmail();
        }
        if ("baigshamshir@gmail.com".equalsIgnoreCase(email)) {
            getMenuInflater().inflate(R.menu.admin_menu, menu);
            return true;
        }
        return true;
    }



    @Override
    public void setContentView(int layoutResID) {
        DrawerLayout fullview=(DrawerLayout) getLayoutInflater().inflate(R.layout.activity_home,null);
            FrameLayout activityContainer= fullview.findViewById(R.id.flContent);
            getLayoutInflater().inflate(layoutResID,activityContainer,true);
        super.setContentView(fullview);

            toolbar = findViewById(R.id.toolbar);

            setSupportActionBar(toolbar);

            final ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_list);


            firebaseAuth = FirebaseAuth.getInstance();

            if (firebaseAuth.getCurrentUser() == null) {
                finish();
                startActivity(new Intent(this, login.class));
            }
            FirebaseUser user = firebaseAuth.getCurrentUser();


            drawerLayout = findViewById(R.id.drawer_Layout);
            navigationView = findViewById(R.id.navigationView);

            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()) {
                        case R.id.nav_notice:
                            item.setChecked(true);
                            toolbar.setTitle("  Notice");
                            startActivity(new Intent(getApplicationContext(), notice_recyclerview.class));
                            break;

                        case R.id.nav_notes:
                            item.setChecked(true);
                            toolbar.setTitle("  Notes");
                            startActivity(new Intent(getApplicationContext(), notes.class));
                            break;

                        case R.id.nav_book:
                            item.setChecked(true);
                            toolbar.setTitle("  Sell & Donate");
                            Intent c = new Intent(Home.this, selling_swipeview.class);
                            startActivity(c);
                            break;


                        case R.id.nav_lost:
                            item.setChecked(true);
                            toolbar.setTitle("  Lost & Found");
                            Intent d = new Intent(Home.this,lost_found.class);
                            startActivity(d);
                            break;

                        case R.id.nav_event:
                            item.setChecked(true);
                            toolbar.setTitle("  Events");
                            Intent e = new Intent(Home.this,event_recyclerview.class);
                            startActivity(e);
                            break;



                        case R.id.nav_maintainence:
                            item.setChecked(true);
                            toolbar.setTitle("  Maintainence");
                            Intent g = new Intent(Home.this,maitainance.class);
                            startActivity(g);
                            break;

                        case R.id.nav_logout:
                            firebaseAuth.signOut();
                            finish();

                            SharedPreferences.Editor editor=getSharedPreferences(SHARED_PREFS,MODE_PRIVATE).edit();
                            editor.remove(TEXT);
                            editor.apply();
                            startActivity(new Intent(Home.this,login.class));

                            break;
                        case R.id.nav_share:
                            item.setChecked(true);
                            Intent myIntent = new Intent(Intent.ACTION_SEND);
                            myIntent.setType("text/plain");
                            String sharebody = "Your body here";
                            String shareSub = "www.vesit.com";
                            myIntent.putExtra(Intent.EXTRA_SUBJECT,sharebody);
                            myIntent.putExtra(Intent.EXTRA_TEXT,shareSub);
                            startActivity(Intent.createChooser(myIntent,"Share using"));
                            break;

                    }

                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                }
            });


        }

        @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       switch (item.getItemId())
        {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;

       }
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.admin_notice) {

            Intent c = new Intent(Home.this, upload_Notice.class);
            startActivity(c);
            return true;
        }
        else  if (id == R.id.admin_notes) {

            Intent c = new Intent(Home.this, Upload_notesfile.class);
            startActivity(c);
            return true;
        }
        else  if (id == R.id.admin_lost) {

            Intent c = new Intent(Home.this, upload_lost_found.class);
            startActivity(c);
            return true;
        }
        else  if (id == R.id.admin_event) {

            Intent c = new Intent(Home.this, upload_event.class);
            startActivity(c);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

}
