package com.example.parkar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkar.model.add_vehicle_model;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditVehicle extends AppCompatActivity {

    TextView edit_vehicle_number, edit_vehicle_type , edit_vehicle_model, edit_vehicle_nick, edit_vehicle_society;
    private DatabaseReference mDatabase;
    Button update;
    private FirebaseAuth mAuth;
    String vehicle_owner,vehicle_owner_id;
    add_vehicle_model data;
    String vmodel ,vnick,vnumber,vsociety,vtype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_vehicle);
        mAuth = FirebaseAuth.getInstance();

        edit_vehicle_number =  findViewById(R.id.edit_vehicle_number);
        edit_vehicle_type = findViewById(R.id.edit_vehicle_type);
        edit_vehicle_model = findViewById(R.id.edit_vehicle_model);
        edit_vehicle_nick = findViewById(R.id.edit_vehicle_name);
        edit_vehicle_society = findViewById(R.id.edit_vehicle_society_code);

        edit_vehicle_number.setEnabled(false);
        edit_vehicle_type.setEnabled(false);
        edit_vehicle_model.setEnabled(false);
        edit_vehicle_number.setText(getIntent().getStringExtra("vehicle_id"));
        mDatabase = FirebaseDatabase.getInstance().getReference("vehicle/"+edit_vehicle_number.getText().toString());
        vehicle_owner = mAuth.getCurrentUser().getDisplayName().toString();
        update = findViewById(R.id.update_vehicle);
        vehicle_owner_id = mAuth.getCurrentUser().getUid().toString();
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                     vnumber = snapshot.child(("vehicle_number")).getValue().toString();
                     vtype = snapshot.child(("vehicle_type")).getValue().toString();
                     vmodel = snapshot.child(("vehicle_model")).getValue().toString();
                     vnick = snapshot.child(("vehicle_nickname")).getValue().toString();
                     vsociety = snapshot.child(("vehicle_societycode")).getValue().toString();

                     edit_vehicle_number.setText(vnumber);
                     edit_vehicle_type.setText(vtype);
                     edit_vehicle_model.setText(vmodel);
                     edit_vehicle_nick.setText(vnick);
                     edit_vehicle_society.setText(vsociety);

                  //data = new add_vehicle_model(vmodel ,vnick,vnumber,vehicle_owner,vehicle_owner_id,,vtype);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditVehicle.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_vehicle_number.setEnabled(true);
                edit_vehicle_type.setEnabled(true);
                edit_vehicle_model.setEnabled(true);
                add_vehicle_model data = new add_vehicle_model(edit_vehicle_model.getText().toString() ,edit_vehicle_nick.getText().toString(),getIntent().getStringExtra("vehicle_id"),vehicle_owner,vehicle_owner_id,edit_vehicle_society.getText().toString(),edit_vehicle_type.getText().toString());
                edit_vehicle_number.setEnabled(false);
                edit_vehicle_type.setEnabled(false);
                edit_vehicle_model.setEnabled(false);
                mDatabase.setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(EditVehicle.this, "Data Updated Successfully", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(),Vehicles.class);
                        startActivity(i);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditVehicle.this, "Not Updated", Toast.LENGTH_SHORT).show();
                    }
                });
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