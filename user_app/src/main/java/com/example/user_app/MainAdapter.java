package com.example.user_app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class MainAdapter extends FirebaseRecyclerAdapter <GetterSetter, MainAdapter.myViewHolder> {

    public MainAdapter(@NonNull FirebaseRecyclerOptions<GetterSetter> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, final int position, @NonNull GetterSetter model) {

        // Get data from gettersetter.java and assign values to the views or textviews.
        holder.date.setText(model.getAppointmentdate());
        holder.timetext.setText(model.getAppointmenttime());
        holder.services.setText(model.getServices());

        FirebaseAuth authProfile;
        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        String userID = firebaseUser.getUid();

        // button to delete the value
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.timetext.getContext());
                builder.setTitle("Are you Sure?");
                builder.setMessage("Deleted data can't be Undone. ");

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        FirebaseDatabase.getInstance().getReference("Users").child(userID).child("RequestBookings")
                                .child(getRef(position).getKey()).removeValue();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(holder.timetext.getContext(), "Cancelled.", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });
    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item,parent, false);
        return new myViewHolder(view);
    }
    class myViewHolder extends RecyclerView.ViewHolder{
        // grabbing the views from the recyclerview layout file
        // kinda like the oncreatemethod
        TextView date, timetext, services;
        Button deleteBtn;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            // Queue Data
            date = itemView.findViewById(R.id.datetext);
            timetext = itemView.findViewById(R.id.timetext);
            services = itemView.findViewById(R.id.servicestext);
            deleteBtn = (Button) itemView.findViewById(R.id.deleteBtn);

        }
    }
}
