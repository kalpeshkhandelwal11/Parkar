package com.example.parkar;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.CameraController;
import androidx.camera.view.LifecycleCameraController;
import androidx.camera.view.PreviewView;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.exifinterface.media.ExifInterface;
import androidx.lifecycle.LifecycleOwner;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.util.Log;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkar.model.add_vehicle_model;
import com.example.parkar.model.user_registration_model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
//import com.otaliastudios.cameraview.CameraView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import id.zelory.compressor.Compressor;


public class AddVehicles extends AppCompatActivity {
    Button add;
    private FirebaseAuth mAuth;
    TextView vehicleType, vehicleNumber, vehicleModel, vehicle_NickName, VehicleCode;
    String baseImageUrl = "https://park-kar-624f3.appspot.com.storage.googleapis.com/";
    String imageUrl = "";
    String vehicle_model;
    String vehicle_nickname;
    String vehicle_number;
    String vehicle_owner;
    String vehicle_owner_id;
    String vehicle_societycode;
    String vehicle_type;
    boolean isFullScreen = false;
    int originalWidth, originalHeight;
    Drawable oldBackground;
    float oldBorderRadius = 50f;
    ViewGroupOverlay oldLayoutViewOverlay;
    View oldLayoutView;
    PreviewView previewView;
    Button cameraShutter;
    ImageView vehicle_logo;
    ImageView overlay;

    private ExecutorService cameraExecutor;
    private ImageCapture imageCapture;
    private static final int REQUEST_CODE_PERMISSIONS = 100;
    private static final String[] REQUIRED_PERMISSIONS = new String[]{Manifest.permission.CAMERA};


    public static boolean isValidCarNo(String NUMBERPLATE) {

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

    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

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
        vehicle_logo = findViewById(R.id.add_vehicle_image);
        vehicleNumber.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        previewView = findViewById(R.id.previewView);
        cameraShutter = findViewById(R.id.cameraShutter);
        overlay = findViewById(R.id.overlay_box);
        cameraShutter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    captureImage();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        if (allPermissionsGranted()) {
            startCamera();
        } else {
            requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }
        vehicleNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (isValidCarNo(vehicleNumber.getText().toString())) {
                    add.setEnabled(true);
                } else {
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
        cameraExecutor = Executors.newSingleThreadExecutor();
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

                add_vehicle_model data = new add_vehicle_model(vehicle_model, vehicle_nickname, vehicle_number, vehicle_owner, vehicle_owner_id, vehicle_societycode, vehicle_type);

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
                                            msg = "Not Subscribbed";
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
                myRef.child(vehicle_number).updateChildren(
                        new HashMap<String, Object>() {
                            {
                                put("vehicle_image", imageUrl);
                            }
                        }
                );
                imageUrl = baseImageUrl + fileName;
                FirebaseStorage storage = FirebaseStorage.getInstance();

                StorageReference storageRef = storage.getReference();
                StorageReference fileRef = storageRef.child("images/" + fileName);
                File compressedImageFile=photoFile;
                try {
                    compressedImageFile = new Compressor(AddVehicles.this).compressToFile(photoFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                UploadTask uploadTask = fileRef.putFile(Uri.fromFile(compressedImageFile));
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // File uploaded successfully
                        // Retrieve the download URL
                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri downloadUrl) {
                                // Handle the download URL
                                imageUrl = downloadUrl.toString();
                                Toast.makeText(AddVehicles.this, "Image sent to firebase", Toast.LENGTH_SHORT).show();
                                Log.i("image-capture-firebase","image sent to firebase");
                                // Perform further operations (e.g., save the URL to a database)
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Error occurred while retrieving the download URL
                                // Handle the error
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error occurred during file upload
                        // Handle the error
                    }
                });


            }
        });
    }
    void setOverlayInApp(){
        try {
            ExifInterface exifInterface = new ExifInterface(photoFile);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

            int rotationDegrees = 0;
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotationDegrees = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotationDegrees = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotationDegrees = 270;
                    break;
            }

            // Load the captured image into a Bitmap
            Bitmap bitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
            int nh = (int) ( bitmap.getHeight() * (512.0 / bitmap.getWidth()) );

            bitmap=Bitmap.createScaledBitmap(bitmap, 512, nh, true);
            bitmap = new Compressor(AddVehicles.this).compressToBitmap(photoFile);

            if (rotationDegrees != 0) {
                // Create a matrix and set the rotation
                Matrix matrix = new Matrix();
                matrix.setRotate(rotationDegrees);
//                                File compressedImageFile = new Compressor(AddVehicles.this).compressToFile(photoFile);
                // Apply the rotation to the bitmap
                Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                // Set the rotated bitmap to the ImageView
                overlay.setImageBitmap(rotatedBitmap);

                // Recycle the original bitmap to free up memory
                bitmap.recycle();
            } else {
                // No rotation needed, directly set the bitmap to the ImageView
                overlay.setImageBitmap(bitmap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        oldLayoutView = findViewById(R.id.overlay_box);
        previewView.setImplementationMode(PreviewView.ImplementationMode.COMPATIBLE);
        previewView.getOverlay().add(findViewById(R.id.overlay_box));
        previewView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                onPreviewClick(view);
                onPreviewClick(findViewById(R.id.container_camera_preview_framelayout));
            }
        });
        Preview preview = new Preview.Builder()
                .build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        imageCapture = new ImageCapture.Builder()
                .build();

        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview, imageCapture);

