package com.example.parkar.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parkar.R;
import com.example.parkar.ViewVehicle;
import com.example.parkar.adapter.notification_adapter;
import com.example.parkar.adapter.vehicle_adapter;
import com.example.parkar.databinding.FragmentHomeBinding;
import com.example.parkar.model.add_vehicle_model;
import com.example.parkar.model.notification_model;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {


    private FragmentHomeBinding binding;
    FloatingActionButton addvehicle;
    private List<notification_model> notificationData;
    private RecyclerView rv;
    private notification_adapter adapter;// Create object of the
    private FirebaseAuth mAuth;

    public HomeFragment(){}
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container,false);
        addvehicle = view.findViewById(R.id.add_vehicle_fab);

        rv=view.findViewById(R.id.notification_recycler);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));



        mAuth = FirebaseAuth.getInstance();
        notificationData=new ArrayList<>();

        final DatabaseReference nm= FirebaseDatabase.getInstance().getReference("user/" + mAuth.getCurrentUser().getUid() +"/notification");
        nm.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String curuid = mAuth.getCurrentUser().getUid();
                if (dataSnapshot.exists()){
                    for(DataSnapshot id:dataSnapshot.getChildren()){
                        String ID = id.child("notification_id").getValue().toString();
                        String date=id.child("date").getValue().toString();
                        String message =id.child("message").getValue().toString();
                        String time =id.child("time").getValue().toString();
                        String lat =id.child("lat").getValue().toString();
                        String lon =id.child("lon").getValue().toString();
                        String city =id.child("city").getValue().toString();
                        String region =id.child("region").getValue().toString();

                        String vehicle_no =id.child("vehicle_no").getValue().toString();
//                        Toast.makeText(getActivity().getApplicationContext(), "message"+message, Toast.LENGTH_SHORT).show();

                        notification_model work=new notification_model(message,time,date,city,lat,lon,region,ID,vehicle_no);


                            notificationData.add(work);
                        adapter = new notification_adapter(notificationData);
                        rv.setAdapter(adapter);



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
                Intent i = new Intent(getActivity().getApplicationContext(), ViewVehicle.class);
                startActivity(i);
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

}