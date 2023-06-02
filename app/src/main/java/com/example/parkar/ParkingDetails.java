package com.example.parkar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ParkingDetails extends AppCompatActivity {
    TextView parking_name , parking_address, parking_space, parking_paid_free,parking_charges;
    Button directions;
    private FirebaseAuth mAuth;
    String parking_ID;
    String latitude , longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_details);
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        parking_name = findViewById(R.id.Parking_name);
                 parking_address = findViewById(R.id.Park_address);
                 parking_space = findViewById(R.id.Parking_spaces);
                 parking_paid_free = findViewById(R.id.Parking_paid_free);
        parking_charges = findViewById(R.id.Parking_charges);

        directions = findViewById(R.id.Directions);

        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        parking_ID = getIntent().getStringExtra("EXTRA_SESSION_ID");
        final DatabaseReference nm= FirebaseDatabase.getInstance().getReference("global_parking/"+parking_ID);
        nm.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("park_name").getValue().toString();
                String address = snapshot.child("address").getValue().toString();
                String id = snapshot.child("id").getValue().toString();
                 latitude = snapshot.child("latitude").getValue().toString();
                 longitude = snapshot.child("longitude").getValue().toString();
                String space =  snapshot.child("no_of_parking").getValue().toString();
                String paid_free =  snapshot.child("paid_free").getValue().toString();
                String charges =  snapshot.child("charges").getValue().toString();

                fillData(name,address,id,space,paid_free,charges);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        directions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("https://www.google.com/maps/search/"
                                + latitude                                                 //passing the coordinates as default parameters in maps Intent as source
                                + ","
                                + longitude


                        ));
                startActivity(intent);
            }

        });
    }
    public void fillData(String name , String address,String id, String space, String paid_free, String charges){

        parking_name.setText(name);
        parking_address.setText(address);
        parking_space.setText(space);
        parking_paid_free.setText(paid_free);
        parking_charges.setText(charges);
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