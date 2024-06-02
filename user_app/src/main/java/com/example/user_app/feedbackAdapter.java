package com.example.user_app;

import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
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

import java.util.HashMap;
import java.util.Map;

public class feedbackAdapter extends FirebaseRecyclerAdapter<GetterSetter, feedbackAdapter.myViewHolder> {
    public feedbackAdapter(FirebaseRecyclerOptions<GetterSetter> options) {
        super(options);
    }
    DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Users");
    FirebaseAuth authProfile;

    @Override
    protected void onBindViewHolder(@NonNull feedbackAdapter.myViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull GetterSetter model) {

        holder.date.setText(model.getAppointmentdate());
        holder.services.setText(model.services);
        holder.completename.setText(model.firstname + " " + model.lastname);
        holder.realtime.setText(model.realtime);


        holder.ic_angry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.emojifeedback1 = "angry";
                holder.ic_angryimg1.getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                holder.ic_sadimg1.getDrawable().clearColorFilter();
                holder.ic_neutralimg1.getDrawable().clearColorFilter();
                holder.ic_goodimg1.getDrawable().clearColorFilter();
                holder.ic_verygoodjobimg1.getDrawable().clearColorFilter();
            }
        });
        holder.ic_sad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.emojifeedback1 = "sad";
                holder.ic_angryimg1.getDrawable().clearColorFilter();
                holder.ic_sadimg1.getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                holder.ic_neutralimg1.getDrawable().clearColorFilter();
                holder.ic_goodimg1.getDrawable().clearColorFilter();
                holder.ic_verygoodjobimg1.getDrawable().clearColorFilter();
            }
        });
        holder.ic_neutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.emojifeedback1 = "neutral";
                holder.ic_angryimg1.getDrawable().clearColorFilter();
                holder.ic_sadimg1.getDrawable().clearColorFilter();
                holder.ic_neutralimg1.getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                holder.ic_goodimg1.getDrawable().clearColorFilter();
                holder.ic_verygoodjobimg1.getDrawable().clearColorFilter();
            }
        });
        holder.ic_goodjob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.emojifeedback1 = "goodjob";
                holder.ic_angryimg1.getDrawable().clearColorFilter();
                holder.ic_sadimg1.getDrawable().clearColorFilter();
                holder.ic_neutralimg1.getDrawable().clearColorFilter();
                holder.ic_goodimg1.getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                holder.ic_verygoodjobimg1.getDrawable().clearColorFilter();
            }
        });
        holder.ic_verygoodjob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.emojifeedback1 = "verygoodjob";
                holder.ic_angryimg1.getDrawable().clearColorFilter();
                holder.ic_sadimg1.getDrawable().clearColorFilter();
                holder.ic_neutralimg1.getDrawable().clearColorFilter();
                holder.ic_goodimg1.getDrawable().clearColorFilter();
                holder.ic_verygoodjobimg1.getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
            }
        });

        // feedback 2

        holder.ic_angry2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.emojifeedback2 = "angry";

                holder.ic_angryimg2.getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                holder.ic_sadimg2.getDrawable().clearColorFilter();
                holder.ic_neutralimg2.getDrawable().clearColorFilter();
                holder.ic_goodimg2.getDrawable().clearColorFilter();
                holder.ic_verygoodjobimg2.getDrawable().clearColorFilter();
            }
        });
        holder.ic_sad2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.emojifeedback2 = "sad";
                holder.ic_angryimg2.getDrawable().clearColorFilter();
                holder.ic_sadimg2.getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                holder.ic_neutralimg2.getDrawable().clearColorFilter();
                holder.ic_goodimg2.getDrawable().clearColorFilter();
                holder.ic_verygoodjobimg2.getDrawable().clearColorFilter();
            }
        });
        holder.ic_neutral2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.emojifeedback2 = "neutral";
                holder.ic_angryimg2.getDrawable().clearColorFilter();
                holder.ic_sadimg2.getDrawable().clearColorFilter();
                holder.ic_neutralimg2.getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                holder.ic_goodimg2.getDrawable().clearColorFilter();
                holder.ic_verygoodjobimg2.getDrawable().clearColorFilter();
            }
        });
        holder.ic_goodjob2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.emojifeedback2 = "goodjob";

                holder.ic_angryimg2.getDrawable().clearColorFilter();
                holder.ic_sadimg2.getDrawable().clearColorFilter();
                holder.ic_neutralimg2.getDrawable().clearColorFilter();
                holder.ic_goodimg2.getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                holder.ic_verygoodjobimg2.getDrawable().clearColorFilter();
            }
        });
        holder.ic_verygoodjob2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.emojifeedback2 = "verygoodjob";

                holder.ic_angryimg2.getDrawable().clearColorFilter();
                holder.ic_sadimg2.getDrawable().clearColorFilter();
                holder.ic_neutralimg2.getDrawable().clearColorFilter();
                holder.ic_goodimg2.getDrawable().clearColorFilter();
                holder.ic_verygoodjobimg2.getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
            }
        });

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        String userID = firebaseUser.getUid();

        holder.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.emojifeedback1.isEmpty()){
                    holder.errorText.setText("*emoji not selected on first question.");
                } else if(holder.emojifeedback2.isEmpty()){
                    holder.errorText.setText("*emoji not selected on second question.");
                } else {
                    holder.errorText.setText("");

                    referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            GetterSetter getterSetter = snapshot.getValue(GetterSetter.class);
                            if (getterSetter != null) {
                                String completename = firebaseUser.getDisplayName();
                                String service = model.getServices();
                                String realtime = model.getRealtime();
                                String date = model.getAppointmentdate();
                                Uri uri = firebaseUser.getPhotoUrl();

                                Map<String, Object> map = new HashMap<>();
                                map.put("completename", completename);
                                map.put("realtime", realtime);
                                map.put("appointmentdate", date);
                                map.put("services", service);
                                map.put("uid", userID);
                                map.put("feedback1", holder.emojifeedback1);
                                map.put("feedback2", holder.emojifeedback2);
                                map.put("feedback3", holder.editTextForQuestion3.getText().toString());
                                map.put("feedback4", holder.editTextForQuestion4.getText().toString());
                                map.put("profilepic", uri.toString());

                                FirebaseDatabase.getInstance().getReference().child("feedback").push()
                                        .setValue(map)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(v.getContext(), "Submitted", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(v.getContext(), "Error", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                //delete feedback info

                                FirebaseDatabase.getInstance().getReference("Users").child(userID).child("feedback").child(getRef(position).getKey()).removeValue();
                            } else {
                                Toast.makeText(v.getContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(v.getContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

    }


    @NonNull
    @Override
    public feedbackAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_item,parent, false);
        return new feedbackAdapter.myViewHolder(view);
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        TextView date, services, completename, realtime;

        LinearLayout ic_angry, ic_sad, ic_neutral, ic_goodjob, ic_verygoodjob;
        LinearLayout ic_angry2, ic_sad2, ic_neutral2, ic_goodjob2, ic_verygoodjob2;
        EditText editTextForQuestion3, editTextForQuestion4;
        AppCompatButton submitButton;
        TextView errorText;

        ImageView ic_angryimg1, ic_sadimg1, ic_neutralimg1, ic_goodimg1, ic_verygoodjobimg1;
        ImageView ic_angryimg2, ic_sadimg2, ic_neutralimg2, ic_goodimg2, ic_verygoodjobimg2;

        String emojifeedback1 = "", emojifeedback2 = "";

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            ic_angryimg1 = itemView.findViewById(R.id.ic_angryimg1);
            ic_sadimg1 = itemView.findViewById(R.id.ic_sadimg1);
            ic_neutralimg1 = itemView.findViewById(R.id.ic_neutralimg1);
            ic_goodimg1 = itemView.findViewById(R.id.ic_goodimg1);
            ic_verygoodjobimg1 = itemView.findViewById(R.id.ic_verygoodjobimg1);


            ic_angryimg2 = itemView.findViewById(R.id.ic_angryimg2);
            ic_sadimg2 = itemView.findViewById(R.id.ic_sadimg2);
            ic_neutralimg2 = itemView.findViewById(R.id.ic_neutralimg2);
            ic_goodimg2 = itemView.findViewById(R.id.ic_goodimg2);
            ic_verygoodjobimg2 = itemView.findViewById(R.id.ic_verygoodjobimg2);



            date = itemView.findViewById(R.id.dateText);
            services = itemView.findViewById(R.id.serviceText);
            completename = itemView.findViewById(R.id.completeNameText);
            realtime = itemView.findViewById(R.id.tvRealtime);

            ic_angry = itemView.findViewById(R.id.ic_angry);
            ic_sad = itemView.findViewById(R.id.ic_sad);
            ic_neutral = itemView.findViewById(R.id.ic_neutral);
            ic_goodjob = itemView.findViewById(R.id.ic_goodjob);
            ic_verygoodjob = itemView.findViewById(R.id.ic_verygoodjob);

            ic_angry2 = itemView.findViewById(R.id.ic_angry2);
            ic_sad2 = itemView.findViewById(R.id.ic_sad2);
            ic_neutral2 = itemView.findViewById(R.id.ic_neutral2);
            ic_goodjob2 = itemView.findViewById(R.id.ic_goodjob2);
            ic_verygoodjob2 = itemView.findViewById(R.id.ic_verygoodjob2);

            editTextForQuestion3 = itemView.findViewById(R.id.editTextForQuestion3);
            editTextForQuestion4 = itemView.findViewById(R.id.editTextForQuestion4);

            submitButton = itemView.findViewById(R.id.submitButton);
            errorText = itemView.findViewById(R.id.errorTv);

        }
    }

}
