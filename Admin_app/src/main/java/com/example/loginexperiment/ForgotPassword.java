package com.example.loginexperiment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotPassword extends AppCompatActivity {

    private Button buttonPwdReset;
    private EditText editTextTPwdResetEmail;
    private FirebaseAuth authprofile;
    private TextView ResetViaPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        editTextTPwdResetEmail = findViewById(R.id.editTextTPwdResetEmail);
        buttonPwdReset = findViewById(R.id.ResetBtn);
        ResetViaPhone = findViewById(R.id.ResetViaPhone);

        buttonPwdReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextTPwdResetEmail.getText().toString();
                if(TextUtils.isEmpty(email)){
                    editTextTPwdResetEmail.setError("Please enter your registered email.");
                    editTextTPwdResetEmail.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    editTextTPwdResetEmail.setError("Please enter a valid email or password.");
                    editTextTPwdResetEmail.requestFocus();
                } else{
                    resetPassword(email);
                }
            }
        });
    }

    private void resetPassword(String email) {
        authprofile = FirebaseAuth.getInstance();
        authprofile.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgotPassword.this,"Please check your inbox for password reset link.", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(ForgotPassword.this, StartActivity.class);
                    // To prevent user from returning back to register activity on pressing back button after registration
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                            | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish(); //CLose UserProfileActivity
                } else {
                    Toast.makeText(ForgotPassword.this,"Something went wrong.", Toast.LENGTH_LONG).show();

                }
            }
        });
    }
}