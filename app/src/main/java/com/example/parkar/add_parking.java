package com.example.parkar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.Manifest;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkar.model.global_parking_model;
import com.example.parkar.model.user_registration_model;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class add_parking extends AppCompatActivity {
    private static final int REQUEST_LOCATION = 1;
    Button btnGetLocation;
    TextView showLocation;
    private GpsTracker gpsTracker;
    ProgressDialog mdialog;
    double latitude , longitude;
    String name,address,space,charges,paid_free_txt;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_parking);
        mdialog  = new ProgressDialog(add_parking.this);
        mdialog.setTitle("register");
        mdialog.setMessage("Registering Please wait");
        showLocation = findViewById(R.id.viewLoc);
        btnGetLocation = findViewById(R.id.get_loc);
        Button add_parking = findViewById(R.id.add_parking_add);
        TextView Parking_name = findViewById(R.id.add_parking_name);
        TextView Parking_address = findViewById(R.id.add_parking_address);
        TextView Parking_space = findViewById(R.id.add_parking_space);
        TextView Parking_charges = findViewById(R.id.add_parking_charges);
        RadioGroup paid_free = findViewById(R.id.radioGroup);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("global_parking");
        mAuth = FirebaseAuth.getInstance();

        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        add_parking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               name=  Parking_name.getText().toString();
                address = Parking_address.getText().toString();
                space = Parking_space.getText().toString();
                charges = Parking_charges.getText().toString();
                int selectedId = paid_free.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) findViewById(selectedId);
                paid_free_txt = radioButton.getText().toString();
                mdialog.show();
                mAuth.getCurrentUser().getUid();
                String user_id = mAuth.getCurrentUser().getUid();
                String id = myRef.push().getKey();

                global_parking_model data = new global_parking_model(name, latitude,longitude,address,space,paid_free_txt,charges,id,user_id);
                myRef.child(id).setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_SHORT).show();
                        mdialog.dismiss();
                        startActivity(new Intent(getApplicationContext(), ViewParking.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("", "error " + e);
                    }
                });


            }
        });



        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }
    public void getLocation(View view){
        gpsTracker = new GpsTracker(add_parking.this);
        if(gpsTracker.canGetLocation()){
             latitude = gpsTracker.getLatitude();
             longitude = gpsTracker.getLongitude();
            showLocation.setText("Latitude: "+latitude+"Longitude: "+longitude);
        }else{
            gpsTracker.showSettingsAlert();
        }
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