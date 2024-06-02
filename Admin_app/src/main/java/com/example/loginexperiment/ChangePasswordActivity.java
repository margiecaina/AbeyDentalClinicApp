package com.example.loginexperiment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {

    private FirebaseAuth authProfile;
    private EditText editTextPwdCurr, editTextPwdNew, editTextPwdConfirmNew;
    private TextView textViewAuthenticated;
    private Button buttonChangePwd, buttonReAuthenticate;
    private String userPwdCurr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        editTextPwdCurr = findViewById(R.id.editText_current_password);
        editTextPwdNew = findViewById(R.id.editText_enter_new_password);
        editTextPwdConfirmNew = findViewById(R.id.editText_confirm_new_password);
        textViewAuthenticated = findViewById(R.id.textView_authenticated);
        buttonReAuthenticate = findViewById(R.id.button_authenticate_user);
        buttonChangePwd = findViewById(R.id.button_change_password);

        // disable edit text for new password, confirm password and password confirm button

        editTextPwdNew.setEnabled(false);
        editTextPwdConfirmNew.setEnabled(false);
        buttonChangePwd.setEnabled(false);

        buttonReAuthenticate.setBackgroundTintList(ContextCompat.getColorStateList(ChangePasswordActivity.this, R.color.maincolor));
        buttonChangePwd.setBackgroundTintList(ContextCompat.getColorStateList(ChangePasswordActivity.this, R.color.darkgray));
        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        if(firebaseUser.equals("")){
            Toast.makeText(ChangePasswordActivity.this, "Something went wrong. User's details are not available.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(ChangePasswordActivity.this, UpdateProfileActivity.class);
            startActivity(intent);
            finish();
        }else{
            reAuthenticate(firebaseUser);
        }
    }
    // reauthenticate user before changing passwod
    private void reAuthenticate(FirebaseUser firebaseUser) {
        buttonReAuthenticate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userPwdCurr = editTextPwdCurr.getText().toString();
                if(TextUtils.isEmpty(userPwdCurr)){
                    Toast.makeText(ChangePasswordActivity.this, "Password is needed.", Toast.LENGTH_LONG).show();
                    editTextPwdCurr.setError("Please enter your current password to authenticate");
                    editTextPwdCurr.requestFocus();
                }else{
                    // reauthenticate user now

                    AuthCredential credential = EmailAuthProvider.getCredential(firebaseUser.getEmail(), userPwdCurr);
                    firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                // disable edit for current password and authenticate the button
                                editTextPwdCurr.setEnabled(false);
                                buttonReAuthenticate.setEnabled(false);

                                // enable change password and password confirm button
                                editTextPwdNew.setEnabled(true);
                                editTextPwdConfirmNew.setEnabled(true);
                                buttonChangePwd.setEnabled(true);

                                // change color of the change button password and authenticate button
                                buttonChangePwd.setBackgroundTintList(ContextCompat.getColorStateList(ChangePasswordActivity.this, R.color.maincolor));
                                buttonReAuthenticate.setBackgroundTintList(ContextCompat.getColorStateList(ChangePasswordActivity.this, R.color.darkgray));
                                textViewAuthenticated.setText("You are authenticated/verified. ");
                                Toast.makeText(ChangePasswordActivity.this, "Password has been verified. Change password now.", Toast.LENGTH_LONG).show();
                            
                                buttonChangePwd.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        changePwd(firebaseUser);
                                    }
                                });
                            }else {
                                try {
                                    throw task.getException();
                                }catch (Exception e){
                                    Toast.makeText(ChangePasswordActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    private void changePwd(FirebaseUser firebaseUser) {
        String userPwdNew = editTextPwdNew.getText().toString();
        String userPwdConfirmNew = editTextPwdConfirmNew.getText().toString();

        if(TextUtils.isEmpty(userPwdNew)){
            Toast.makeText(ChangePasswordActivity.this, "New password is needed.", Toast.LENGTH_LONG).show();
            editTextPwdNew.setError("Please enter your new password. ");
            editTextPwdNew.requestFocus();
        }else if(TextUtils.isEmpty(userPwdConfirmNew)){
            Toast.makeText(ChangePasswordActivity.this, "Please confirm your new password", Toast.LENGTH_LONG).show();
            editTextPwdConfirmNew.setError("Please enter confirm password. ");
            editTextPwdConfirmNew.requestFocus();
        }else if(!userPwdNew.matches(userPwdConfirmNew)){
            Toast.makeText(ChangePasswordActivity.this, "Password did not match.", Toast.LENGTH_LONG).show();
            editTextPwdConfirmNew.setError("Please re-enter same password. ");
            editTextPwdConfirmNew.requestFocus();
        }else if(userPwdCurr.matches(userPwdNew)){
            Toast.makeText(ChangePasswordActivity.this, "New password cannot be same as old password", Toast.LENGTH_LONG).show();
            editTextPwdConfirmNew.setError("Please enter a new password. ");
            editTextPwdConfirmNew.requestFocus();
        }else{
            firebaseUser.updatePassword(userPwdNew).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(ChangePasswordActivity.this, "Password has been changed. ", Toast.LENGTH_LONG).show();
                        finish();
                    }else{
                        try {
                            throw task.getException();
                        }catch (Exception e){
                            Toast.makeText(ChangePasswordActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        }
    }
}