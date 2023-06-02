package com.example.parkar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.parkar.model.user_registration_model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;


public class Registration extends AppCompatActivity {
    private FirebaseAuth mAuth;
    ProgressDialog mdialog;
    Button registerButton, loginButton;
    String u_id;
    String u_name;
    String u_phone;
    String u_email;
    String u_address;
    String u_society;
    String u_dob;
    String u_whatsapp;
    String u_password;
    String female_male_txt;
    DatePickerDialog datePickerDialog;

    EditText id;
    TextInputEditText name , phone, email, address, society, dob, whatsapp, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        getSupportActionBar().hide();
        mdialog  = new ProgressDialog(Registration.this);
        mdialog.setTitle("register");
        mdialog.setMessage("Registering Please wait");
        registerButton = findViewById(R.id.reg_register);
        loginButton = findViewById(R.id.reg_login);
        RadioGroup female_male = findViewById(R.id.radiogroup1);

        name = findViewById(R.id.reg_name);

        phone = findViewById(R.id.reg_phone);

        email = findViewById(R.id.reg_email);

        address = findViewById(R.id.reg_address);

        society = findViewById(R.id.reg_societycode);

        dob = findViewById(R.id.reg_dob);

        whatsapp = findViewById(R.id.reg_wano);

        password = findViewById(R.id.reg_password);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("user");
        mAuth = FirebaseAuth.getInstance();

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                Toast.makeText(Registration.this, "Im Here", Toast.LENGTH_SHORT).show();
                datePickerDialog = new DatePickerDialog(Registration.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                dob.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                u_name = name.getText().toString();
                u_phone = phone.getText().toString();
                u_email = email.getText().toString();
                u_address = address.getText().toString();
                u_society = society.getText().toString();
                u_dob = dob.getText().toString();
                int selectedId = female_male.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) findViewById(selectedId);
                female_male_txt = radioButton.getText().toString();

                u_whatsapp = whatsapp.getText().toString();
                u_password = password.getText().toString();

                mdialog.show();
                //create user with firebase
                mAuth.createUserWithEmailAndPassword(u_email, u_password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        mAuth.getCurrentUser().getUid();
                        String id = mAuth.getCurrentUser().getUid();
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        //set Users display name
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(u_name)
                                .build();

                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            //add success message
                                        }
                                    }
                                });
                        //sending data to  node

                        //Toast.makeText(getApplicationContext(),uid,Toast.LENGTH_SHORT).show();
                        user_registration_model data = new user_registration_model(id, u_name, u_phone, u_email, u_address, u_society, u_dob, female_male_txt, u_whatsapp);
                        myRef.child(id).setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_SHORT).show();
                                mdialog.dismiss();
                                startActivity(new Intent(getApplicationContext(), Login.class));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("", "error " + e);
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mdialog.dismiss();
                        Toast.makeText(getApplicationContext(), "problem" + e, Toast.LENGTH_SHORT).show();
                    }
                });


            }
    });

    }

}