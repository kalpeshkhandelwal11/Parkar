package com.example.parkar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.parkar.model.current_vehicle_model;
import com.example.parkar.ui.home.HomeFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddCurrentParking extends AppCompatActivity {
    Spinner vehicle_spinner;
    Button add_parking;
    FirebaseAuth mAuth;
    String user_id;
    DatabaseReference mDatabase,MDbUser;
    List<String> cur_vehicle_data;
    FusedLocationProviderClient mFusedLocationClient;
    int PERMISSION_ID = 44;
    String Latitude;
    String Longitude;
    ProgressDialog mdialog;
    private GpsTracker gpsTracker;
    double latitude , longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_current_parking);

        vehicle_spinner = findViewById(R.id.vehicle_spinner);
        add_parking = findViewById(R.id.curr_parking_button);
        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        mdialog  = new ProgressDialog(AddCurrentParking.this);
        mdialog.setTitle("register");
        mdialog.setMessage("Registering Please wait");

        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        mDatabase = FirebaseDatabase.getInstance().getReference("vehicle");

        cur_vehicle_data = new ArrayList<>();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    for(DataSnapshot id:snapshot.getChildren()){
                        String ID = id.child("vehicle_owner_id").getValue().toString();
                        String number=id.child("vehicle_number").getValue().toString();
                        String model =id.child("vehicle_model").getValue().toString();
                        String nickname =id.child("vehicle_nickname").getValue().toString();
                        String type =id.child("vehicle_type").getValue().toString();
                        String society =id.child("vehicle_societycode").getValue().toString();



                        if(user_id.equals(ID)) {
                            cur_vehicle_data.add(number);

                      ArrayAdapter<String> current_parking_adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,cur_vehicle_data );
                            vehicle_spinner.setAdapter(current_parking_adapter);
                        }

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        add_parking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation(v);
                MDbUser = FirebaseDatabase.getInstance().getReference("user/"+user_id+"/currentParking/"+vehicle_spinner.getSelectedItem());
                current_vehicle_model model = new current_vehicle_model(vehicle_spinner.getSelectedItem().toString(),Latitude,Longitude);
                MDbUser.setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(gpsTracker, "Current Parking Added", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), User_dashboard.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

            }
        });


    }
    public void getLocation(View view){
        gpsTracker = new GpsTracker(AddCurrentParking.this);
        if(gpsTracker.canGetLocation()){
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
            Latitude = String.valueOf(latitude);
            Longitude = String.valueOf(longitude);

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
