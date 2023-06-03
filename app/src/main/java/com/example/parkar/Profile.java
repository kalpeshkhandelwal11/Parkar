package com.example.parkar;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkar.model.user_registration_model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Button logout;
    Button update_btn;
    FirebaseAuth auth;
    DatabaseReference mDatabase;
    TextView fullname_field,username_field;
    TextInputEditText profile_name,profile_dob,profile_gender,profile_phone,profile_whatsapp,profile_email,profile_address;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Profile.
     */
    // TODO: Rename and change types and number of parameters
    public static Profile newInstance(String param1, String param2) {
        Profile fragment = new Profile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        auth = FirebaseAuth.getInstance();
//        Toast.makeText(getActivity().getApplicationContext(), auth.getCurrentUser().getUid(), Toast.LENGTH_SHORT).show();
        mDatabase = FirebaseDatabase.getInstance().getReference("user/"+auth.getCurrentUser().getUid());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Inflate the layout for this fragment
        Button logout = view.findViewById(R.id.profile_logout);

        profile_name = view.findViewById(R.id.profile_name);
        profile_dob = view.findViewById(R.id.profile_dob);
        profile_gender = view.findViewById(R.id.profile_gender);
        profile_phone = view.findViewById(R.id.profile_phone);
        profile_whatsapp = view.findViewById(R.id.profile_whatsapp);
        profile_email = view.findViewById(R.id.profile_email);
        profile_address = view.findViewById(R.id.profile_address);
        update_btn = view.findViewById(R.id.profile_update);

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    profile_name.setText(snapshot.child("u_name").getValue().toString());
                    profile_dob.setText(snapshot.child("u_dob").getValue().toString());
                    profile_gender.setText(snapshot.child("u_gender").getValue().toString());
                    profile_phone.setText(snapshot.child("u_phone").getValue().toString());
                    profile_whatsapp.setText(snapshot.child("u_whatsapp").getValue().toString());

                    profile_email.setText(snapshot.child("u_email").getValue().toString());
                    profile_address.setText(snapshot.child("u_address").getValue().toString());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent intent = new Intent(getActivity().getApplicationContext(), Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_registration_model model = new user_registration_model(auth.getCurrentUser().getUid(),profile_phone.getText().toString(),profile_email.getText().toString(),profile_address.getText().toString(),"");
                Map<String, Object> postValues = model.toMap();
                mDatabase.updateChildren(postValues).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getActivity().getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getActivity().getApplicationContext(),Profile.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });
        return view;
    }
}