//        imageCapture.setTargetRotation(Surface.ROTATION_180);
    }

    private void startCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                    bindPreview(cameraProvider);

                } catch (ExecutionException | InterruptedException e) {
                    Log.e(TAG, "Error starting camera: " + e.getMessage());
                }
            }
        }, ContextCompat.getMainExecutor(this));
    }


    public void onPreviewClick(View view) {
        if (isFullScreen) {
            // Shrink the preview back to its original size
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.width = originalWidth;
            layoutParams.height = originalHeight;
            view.setLayoutParams(layoutParams);
            LinearLayout addVehicleLayout = findViewById(R.id.add_vehicle_layout);
            addVehicleLayout.setVisibility(View.VISIBLE);
            findViewById(R.id.container_camera_preview_framelayout).setBackground(oldBackground);
            isFullScreen = false;
            CardView cardView = (CardView) findViewById(R.id.cardview_camera_preview);
            cardView.setRadius(oldBorderRadius);
            findViewById(R.id.overlay_label).setVisibility(View.VISIBLE);
            ((PreviewView) findViewById(R.id.previewView)).getOverlay().add(oldLayoutView);
            cameraShutter.setVisibility(View.GONE);

        } else {
            // Expand the preview to full screen
            findViewById(R.id.overlay_label).setVisibility(View.GONE);
            LinearLayout addVehicleLayout = findViewById(R.id.add_vehicle_layout);
            addVehicleLayout.setVisibility(View.GONE);
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            originalWidth = layoutParams.width;
            originalHeight = layoutParams.height;
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            view.setLayoutParams(layoutParams);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            isFullScreen = true;
            oldBackground = findViewById(R.id.container_camera_preview_framelayout).getBackground();
            findViewById(R.id.container_camera_preview_framelayout).setBackgroundColor(0x00000000);
            CardView cardView = (CardView) findViewById(R.id.cardview_camera_preview);
            oldBorderRadius = cardView.getRadius();
            cardView.setRadius(0f);
            oldLayoutViewOverlay = previewView.getOverlay();
            previewView.setBackgroundColor(0x00000000);
            previewView.getOverlay().clear();
            cameraShutter.setVisibility(View.VISIBLE);
        }
    }

    public static String generateRandomId() {
        // Generate a random UUID
        UUID uuid = UUID.randomUUID();

        // Convert UUID to string and remove hyphens
        String randomId = uuid.toString().replaceAll("-", "");

        return randomId;
    }

    File getAppSpecificAlbumStorageDir(Context context, String albumName) {
        // Get the pictures directory that's inside the app-specific directory on
        // external storage.
        File file = new File(context.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES), albumName);
        if (file == null || !file.mkdirs()) {
            Log.e("image", "Directory not created");
        }
        return file;
    }
    File photoFile;
    String fileName;
    private void captureImage() throws IOException {
        File outputDirectory = getOutputDirectory();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
                .format(System.currentTimeMillis());

        photoFile= getAppSpecificAlbumStorageDir(this, timeStamp + ".jpg");
        Log.d("Image-Path", photoFile.getAbsolutePath());
        ImageCapture.OutputFileOptions outputFileOptions =
                new ImageCapture.OutputFileOptions.Builder(photoFile).build();
        fileName= photoFile.getName();

        imageCapture.takePicture(outputFileOptions, cameraExecutor, new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(ImageCapture.OutputFileResults outputFileResults) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(AddVehicles.this, "Image captured!", Toast.LENGTH_SHORT).show();
                        previewView.performClick();
                        setOverlayInApp();


//                        Bitmap myBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
//
//                        overlay.setImageBitmap(myBitmap);
                        Log.i("image", "Image captured");
                    }
                });
            }

            @Override
            public void onError(ImageCaptureException error) {
                Log.e(TAG, "Error capturing image: " + error.getMessage());
            }
        });
    }

    private File getOutputDirectory() {
        File dcimDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + "/Car-Mitr");
        File outputDir = new File(dcimDir, "Camera");
        outputDir.mkdirs();
        return outputDir;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera();
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraExecutor.shutdown();
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