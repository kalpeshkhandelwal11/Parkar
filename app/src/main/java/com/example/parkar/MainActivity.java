package com.example.parkar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class MainActivity extends AppCompatActivity {
    private static final Object TAG = "Some Error";
    private EditText sampleText;
    private ImageView QrCodeImage;
    Bitmap bitmap1;
    Button GenerateQr, ScannerButton,rb,Login,dashboard;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        currentUser= mAuth.getCurrentUser();
        EditText sampleText =  findViewById(R.id.textView2);

        ImageView QrCodeImage = findViewById(R.id.imageView);
        Button GenerateQr = findViewById(R.id.button);
        Button ScannerButton = findViewById(R.id.button2);
        Button rb = findViewById(R.id.reg_btn);
       Button  button3 = findViewById(R.id.button3);
       Button login = findViewById(R.id.button4);
       Button fpass = findViewById(R.id.button5);
       Button ViewParking = findViewById(R.id.button6);
       Button vehicle = findViewById(R.id.Vehicle);
       Button dashboard = findViewById(R.id.button7);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");
        mAuth = FirebaseAuth.getInstance();
        currentUser= mAuth.getCurrentUser();
        if(currentUser != null) {
            fpass.setVisibility(View.VISIBLE);
        }
        if(currentUser == null ){
            fpass.setVisibility(View.INVISIBLE);
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Login.class);
                startActivity(i);
            }
        });

        dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), User_dashboard.class);
                startActivity(i);
            }
        });

        fpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentUser == null)
                {
                    Toast.makeText(getApplicationContext(),"You Cannot logout until you login",Toast.LENGTH_SHORT).show();
                }else {

                    mAuth.signOut();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }

            }

        });
       button3.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(getApplicationContext(), add_parking.class);
               startActivity(intent);
           }
       });

        rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Registration.class);
                startActivity(intent);
            }
        });

        ScannerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Scanner.class);
                startActivity(intent);
            }
        });

        GenerateQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputText = sampleText.getText().toString();
                QRGEncoder qrgEncoder = new QRGEncoder(inputText, null, QRGContents.Type.TEXT, 250);
                qrgEncoder.setColorBlack(Color.BLACK);
                qrgEncoder.setColorWhite(Color.WHITE);
                // Getting QR-Code as Bitmap
                bitmap1 = qrgEncoder.getBitmap();
                // Setting Bitmap to ImageView
                QrCodeImage.setImageBitmap(bitmap1);
                myRef.setValue("Hello, World!");
            }
        });

        ViewParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AddCurrentParking.class);
                startActivity(i);
            }
        });
        vehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Vehicles.class);
                startActivity(i);
            }
        });
    }

}