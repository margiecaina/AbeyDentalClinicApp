package com.example.loginexperiment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
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
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class feedbackAdapter extends FirebaseRecyclerAdapter<GetterSetter, feedbackAdapter.myViewHolder> {
    public feedbackAdapter(FirebaseRecyclerOptions<GetterSetter> options) {
        super(options);
    }
    FirebaseAuth authProfile;

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull GetterSetter model) {
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("feedback");

        holder.date.setText(model.getAppointmentdate());
        holder.services.setText(model.services);
        holder.completename.setText(model.getCompletename());
        holder.realtime.setText(model.realtime);

        holder.emojifeedback1.setText(model.getFeedback1());
        holder.emojifeedback2.setText(model.getFeedback2());
        holder.editTextForQuestion3.setText(model.getFeedback3());
        holder.editTextForQuestion4.setText(model.getFeedback4());

        if(holder.emojifeedback1.getText().toString().equals("angry")){
            holder.emojifeedback1.setText("Angry");
            holder.emoji1.setImageResource(R.drawable.angryicon);
        } else if(holder.emojifeedback1.getText().toString().equals("sad")){
            holder.emojifeedback1.setText("Sad");
            holder.emoji1.setImageResource(R.drawable.sadicon);
        } else if(holder.emojifeedback1.getText().toString().equals("neutral")){
            holder.emojifeedback1.setText("Neutral");
            holder.emoji1.setImageResource(R.drawable.confusedicon);
        } else if(holder.emojifeedback1.getText().toString().equals("goodjob")){
            holder.emojifeedback1.setText("Good Job");
            holder.emoji1.setImageResource(R.drawable.goodicon);
        } else if(holder.emojifeedback1.getText().toString().equals("verygoodjob")){
            holder.emojifeedback1.setText("Very Good Job");
            holder.emoji1.setImageResource(R.drawable.happyicon);
        }

        if(holder.emojifeedback2.getText().toString().equals("angry")){
            holder.emojifeedback2.setText("Angry");
            holder.emoji2.setImageResource(R.drawable.angryicon);
        } else if(holder.emojifeedback2.getText().toString().equals("sad")){
            holder.emojifeedback2.setText("Sad");
            holder.emoji2.setImageResource(R.drawable.sadicon);
        } else if(holder.emojifeedback2.getText().toString().equals("neutral")){
            holder.emojifeedback2.setText("Neutral");
            holder.emoji2.setImageResource(R.drawable.confusedicon);
        } else if(holder.emojifeedback2.getText().toString().equals("goodjob")){
            holder.emojifeedback2.setText("Good Job");
            holder.emoji2.setImageResource(R.drawable.goodicon);
        } else if(holder.emojifeedback2.getText().toString().equals("verygoodjob")){
            holder.emojifeedback2.setText("Very Good Job");
            holder.emoji2.setImageResource(R.drawable.happyicon);
        }
        //Extract user reference from data base from registered user
        DatabaseReference referenceProfile2 = FirebaseDatabase.getInstance().getReference("Users").child(model.Uid);
        referenceProfile2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readWriteUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if (readWriteUserDetails != null) {
                    Picasso.with(holder.date.getContext()).load(readWriteUserDetails.getProfilepiclink()).into(holder.imageView_profile_dp);
                } else {
                    Toast.makeText(holder.emoji1.getContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(holder.emoji1.getContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });

        holder.expandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.moreDetails.setVisibility(View.VISIBLE);
                holder.expandButton.setVisibility(View.GONE);
            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.moreDetails.getVisibility() == View.VISIBLE){
                    holder.moreDetails.setVisibility(View.GONE);
                    holder.expandButton.setVisibility(View.VISIBLE);
                }
            }
        });


    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_item,parent, false);
        return new myViewHolder(view);
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        TextView date, services, completename, realtime;

        ImageView emoji1, emoji2;
        EditText editTextForQuestion3, editTextForQuestion4;

        TextView emojifeedback1, emojifeedback2;

        ImageView imageView_profile_dp, expandButton;
        LinearLayout moreDetails;

        CardView cardView;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.dateText);
            services = itemView.findViewById(R.id.serviceText);
            completename = itemView.findViewById(R.id.completeNameText);
            realtime = itemView.findViewById(R.id.tvRealtime);

            emoji1 = itemView.findViewById(R.id.emoji1);

            emoji2 = itemView.findViewById(R.id.emoji2);

            editTextForQuestion3 = itemView.findViewById(R.id.editTextForQuestion3);
            editTextForQuestion4 = itemView.findViewById(R.id.editTextForQuestion4);

            emojifeedback1 = itemView.findViewById(R.id.emoji1text);
            emojifeedback2 = itemView.findViewById(R.id.emoji2text);

            imageView_profile_dp = itemView.findViewById(R.id.imageView_profile_dp);

            expandButton = itemView.findViewById(R.id.expandButton);

            moreDetails = itemView.findViewById(R.id.moreDetails);

            cardView = itemView.findViewById(R.id.cardView);

        }
    }

}
