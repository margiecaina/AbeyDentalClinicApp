package com.example.user_app;

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

import org.w3c.dom.Text;

public class NotificationAdapter extends FirebaseRecyclerAdapter<GetterSetter, NotificationAdapter.myViewHolder>{

    public NotificationAdapter(@NonNull FirebaseRecyclerOptions<GetterSetter> options) {
        super(options);
    }
    @Override
    protected void onBindViewHolder(@NonNull NotificationAdapter.myViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull GetterSetter model) {
        FirebaseAuth authProfile;

        // Get data from firebase and input to the adapter
        holder.date.setText(model.getAppointmentdate());
        holder.timetext.setText(model.getAppointmenttime());
        holder.services.setText(model.getServices());

        String statustext = model.getStatus();
        holder.statustextView.setText(statustext);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        String userID = firebaseUser.getUid();


        // change color if reschedule
        if(statustext.equals("Reschedule")){
            holder.statustextView.setTextColor(Color.RED);
        }else{
            holder.statustextView.setTextColor(Color.parseColor("#00cc00"));
        }

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.timetext.getContext());
                builder.setTitle("Are you Sure?");
                builder.setMessage("Deleted data can't be Undone. ");

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        FirebaseDatabase.getInstance().getReference("Users").child(userID).child("Appointment")
                                .child(getRef(position).getKey()).removeValue();
                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
                notifyDataSetChanged();

            }
        });
    }
    @NonNull
    @Override
    public NotificationAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item,parent, false);
        return new NotificationAdapter.myViewHolder(view);
    }


    public class myViewHolder extends RecyclerView.ViewHolder{
        TextView date, timetext, services, statustextView;
        Button deleteBtn;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            // Queue Data
            date = itemView.findViewById(R.id.datetext);
            timetext = itemView.findViewById(R.id.timetext);
            services = itemView.findViewById(R.id.servicestext);
            statustextView = itemView.findViewById(R.id.status);
            deleteBtn = (Button) itemView.findViewById(R.id.deleteBtn);
        }
    }
}
