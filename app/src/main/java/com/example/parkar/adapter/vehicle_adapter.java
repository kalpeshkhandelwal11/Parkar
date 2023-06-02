package com.example.parkar.adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parkar.EditVehicle;
import com.example.parkar.R;
import com.example.parkar.ViewVehicle;
import com.example.parkar.model.add_vehicle_model;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class vehicle_adapter extends  RecyclerView.Adapter<vehicle_adapter.ViewHolder> {
    private DatabaseReference mDatabase;

private List<add_vehicle_model> vehicleData;
public vehicle_adapter(List<add_vehicle_model> vehicleData) {
        this.vehicleData = vehicleData;
        }


@NonNull
@Override
public vehicle_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.vehicle_card,parent,false);
        return new vehicle_adapter.ViewHolder(view);
        }

    @Override
    public void onBindViewHolder(@NonNull vehicle_adapter.ViewHolder holder, int position) {
        add_vehicle_model model=vehicleData.get(position);
        holder.card_vehicle_number.setText(model.getVehicle_number());
        holder.card_vehicle_model.setText(model.getVehicle_model());
        holder.card_vehicle_society.setText(model.getVehicle_societycode());
        holder.card_vehicle_nick_name.setText(model.getVehicle_nickname());
        holder.card_vehicle_type.setText(model.getVehicle_type());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase = FirebaseDatabase.getInstance().getReference("vehicle/"+model.getVehicle_number());
                mDatabase.removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(v.getContext(), "Vehicle Deleted Successfully", Toast.LENGTH_SHORT).show();
                        v.getContext().startActivity(new Intent(v.getContext(), ViewVehicle.class));
                    }
                });
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Toast.makeText(v.getContext(), "Vehicle ID"+model.getVehicle_number(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(v.getContext(), EditVehicle.class);
                intent.putExtra("vehicle_id", model.getVehicle_number());
                v.getContext().startActivity(intent);
            }
        });
        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://172.16.91.26:80/parkar/verifyer.php?id="+model.getVehicle_number()+"&&uid="+model.getVehicle_owner_id();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                v.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return vehicleData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView card_vehicle_number, card_vehicle_model, card_vehicle_society, card_vehicle_nick_name, card_vehicle_type;
        Button edit , download , delete;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            card_vehicle_number
                    = itemView.findViewById(R.id.card_vehicle_no);
            card_vehicle_model = itemView.findViewById(R.id.card_vehicle_model);
            card_vehicle_society = itemView.findViewById(R.id.card_vehicle_society);
            card_vehicle_nick_name = itemView.findViewById(R.id.card_vehicle_nick_name);
            card_vehicle_type = itemView.findViewById(R.id.card_vehicle_type);

            edit = itemView.findViewById(R.id.vehicle_edit_btn);
            download = itemView.findViewById(R.id.vehicle_download_button);
            delete = itemView.findViewById(R.id.vehicle_delete_btn);



        }
    }
}
