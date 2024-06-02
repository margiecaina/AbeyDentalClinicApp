package com.example.loginexperiment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class MainAdapter extends FirebaseRecyclerAdapter <GetterSetter, MainAdapter.myViewHolder> {

    private DatabaseReference mDatabase;
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MainAdapter(@NonNull FirebaseRecyclerOptions<GetterSetter> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") final int position, @NonNull GetterSetter model) {

        // reference the realtime database source

        holder.firstname.setText(model.getFirstname());
        holder.lastname.setText(model.getLastname());
        holder.date.setText(model.getAppointmentdate());
        if(Objects.equals(model.getBookingType(), "Registered User")){
            holder.timetext.setText(model.getAppointmenttime());
        }else{
            holder.timetext.setText(model.getRealtime());
        }
        holder.services.setText(model.getServices());

        // button when patient has prescription
        holder.prescriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogAfterServed dialogAfterServed = new DialogAfterServed(holder.timetext.getContext());
                dialogAfterServed.getWindow();
                dialogAfterServed.setCancelable(true);
                dialogAfterServed.show();
                Button positive = dialogAfterServed.findViewById(R.id.saveButton);
                EditText prescriptionEditText = dialogAfterServed.findViewById(R.id.editTextPrescription);
                prescriptionEditText.setText(holder.prescription);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.prescription = prescriptionEditText.getText().toString();

                        dialogAfterServed.dismiss();
                        notifyDataSetChanged();
                    }
                });
            }

        });

        holder.completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("KK:mm aaa");
                String dateTime = simpleDateFormat.format(calendar.getTime()).toString();

                Map<String, Object> historymap = new HashMap<>();
                //Add to History
                historymap.put("firstname", model.firstname);
                historymap.put("lastname", model.lastname);
                historymap.put("appointmentdate", model.appointmentdate);
                historymap.put("services", model.services);
                historymap.put("prescription", holder.prescription);
                historymap.put("realtime", dateTime);
                historymap.put("BookingType", model.getBookingType());

                if(Objects.equals(model.getBookingType(), "Registered User")){
                    historymap.put("appointmenttime", model.appointmenttime);
                    historymap.put("uid", model.getUid());
                    ///////////////////////////Inform User Appointment is Approved//////////////////////////
                    FirebaseDatabase.getInstance().getReference("Users").child(model.getUid()).child("history").push()
                            .setValue(historymap)
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
                    FirebaseDatabase.getInstance().getReference("Users").child(model.getUid()).child("feedback").push()
                            .setValue(historymap)
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

                }else{
                    historymap.put("address", model.address);
                    historymap.put("phonenumber", model.phonenumber);
                }
                // Store Data to History Since Patient has already been served
                FirebaseDatabase.getInstance().getReference().child("history").push()
                        .setValue(historymap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(holder.timetext.getContext(), "Error.", Toast.LENGTH_SHORT).show();
                            }
                        });
                // delete from queue
                FirebaseDatabase.getInstance().getReference().child("Appointment")
                        .child(getRef(position).getKey()).removeValue();
            notifyDataSetChanged();
            }
        });
        int queuenumberInt = position + 1;
        if (queuenumberInt < 10){
            holder.queue_number.setText("0" + queuenumberInt);
        }else{
            holder.queue_number.setText(String.valueOf(queuenumberInt));
        }
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item,parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        TextView firstname, lastname, date, timetext, services;
        AppCompatButton completeButton;
        ImageView prescriptionButton;
        String prescription = "";

        TextView queue_number;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            // Queue Data
            firstname = itemView.findViewById(R.id.firstNametext);
            lastname = itemView.findViewById(R.id.lastNametext);
            date = itemView.findViewById(R.id.datetext);
            timetext = itemView.findViewById(R.id.timetext);
            services = itemView.findViewById(R.id.servicestext);

            completeButton = itemView.findViewById(R.id.completeButton);
            prescriptionButton = itemView.findViewById(R.id.prescriptionButton);

            queue_number = (itemView).findViewById(R.id.queue_number);

        }
    }
}
