package com.example.parkar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    Button login, register, forgot;
    private FirebaseAuth mAuth;
    ProgressDialog mdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //initialise the dialog box
        getSupportActionBar().hide();
        mdialog  = new ProgressDialog(Login.this);
        mdialog.setTitle("Login");
        mdialog.setMessage("Logging in Please wait");
        Button login = findViewById(R.id.login_login);
        TextInputEditText email = findViewById(R.id.login_email);
        TextInputEditText password = findViewById(R.id.login_pass);

        Button register = findViewById(R.id.login_register);
        Button forgot = findViewById(R.id.login_forgot);
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),User_dashboard.class));
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String memail = email.getText().toString().trim();
                String mpass = password.getText().toString().trim();
                if (TextUtils.isEmpty(memail)) {
                    email.setError("required Field...");
                    return;
                } else if (TextUtils.isEmpty(mpass)) {
                    password.setError("Required Field");
                    return;
                } else {
                    mdialog.setMessage("Processing..");
                    mdialog.show();
                    //Sign in with email and password
                    mAuth.signInWithEmailAndPassword(memail, mpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                mdialog.dismiss();
                                startActivity(new Intent(getApplicationContext(), User_dashboard.class));
//                                Toast.makeText(getApplicationContext(), "login complete", Toast.LENGTH_SHORT).show();
                            } else {
                                mdialog.dismiss();
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Registration.class);
                startActivity(intent);
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ForgotPassword.class);
                startActivity(intent);
            }
        });
    }
}