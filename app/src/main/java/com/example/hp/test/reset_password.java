package com.example.hp.test;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class reset_password extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText etreset;
    private Button reset;
    private TextView click_to_register,click_to_login;
    private FirebaseAuth auth;
    private ProgressDialog msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        toolbar=findViewById(R.id.reset_include );
        etreset=findViewById(R.id.et_reset);
        reset=findViewById(R.id.reset);
        click_to_register=findViewById(R.id.click_to_register);
        click_to_login=findViewById(R.id.click_to_login);
        toolbar.setTitle("  Login");
        setSupportActionBar(toolbar);
        msg = new ProgressDialog(this);

        click_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(reset_password.this,login.class));

            }
        });

        click_to_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(reset_password.this,Example.class));
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String usrmail = etreset.getText().toString().trim();
                if(!validateEmail())
                {
                    return;
                }
                else {
                    msg.setMessage("Sending Reset Link");
                    msg.show();
                    msg.setCancelable(false);
                    auth.sendPasswordResetEmail(usrmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                msg.dismiss();
                                etreset.setText("");
                                Toast.makeText(getApplicationContext(), "Reset Link Sent", Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

            }
        });
    }

    private boolean validateEmail() {
        String emailInput = etreset.getText().toString().trim();
        if (emailInput.isEmpty()) {
            etreset.setError("Email field can not be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            etreset.setError("Please enter a valid email");
            return false;
        } else {
            etreset.setError(null);
            return true;
        }
    }
}
