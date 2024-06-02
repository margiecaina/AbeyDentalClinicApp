package com.example.loginexperiment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class historyAdapter extends FirebaseRecyclerAdapter<GetterSetter, historyAdapter.myViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public historyAdapter(@NonNull FirebaseRecyclerOptions<GetterSetter> options) {
        super(options);
    }
    private FirebaseAuth authProfile;

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull GetterSetter model) {
        holder.firstname.setText(model.getFirstname());
        holder.lastname.setText(model.getLastname());
        holder.date.setText(model.getAppointmentdate());
        holder.services.setText(model.getServices());
        holder.realtime.setText(model.getRealtime());

        holder.historyProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.firstname.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_popup))
                        .setExpanded(true, 2050)
                        .create();

                // set values of textviews
                View view = dialogPlus.getHolderView();

                TextView fullnametv = view.findViewById(R.id.textView_show_fullname);
                TextView agetv = view.findViewById(R.id.textviewAge);
                TextView addresstv = view.findViewById(R.id.textView_show_address);
                TextView phonetv = view.findViewById(R.id.textView_show_phone);
                TextView birthdatetv = view.findViewById(R.id.textView_show_birthdate);
                TextView gendertv = view.findViewById(R.id.textView_show_gender);
                ImageView imageView_profile_dp = view.findViewById(R.id.imageView_profile_dp);

                fullnametv.setText(model.getFirstname() + " " + model.getLastname());

                if(Objects.equals(model.getBookingType(), "Registered User")){
                    String UID = model.getUid();
                //Extract user reference from data base from registered user
                    DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Users").child(UID);
                    referenceProfile.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ReadWriteUserDetails readWriteUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                            if (readWriteUserDetails != null) {
                                addresstv.setText(readWriteUserDetails.Address);
                                phonetv.setText(readWriteUserDetails.Phone);
                                birthdatetv.setText(readWriteUserDetails.DOB);
                                gendertv.setText(readWriteUserDetails.Gender);
                                Picasso.with(holder.date.getContext()).load(readWriteUserDetails.getProfilepiclink()).into(imageView_profile_dp);

                                String splitDOB[] = readWriteUserDetails.DOB.split("/");
                                int month = Integer.parseInt(splitDOB[0]);
                                int day = Integer.parseInt(splitDOB[1]);
                                int year = Integer.parseInt(splitDOB[2]);

                                LocalDate birthdate = LocalDate.of(year, month, day);
                                LocalDate currentDate = LocalDate.now();
                                // Calculate Age
                                int age = calculateAge(birthdate, currentDate);

                                agetv.setText(", " + age);
                            } else {
                                Toast.makeText(fullnametv.getContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(fullnametv.getContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
                        }
                    });
                }else{
                    addresstv.setText(model.getAddress());
                    phonetv.setText(model.getPhonenumber());
                    birthdatetv.setText("N/a");
                    gendertv.setText("N/a");
                    agetv.setText(" (walk-in)");
                }


                dialogPlus.show();

            }
        });

        holder.prescriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.firstname.getContext())
                        .setContentHolder(new ViewHolder(R.layout.historyprescriptiondialog))
                        .setExpanded(true, 1800)
                        .create();

                // set values of textviews
                View view2 = dialogPlus.getHolderView();

                TextView completeNameText = view2.findViewById(R.id.completeNameText);
                TextView dateText = view2.findViewById(R.id.dateText);
                TextView serviceText = view2.findViewById(R.id.serviceText);
                TextView tvRealtime = view2.findViewById(R.id.tvRealtime);
                TextView prescriptionEditText = view2.findViewById(R.id.prescriptionEditText);
                ImageView imageView_profile_dp2 = view2.findViewById(R.id.imageView_profile_dp);

                if(model.getBookingType() == "Registered User"){
                    //Extract user reference from data base from registered user
                    DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Users").child(model.Uid);
                    referenceProfile.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ReadWriteUserDetails readWriteUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                            if (readWriteUserDetails != null) {
                                Picasso.with(holder.date.getContext()).load(readWriteUserDetails.getProfilepiclink()).into(imageView_profile_dp2);

                            } else {
                                Toast.makeText(dateText.getContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(dateText.getContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                completeNameText.setText(model.getFirstname() + " " + model.getLastname());
                dateText.setText(model.getAppointmentdate());
                serviceText.setText(model.getServices());

                tvRealtime.setText(model.getRealtime());

                prescriptionEditText.setText(model.getPrescription());
                dialogPlus.show();


                AppCompatButton EditButton = view2.findViewById(R.id.EditButton);
                AppCompatButton SaveButton = view2.findViewById(R.id.SaveButton);

                EditButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditButton.setVisibility(View.GONE);
                        SaveButton.setVisibility(View.VISIBLE);
                        prescriptionEditText.setEnabled(true);
                    }
                });

                SaveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String, Object> historymap = new HashMap<>();
                        //Add to History
                        historymap.put("prescription", prescriptionEditText.getText().toString());

                        // Store Data to History Since Patient has already been served
                        FirebaseDatabase.getInstance().getReference().child("history")
                                .child(getRef(position).getKey()).updateChildren(historymap)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        notifyDataSetChanged();
                                        dialogPlus.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(holder.firstname.getContext(), "Error.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });


            }
        });
    }
    public static int calculateAge(LocalDate birthdate, LocalDate currentDate) {
        // Calculate period between birthdate and current date
        Period period = Period.between(birthdate, currentDate);

        return period.getYears();
    }
    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item,parent, false);
        return new historyAdapter.myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        TextView firstname, lastname, date, services, realtime;
        AppCompatButton historyProfileBtn;

        ImageView prescriptionButton;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            // Queue Data
            firstname = itemView.findViewById(R.id.historyfirstname);
            lastname = itemView.findViewById(R.id.historylastname);
            date = itemView.findViewById(R.id.historydate);
            realtime = itemView.findViewById(R.id.historyTime);
            services = itemView.findViewById(R.id.historyservices);

            historyProfileBtn = (AppCompatButton) itemView.findViewById(R.id.historyProfileBtn);
            prescriptionButton = (ImageView) itemView.findViewById(R.id.prescriptionButton);
        }
    }
}
