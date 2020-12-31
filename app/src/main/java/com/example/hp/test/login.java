package com.example.hp.test;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity  implements View.OnClickListener

{
    private Button bLogin;
    private EditText etEmail;
    private TextInputEditText etPassword;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private Toolbar toolbar;
    String[]  SPINNERLIST={"Select Your Class","F Y MCA","S Y MCA","T Y MCA"};
    String classname;
    public static final String TEXT="text";
    public static final String SHARED_PREFS="sharedPrefs";
    private TextView go_to_register,forget_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_login);


        if(!isConnected(login.this)) buildDialog(login.this).show();
        else {
            Toast.makeText(login.this,"Welcome", Toast.LENGTH_SHORT).show();
            setContentView(R.layout.activity_login);
        }

        firebaseAuth=FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser()!=null)
        {
            finish();
            startActivity(new Intent(getApplicationContext(),notice_recyclerview.class));

        }

        etEmail=(EditText)findViewById(R.id.etEmail);
        etPassword=(TextInputEditText) findViewById(R.id.etPassword);
        bLogin=(Button) findViewById(R.id.bLogin);
        forget_password=(TextView)findViewById(R.id.forget_password);
        go_to_register=(TextView)findViewById(R.id.go_to_register);
        bLogin.setOnClickListener(this);
        progressDialog= new ProgressDialog(this);
        toolbar=findViewById(R.id.login_include);
        toolbar.setTitle("  Login");
        setSupportActionBar(toolbar);

        final Spinner login_spinner=(Spinner)findViewById(R.id.login_spinner);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, SPINNERLIST);

        login_spinner.setAdapter(arrayAdapter);

        forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(login.this,reset_password.class));
            }
        });
        go_to_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(login.this,Example .class));

            }
        });


        login_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

               if(parent.getItemAtPosition(position).equals("Select Your Class")) {
               }
               else {
                   classname=(String)parent.getItemAtPosition(position);
               }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private  void Userlogin()
    {
        String email =etEmail.getText().toString().trim();
        String password =etPassword.getText().toString().trim();
        if(TextUtils.isEmpty(email)){

            Toast.makeText(this,"please enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"please enter password", Toast.LENGTH_SHORT).show();
            return;

        }
        progressDialog.setMessage("Login in Progress !!!!!");
        progressDialog.show();
        progressDialog.setCancelable(false);

        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            if(firebaseAuth.getCurrentUser().isEmailVerified()) {
                                progressDialog.dismiss();
                                savedata();
                                finish();
                                Intent intent=new Intent(login.this,notice_recyclerview.class);
                                startActivity(intent);
                                }
                            else
                            {   progressDialog.dismiss();
                                Toast.makeText(login.this, "Please verify your email address", Toast.LENGTH_SHORT).show();
                                firebaseAuth.signOut();
                            }
                        }
                    }
                });
    }

    public void savedata(){
        if(classname!=null) {
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(TEXT, classname);
            editor.apply();
        }
    }
    private boolean validateEmail() {
        String emailInput = etEmail.getText().toString().trim();
        if (emailInput.isEmpty()) {
            etEmail.setError("Email field can not be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            etEmail.setError("Please enter a valid email");
            return false;
        } else {
            etEmail.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String passwordInput = etPassword.getText().toString().trim();
        if (passwordInput.isEmpty()) {
            etPassword.setError("Password field can not be empty");
            return false;
        } else if (passwordInput.length() < 8) {
            etPassword.setError("Password too Short atleast 8 chracter");
            return false;
        } else {
            etPassword.setError(null);
            return true;
        }
    }

    @Override
    public void onClick(View view) {
        if(view ==bLogin)
        {
            if(!validateEmail() || !validatePassword()){
                Toast.makeText(login.this,"Provide valid Information",Toast.LENGTH_SHORT).show();

            }
            else {
                if(classname!=null) {
                    Userlogin();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Please Select your class ",Toast.LENGTH_LONG).show();

                }
            }

        }

    }

    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) return true;
        else return false;
        } else
        return false;
    }

    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or wifi to access this. Press ok to Exit");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
            }
        });

        return builder;
    }
    @Override
    public void onBackPressed() {
        final android.app.AlertDialog.Builder demobuilder = new android.app.AlertDialog.Builder(this);
        demobuilder.setMessage("Do you want to exit?");
        demobuilder.setCancelable(false);
        demobuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        demobuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        android.app.AlertDialog alert = demobuilder.create();
        alert.show();
    }
}
