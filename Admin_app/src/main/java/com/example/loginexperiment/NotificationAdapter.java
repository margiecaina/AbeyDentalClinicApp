package com.example.loginexperiment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
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

public class NotificationAdapter extends FirebaseRecyclerAdapter<GetterSetter, NotificationAdapter.myViewHolder>{

    public NotificationAdapter(@NonNull FirebaseRecyclerOptions<GetterSetter> options) {
        super(options);
    }
    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull GetterSetter model) {
        FirebaseAuth authProfile;

        // Get data from firebase and input to the adapter
        holder.notificationtext.setText(model.getFirstname() + " " + model.getLastname() + " has requested a booking for a " + model.getServices().toLowerCase() + " proposed for " + model.getAppointmentdate() + " " + model.getAppointmenttime().toLowerCase() + ".");

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        String userID = firebaseUser.getUid();
    }
    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item,parent, false);
        return new myViewHolder(view);
    }


    public class myViewHolder extends RecyclerView.ViewHolder{
        TextView notificationtext;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            // Queue Data
            notificationtext = itemView.findViewById(R.id.notificationtext);
        }
    }
}
