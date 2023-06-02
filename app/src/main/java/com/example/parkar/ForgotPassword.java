package com.example.parkar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    Button fpass;
    private FirebaseAuth mAuth;
    private ProgressDialog mdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        fpass = findViewById(R.id.forgot_reset);
        mAuth = FirebaseAuth.getInstance();
        mdialog = new ProgressDialog(this);
        TextView fp = findViewById(R.id.forgot_id);

        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        fpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mdialog.setMessage("processing");
                mdialog.show();
                final String fpemail = fp.getText().toString().trim();
                if (TextUtils.isEmpty(fpemail)) {
                    fp.setError("Required Field");
                    Toast.makeText(getApplicationContext(), "Enter Proper Email", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.sendPasswordResetEmail(fpemail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Mail has been sent to your mail id to reset your Password", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), Login.class));

                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);


                            } else {
                                String message = task.getException().getMessage();
                                Toast.makeText(getApplicationContext(), "Error Occured" + message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}