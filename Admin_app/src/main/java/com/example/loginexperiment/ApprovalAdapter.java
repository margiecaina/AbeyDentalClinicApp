package com.example.loginexperiment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ApprovalAdapter extends FirebaseRecyclerAdapter<GetterSetter, ApprovalAdapter.myViewHolder> {
    private DatabaseReference mDatabase;

    public ApprovalAdapter(@NonNull FirebaseRecyclerOptions<GetterSetter> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") final int position, @NonNull GetterSetter model) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Approval");

        holder.firstname.setText(model.getFirstname());
        holder.lastname.setText(model.getLastname());
        holder.date.setText(model.getAppointmentdate());
        holder.timetext.setText(model.getAppointmenttime());
        holder.services.setText(model.getServices());
        holder.BookingType.setText(model.getBookingType());

        //for registered users only code to approve appointment
        holder.approvedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.timetext.getContext());
                builder.setTitle("Are you Sure?");
                builder.setMessage("Approval of this appointment will be finalized and patient will be notified. ");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Map<String, Object> map = new HashMap<>();
                        //Add to History
                        map.put("firstname", model.getFirstname());
                        map.put("lastname", model.getLastname());
                        map.put("address", model.getAddress());
                        map.put("appointmentdate", model.getAppointmentdate());
                        map.put("appointmenttime", model.getAppointmenttime());
                        map.put("services", model.getServices());
                        map.put("phonenumber", model.getPhonenumber());
                        map.put("BookingType", model.getBookingType());
                        map.put("status", "Approved");
                        if(Objects.equals(model.getBookingType(), "Registered User")){
                            map.put("uid", model.getUid());
                            ///////////////////////////Inform User Appointment is Approved//////////////////////////
                            FirebaseRecyclerOptions<GetterSetter> options =
                                    new FirebaseRecyclerOptions.Builder<GetterSetter>()
                                            .setQuery(FirebaseDatabase.getInstance().getReference("Users").child(model.Uid).child("Appointment"), GetterSetter.class)
                                            .build();
                            FirebaseDatabase.getInstance().getReference("Users").child(model.Uid).child("Appointment").push()
                                    .setValue(map)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(holder.timetext.getContext(), "Error.", Toast.LENGTH_SHORT).show();
                                        }});
                        }
                        // Queue Patient once approved
                        FirebaseDatabase.getInstance().getReference().child("Appointment").push()
                                .setValue(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        FirebaseDatabase.getInstance().getReference().child("Approval")
                                                .child(getRef(position).getKey()).removeValue();
                                        notifyDataSetChanged();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(holder.timetext.getContext(), "Error.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        notifyDataSetChanged();

                    }
                });
                builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
            }
        });

        // declined appointment

        holder.declineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.timetext.getContext());
                builder.setTitle("Did you notify the patient via SMS or Email?");
                builder.setMessage("Declined appointment will be finalized. ");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Map<String, Object> map = new HashMap<>();
                        //Add to History
                        map.put("firstname", model.getFirstname());
                        map.put("lastname", model.getLastname());
                        map.put("address", model.getAddress());
                        map.put("appointmentdate", model.getAppointmentdate());
                        map.put("appointmenttime", model.getAppointmenttime());
                        map.put("services", model.getServices());
                        map.put("phonenumber", model.getPhonenumber());
                        map.put("bookingtype", model.getBookingType());
                        map.put("status", "Reschedule");

                        FirebaseDatabase.getInstance().getReference().child("Approval")
                                .child(getRef(position).getKey()).removeValue();
                        Toast.makeText(holder.timetext.getContext(), "Patient's appointment has been declined.", Toast.LENGTH_SHORT).show();

                        ///////////////////////////Inform User Appointment is Declined//////////////////////////
                        FirebaseRecyclerOptions<GetterSetter> options =
                                new FirebaseRecyclerOptions.Builder<GetterSetter>()
                                        .setQuery(FirebaseDatabase.getInstance().getReference("Users").child(model.Uid).child("Appointment"), GetterSetter.class)
                                        .build();

                        FirebaseDatabase.getInstance().getReference("Users").child(model.Uid).child("Appointment").push()
                                .setValue(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(holder.timetext.getContext(), "Patient's appointment has been declined.", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(holder.timetext.getContext(), "Error.", Toast.LENGTH_SHORT).show();
                                    }});
                        notifyDataSetChanged();

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(holder.timetext.getContext(), "Please notify patient first.", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
                notifyDataSetChanged();
            }
        });
        //END CODE//
        //for registered users only code to approve appointment

    }


    @NonNull
    @Override
    public ApprovalAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.approval_item,parent, false);
        return new myViewHolder(view);
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView firstname, lastname, date, timetext, services, BookingType;
        Button approvedBtn, declineBtn;
        RelativeLayout rl;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            firstname = itemView.findViewById(R.id.firstNametext);
            lastname = itemView.findViewById(R.id.lastNametext);
            date = itemView.findViewById(R.id.datetext);
            timetext = itemView.findViewById(R.id.timetext);
            services = itemView.findViewById(R.id.servicestext);
            BookingType = itemView.findViewById(R.id.bookingType);

            approvedBtn = (Button) itemView.findViewById(R.id.approvedBtn);
            declineBtn = (Button) itemView.findViewById(R.id.declineBtn);

            rl = (RelativeLayout) itemView.findViewById(R.id.rl);

        }
    }


}
