package com.example.parkar;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkar.model.add_vehicle_model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class AddVehicles extends AppCompatActivity {
    Button add;
    private FirebaseAuth mAuth;
    TextView  vehicleType, vehicleNumber , vehicleModel , vehicle_NickName , VehicleCode;
    String vehicle_model;
    String vehicle_nickname;
    String vehicle_number;
    String vehicle_owner;
    String vehicle_owner_id;
    String vehicle_societycode;
    String vehicle_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicles);
        add = findViewById(R.id.addv_add);
        vehicleType = findViewById(R.id.addv_vtype);
        vehicleNumber = findViewById(R.id.addv_vnumber);
        vehicleModel = findViewById(R.id.addv_model);
        vehicle_NickName = findViewById(R.id.addv_nickname);
        VehicleCode = findViewById(R.id.addv_securitycode);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            // Create channel to show notifications.
//            String channelId  = getString(R.string.default_notification_channel_id);
//            String channelName = getString(R.string.default_notification_channel_name);
//            NotificationManager notificationManager =
//                    getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
//                    channelName, NotificationManager.IMPORTANCE_LOW));
//        }



        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("vehicle");
                mAuth = FirebaseAuth.getInstance();
                vehicle_model = vehicleModel.getText().toString();
                vehicle_type = vehicleType.getText().toString();
                vehicle_number = vehicleNumber.getText().toString();
                vehicle_nickname = vehicle_NickName.getText().toString();
                vehicle_societycode = VehicleCode.getText().toString();
                vehicle_owner = mAuth.getCurrentUser().getDisplayName().toString();
                vehicle_owner_id = mAuth.getCurrentUser().getUid().toString();

                add_vehicle_model data = new add_vehicle_model(vehicle_model ,vehicle_nickname,vehicle_number,vehicle_owner,vehicle_owner_id,vehicle_societycode,vehicle_type);
                myRef.child(vehicle_number).setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(AddVehicles.this, "Vehicle Added Successfully", Toast.LENGTH_SHORT).show();
                        FirebaseMessaging.getInstance().subscribeToTopic(vehicle_number)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        String msg = "Subscribed";
                                        if (!task.isSuccessful()) {
                                            msg ="Not Subscribbed";
                                        }
                                        Log.d(TAG, msg);
//                                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                    }
                                });

                        Intent intent = new Intent(getApplicationContext(), Vehicles.class);
                        startActivity(intent);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddVehicles.this, e.toString(), Toast.LENGTH_SHORT).show();
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