package com.example.parkar.adapter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parkar.EditVehicle;
import com.example.parkar.R;
import com.example.parkar.model.notification_model;
import com.example.parkar.ui.home.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class notification_adapter extends RecyclerView.Adapter<notification_adapter.ViewHolder> {
    private DatabaseReference mDatabase;

    private List<notification_model> notificationData;
    FirebaseAuth mAuth;

    public notification_adapter(List<notification_model> notificationData) {
        this.notificationData = notificationData;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_card, parent, false);
        return new notification_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull notification_adapter.ViewHolder holder, int position) {
        notification_model model = notificationData.get(position);
        holder.notification_date.setText(model.getDate());
        holder.notification_time.setText(model.getTime());
        holder.notification_message.setText(model.getMessage());
        holder.notification_vehicle_no.setText(model.getVehicle_no());

        holder.cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.notification_clear.getVisibility() == View.GONE) {
                    holder.notification_location.setVisibility(View.VISIBLE);
                    holder.notification_clear.setVisibility(View.VISIBLE);
                }else{
                    holder.notification_location.setVisibility(View.GONE);
                    holder.notification_clear.setVisibility(View.GONE);
                }
            }
        });

        holder.notification_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("https://www.google.com/maps/search/"
                                + model.getLat()                                                 //passing the coordinates as default parameters in maps Intent as source
                                + ","
                                + model.getLon()


                        ));
                v.getContext().startActivity(intent);
            }
        });

        holder.notification_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth = FirebaseAuth.getInstance();
                mDatabase = FirebaseDatabase.getInstance().getReference("user/" + mAuth.getCurrentUser().getUid() +"/notification/"+model.getNotification_id());
                mDatabase.removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(v.getContext(), "NOtification Removed Successfully", Toast.LENGTH_SHORT).show();
                        v.getContext().startActivity(new Intent(v.getContext(), HomeFragment.class));
                    }
                });
            }
        });

//        holder.edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
////                Toast.makeText(v.getContext(), "Vehicle ID"+model.getVehicle_number(), Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(v.getContext(), EditVehicle.class);
//                // intent.putExtra("vehicle_id", model.getVehicle_number());
//                v.getContext().startActivity(intent);
//            }
//        });
//        holder.download.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //String url = "http://192.168.1.29:80/parkar/verifyer.php?id="+model.getVehicle_number()+"&&uid="+model.getVehicle_owner_id();
////        Intent i = new Intent(Intent.ACTION_VIEW);
////        i.setData(Uri.parse(url));
////        v.getContext().startActivity(i);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return notificationData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView notification_date,notification_time,notification_message,notification_vehicle_no;
        Button notification_clear , notification_location;
        ConstraintLayout cl;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            notification_date
                    = itemView.findViewById(R.id.notification_date);
            notification_time = itemView.findViewById(R.id.notification_time);
            notification_message = itemView.findViewById(R.id.notification_message);
            notification_vehicle_no = itemView.findViewById(R.id.notification_vehicle_no);
            notification_clear = itemView.findViewById(R.id.notification_clear);
            notification_location = itemView.findViewById(R.id.notification_location);
            notification_location.setVisibility(View.GONE);
            notification_clear.setVisibility(View.GONE);
            cl = itemView.findViewById(R.id.notification_constraint);


        }
    }
}