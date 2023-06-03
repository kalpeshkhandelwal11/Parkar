package com.example.parkar;

import androidx.annotation.ContentView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class Login extends AppCompatActivity {
    Button login, register, forgot;
    private FirebaseAuth mAuth;
    ProgressDialog mdialog;
    boolean isKeyboardShowing = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //initialise the dialog box
        getSupportActionBar().hide();
        mdialog = new ProgressDialog(Login.this);
        mdialog.setTitle("Login");
        mdialog.setMessage("Logging in Please wait");
        Button login = findViewById(R.id.login_login);
        TextInputEditText email = findViewById(R.id.login_email);
        TextInputEditText password = findViewById(R.id.login_pass);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        Button register = findViewById(R.id.login_register);
        ImageView logo = findViewById(R.id.login_logo);
        TextView logo_name = findViewById(R.id.logo_name);
        View contentView = this.findViewById(android.R.id.content);
        // ContentView is the root view of the layout of this activity/fragment
        contentView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        Rect r = new Rect();
                        contentView.getWindowVisibleDisplayFrame(r);
                        int screenHeight = contentView.getRootView().getHeight();

                        // r.bottom is the position above soft keypad or device button.
                        // if keypad is shown, the r.bottom is smaller than that before.
                        int keypadHeight = screenHeight - r.bottom;


                        if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                            // keyboard is opened
                            if (!isKeyboardShowing) {
                                isKeyboardShowing = true;
                                logo.setVisibility(View.GONE);
                                logo_name.setVisibility(View.GONE);

                            }
                        }
                        else {
                            // keyboard is closed
                            if (isKeyboardShowing) {
                                isKeyboardShowing = false;
                                logo.setVisibility(View.VISIBLE);
                                logo_name.setVisibility(View.VISIBLE);

                            }
                        }
                    }
                });
        logo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                logo.setVisibility(View.GONE);
            }
        });
        logo_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                logo_name.setVisibility(View.GONE);
            }
        });
        Button forgot = findViewById(R.id.login_forgot);
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), User_dashboard.class));
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