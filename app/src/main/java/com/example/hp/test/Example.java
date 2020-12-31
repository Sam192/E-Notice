package com.example.hp.test;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;

public class Example extends AppCompatActivity implements View.OnClickListener {

    private Button bRegister;
    private EditText etEmail;
    private TextInputEditText etPassword, confPassword;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private TextView textView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);

        toolbar = findViewById(R.id.register_include);
        toolbar.setTitle("  Register");
        setSupportActionBar(toolbar);
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(getApplicationContext(), notice_recyclerview.class));

        }


        progressDialog = new ProgressDialog(this);
        bRegister = (Button) findViewById(R.id.bLogin);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (TextInputEditText) findViewById(R.id.etPassword);
        confPassword = (TextInputEditText) findViewById(R.id.confPassword);
        textView = (TextView) findViewById(R.id.textView);
        bRegister.setOnClickListener(this);
        textView.setOnClickListener(this);
    }

    private void registerUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {

            Toast.makeText(this, "please enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "please enter password", Toast.LENGTH_SHORT).show();
            return;

        }
        progressDialog.setMessage("Registering you");
        progressDialog.setCancelable(false);
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        finish();
                                        startActivity(new Intent(getApplicationContext(), login.class));
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "Registration Successful. Please check your email", Toast.LENGTH_LONG).show();
                                        firebaseAuth.signOut();
                                        etEmail.setText("");
                                        etPassword.setText("");
                                        confPassword.setText("");
                                    } else {
                                        etEmail.setText("");
                                        etPassword.setText("");
                                        confPassword.setText("");
                                        Toast.makeText(getApplicationContext(), "Verification Link will be send shortly to your Registered Email", Toast.LENGTH_LONG).show();

                                    }
                                }
                            });
                        } else {
                            Toast.makeText(Example.this, "could not register", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
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

    private boolean validateConfirmPassword() {
        String confirmPassword = confPassword.getText().toString().trim();
        if (confirmPassword.isEmpty()) {
            confPassword.setError("Confirm Password field can not be empty");
            return false;
        } else if (etPassword.getText().toString().trim() == confirmPassword) {
            confPassword.setError("Please does not match");
            return false;
        } else {
            confPassword.setError(null);
            return true;
        }
    }

    @Override
    public void onClick(View view) {
        if (view == bRegister) {
            if(!validateEmail()|| !validatePassword() || !validateConfirmPassword())
            {
                Toast.makeText(getApplicationContext(), "Please provide valid information", Toast.LENGTH_LONG).show();

            }else {
                registerUser();
            }

        }
        if (view == textView) {
            startActivity(new Intent(this, login.class));
        }

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