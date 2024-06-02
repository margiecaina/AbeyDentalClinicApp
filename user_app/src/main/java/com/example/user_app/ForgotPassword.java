package com.example.user_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    private AppCompatButton ConfirmButton;
    private EditText editTextTPwdResetEmail, editTextTPwdResetSMS;
    private FirebaseAuth authprofile;

    private LinearLayout smsBtn, emailBtn;

    ImageView exit_icon;
    TextView errorTextview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        editTextTPwdResetEmail = findViewById(R.id.editTextTPwdResetEmail);
        ConfirmButton = findViewById(R.id.ConfirmButton);
        smsBtn = findViewById(R.id.smsBtn);
        emailBtn = findViewById(R.id.emailBtn);
        editTextTPwdResetSMS = findViewById(R.id.editTextTPwdResetSMS);
        errorTextview = findViewById(R.id.errorTextview);
        exit_icon = findViewById(R.id.exitBtn);
        emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextTPwdResetSMS.setVisibility(View.GONE);
                editTextTPwdResetEmail.setVisibility(View.VISIBLE);
                ConfirmButton.setVisibility(View.VISIBLE);
                emailBtn.setElevation(10);
                smsBtn.setElevation(0);
                ConfirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String email = editTextTPwdResetEmail.getText().toString();
                        if(TextUtils.isEmpty(email)){
                            editTextTPwdResetEmail.setError("Please enter your registered email.");
                            editTextTPwdResetEmail.requestFocus();
                            errorTextview.setText("Please enter your registered email.");
                        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                            editTextTPwdResetEmail.setError("Please enter a valid email.");
                            editTextTPwdResetEmail.requestFocus();
                            errorTextview.setText("Please enter a valid email.");
                        } else{
                            resetPassword(email);
                        }
                    }
                });
            }
        });


        smsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextTPwdResetEmail.setVisibility(View.GONE);
                editTextTPwdResetSMS.setVisibility(View.VISIBLE);
                ConfirmButton.setVisibility(View.VISIBLE);
                emailBtn.setElevation(0);
                smsBtn.setElevation(10);
            }
        });

        exit_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void resetPassword(String email) {
        authprofile = FirebaseAuth.getInstance();
        authprofile.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    showAlertDialog();
                } else {
                    errorTextview.setText("Something went wrong.");
                }
            }
        });
    }

    private void showAlertDialog() {
        // Set up alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPassword.this);
        builder.setTitle("Reset Password");
        builder.setMessage("Please check your inbox for password reset link if email is registered. ");

        // Open email app
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // open email app
                startActivity(intent);
            }
        });

        // Create the alert Dialog
        AlertDialog alertDialog = builder.create();
        // Show dialog
        alertDialog.show();
    }
}