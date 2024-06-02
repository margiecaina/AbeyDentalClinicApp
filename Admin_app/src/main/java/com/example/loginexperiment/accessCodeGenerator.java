package com.example.loginexperiment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class accessCodeGenerator extends AppCompatActivity {
// nices
    ImageButton closeButton;
    Button generateButton;
    TextView accesscode, seconds, hours, minutes;
    DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("Main");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_code_generator);
        closeButton = findViewById(R.id.closeButton);

        // close activity
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // timer
        seconds = findViewById(R.id.seconds);

        // generate access code
        accesscode = findViewById(R.id.accesscode);
        generateButton = findViewById(R.id.generateButton);

        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random=new Random();

                int min=100;
                int max=500;

                String firstset = String.valueOf(random.nextInt(max-min+1)+min);
                String secondset = String.valueOf(random.nextInt(max-min+1)+min);

                String randomcode = firstset + secondset;
                accesscode.setText(randomcode);

                dataRef.child("AccessCode").setValue(randomcode, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            Toast.makeText(accessCodeGenerator.this,"Something went wrong!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                int duration = 60;

                new CountDownTimer(duration * 1000, 1000){

                    @Override
                    public void onTick(long millisUntilFinished) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String time = String.format(Locale.getDefault(), "%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)-
                                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)-
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));

                                final String[] hourMinSec = time.split(":");
                                seconds.setText(hourMinSec[2] + "s");
                            }
                        });
                    }

                    @Override
                    public void onFinish() {
                        // once timer is finished set access code to null
                        accesscode.setText("");

                        dataRef.child("AccessCode").setValue("", new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                    Toast.makeText(accessCodeGenerator.this,"Something went wrong!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }.start();
            }
        });

    }
    @Override
    public void onStop() {
        accesscode.setText("");

        dataRef.child("AccessCode").setValue("", new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Toast.makeText(accessCodeGenerator.this,"Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        super.onStop();
    }
}