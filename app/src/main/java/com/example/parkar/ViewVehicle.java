package com.example.parkar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.parkar.adapter.vehicle_adapter;
import com.example.parkar.model.add_vehicle_model;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewVehicle extends AppCompatActivity {
    private List<add_vehicle_model> vehicleData;
    private RecyclerView rv;
    private vehicle_adapter adapter;// Create object of the
    // Firebase Realtime Database
    private FirebaseAuth mAuth;
    FloatingActionButton addvehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_vehicle);

        mAuth = FirebaseAuth.getInstance();
        //recycler
        rv=(RecyclerView)findViewById(R.id.recycler1);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        vehicleData=new ArrayList<>();
        addvehicle = findViewById(R.id.add_vehicle_fab);

        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        final DatabaseReference nm= FirebaseDatabase.getInstance().getReference("vehicle");
        nm.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String curuid = mAuth.getCurrentUser().getUid();
                if (dataSnapshot.exists()){
                    for(DataSnapshot id:dataSnapshot.getChildren()){
                        String ID = id.child("vehicle_owner_id").getValue().toString();
                        String number=id.child("vehicle_number").getValue().toString();
                        String model =id.child("vehicle_model").getValue().toString();
                        String nickname =id.child("vehicle_nickname").getValue().toString();
                        String type =id.child("vehicle_type").getValue().toString();
                        String society =id.child("vehicle_societycode").getValue().toString();
                        add_vehicle_model work=new add_vehicle_model(model,nickname,number,"",ID,society,type);
                       if(curuid.equals(ID)) {
                           vehicleData.add(work);
                           adapter = new vehicle_adapter(vehicleData);
                           rv.setAdapter(adapter);
                      }

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
           }
        });

        addvehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AddVehicles.class);
                startActivity(i);
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
