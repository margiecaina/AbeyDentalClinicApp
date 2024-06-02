package com.example.user_app;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class HistoryAdapter extends FirebaseRecyclerAdapter<GetterSetter, HistoryAdapter.myViewHolder> {

    public HistoryAdapter(@NonNull FirebaseRecyclerOptions<GetterSetter> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull HistoryAdapter.myViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull GetterSetter model) {
        FirebaseAuth authProfile;

        // Get data from firebase and input to the adapter
        holder.date.setText(model.getAppointmentdate());
        holder.timetext.setText(model.getAppointmenttime());
        holder.services.setText(model.getServices());
        holder.name.setText(model.firstname + " " + model.lastname);
        holder.realtime.setText(model.realtime);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        String userID = firebaseUser.getUid();
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.timetext.getContext());
                builder.setTitle("Are you Sure?");
                builder.setMessage("Deleted data can't be Undone. ");

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        FirebaseDatabase.getInstance().getReference("Users").child(userID).child("history")
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
            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item,parent, false);
        return new HistoryAdapter.myViewHolder(view);
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView date, timetext, services, name, realtime;
        Button deleteBtn;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.datetext);
            timetext = itemView.findViewById(R.id.timetext);
            services = itemView.findViewById(R.id.servicestext);
            name = itemView.findViewById(R.id.completenametext);
            realtime = itemView.findViewById(R.id.tvRealtime);
            deleteBtn = (Button) itemView.findViewById(R.id.deleteBtn);
        }
    }
}
