package com.example.parkar;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
//import com.otaliastudios.cameraview.CameraView;

import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.util.concurrent.ListenableFuture;
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

    public static boolean isValidCarNo(String NUMBERPLATE)
    {

        // Regex to check valid Indian Vehicle Number Plate
        String regex
                = "^[A-Z]{2}[\\ -]{0,1}[0-9]{2}[\\ -]{0,1}[A-Z]{1,2}[\\ -]{0,1}[0-9]{4}$";
        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the  Indian Vehicle Number Plate
        // is empty return false
        if (NUMBERPLATE == null) {
            return false;
        }

        // Pattern class contains matcher() method
        // to find matching between given
        // Indian Vehicle Number Plate Validation  using
        // regular expression.
        Matcher m = p.matcher(NUMBERPLATE);

        // Return if the  Indian Vehicle Number Plate
        // matched the ReGex
        return m.matches();
    }
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    ImageView vehicle_logo;
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
        vehicle_logo  = findViewById(R.id.add_vehicle_image);
        vehicleNumber.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                // No errors need to be handled for this Future.
                // This should never be reached.
            }
        }, ContextCompat.getMainExecutor(this));
        vehicleNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(isValidCarNo(vehicleNumber.getText().toString())){
                    add.setEnabled(true);
                }else{
                    add.setEnabled(false);
                }
            }
        });
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
    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {



        PreviewView cameraView  = findViewById(R.id.previewView);
        cameraView.setImplementationMode(PreviewView.ImplementationMode.COMPATIBLE);


        cameraView.getOverlay().add(findViewById(R.id.overlay_test));
        Preview preview = new Preview.Builder()
                .build();

        preview.setSurfaceProvider(cameraView.getSurfaceProvider());
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();
        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, preview);
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