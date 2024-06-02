package com.example.user_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class RequestConfirmation extends AppCompatActivity {

    String name, date, session, service;
    DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Users");
    private FirebaseAuth authProfile;

    TextView tvname, tvservice, tvdate, tvsession;
    ImageView backbutton;

    Button confirmBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_confirmation);

        // back
        backbutton = findViewById(R.id.exitBtn);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // get current user
        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        // if user is null
        if(firebaseUser == null){
            Toast.makeText(getApplicationContext(),"Something went wrong. User's details are not available. ", Toast.LENGTH_SHORT).show();
        }

        name = firebaseUser.getDisplayName();
        date = getIntent().getStringExtra("appointmentdate");
        service = getIntent().getStringExtra("services");
        session = getIntent().getStringExtra("session");

        tvname = findViewById(R.id.fullname);
        tvdate = findViewById(R.id.date);
        tvservice = findViewById(R.id.service);
        tvsession = findViewById(R.id.session);

        tvname.setText(name);
        tvdate.setText(date);
        tvservice.setText(service);
        tvsession.setText(session);

        // on click of confirmation
        confirmBtn = findViewById(R.id.confirmBtn);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
                finish();
            }
        });
    }
    String firstname, lastname, address, mobile;

    //// Add booking appointment
    private void insertData() {

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        String userID = firebaseUser.getUid();
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readWriteUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if (readWriteUserDetails != null) {
                    firstname = readWriteUserDetails.FirstName;
                    lastname = readWriteUserDetails.LastName;
                    address = readWriteUserDetails.Address;
                    mobile = readWriteUserDetails.Phone;
                    Uri uri = firebaseUser.getPhotoUrl();

                    Map<String, Object> map = new HashMap<>();
                    map.put("firstname", firstname);
                    map.put("lastname", lastname);
                    map.put("address", address);
                    map.put("phonenumber", mobile);
                    map.put("appointmentdate", date);
                    map.put("appointmenttime", session);
                    map.put("services", service);
                    map.put("uid", userID);
                    map.put("BookingType", "Registered User");

                    FirebaseDatabase.getInstance().getReference().child("Approval").push()
                            .setValue(map)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(RequestConfirmation.this, "Error", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(RequestConfirmation.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RequestConfirmation.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });

    }
}