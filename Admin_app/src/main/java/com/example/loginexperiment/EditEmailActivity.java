package com.example.loginexperiment;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EditEmailActivity extends AppCompatActivity {
    private FirebaseAuth authProfile;
    private FirebaseUser firebaseUser;
    private TextView textViewAuthenticated, errortext, errortextforemail;
    private String userOldEmail, userNewEmail, userPwd;
    private Button buttonUpdateEmail;
    private EditText editTextNewEmail, editTextPwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_email);

        errortext = findViewById(R.id.errortext);
        errortextforemail = findViewById(R.id.errortextforemail);
        editTextPwd = findViewById(R.id.editText_update_email_verify_password);
        editTextNewEmail = findViewById(R.id.editText_update_email_new);
        textViewAuthenticated = findViewById(R.id.textView_update_email_authenticated);
        buttonUpdateEmail = findViewById(R.id.button_update_email);

        buttonUpdateEmail.setEnabled(false); //do not allow user to update email without verification
        editTextNewEmail.setEnabled(false);
        buttonUpdateEmail.setBackgroundTintList(ContextCompat.getColorStateList(EditEmailActivity.this, R.color.gray));

        authProfile = FirebaseAuth.getInstance();
        firebaseUser = authProfile.getCurrentUser();
        // Set old email id on text view
        editTextPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                errortext.setText("");
            }
        });

        editTextNewEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                errortextforemail.setText("");
            }
        });
        userOldEmail = firebaseUser.getEmail();
        TextView textViewViewOldEmail = findViewById(R.id.textview_update_email_old);
        textViewViewOldEmail.setText(userOldEmail);

        if(firebaseUser.equals("")){
            Toast.makeText(EditEmailActivity.this, "Something went wrong. User's details are not available.", Toast.LENGTH_LONG).show();
        }else{
            reAuthenticate(firebaseUser);
        }
    }
    // verify user before updating email
    private void reAuthenticate(FirebaseUser firebaseUser) {
        Button buttonVerifyUser = findViewById(R.id.button_authenticate_user);
        buttonVerifyUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtain password for authentication
                userPwd = editTextPwd.getText().toString();
                if(TextUtils.isEmpty(userPwd)){
                    errortext.setText("Password is needed to continue.");
                    editTextPwd.setError("Please enter your password for authentication.");
                    editTextPwd.requestFocus();
                } else {
                    AuthCredential credential = EmailAuthProvider.getCredential(userOldEmail, userPwd);

                    firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                errortext.setText("");
                                Toast.makeText(EditEmailActivity.this, "Password has been verified. You can update email now.", Toast.LENGTH_LONG).show();

                                // set textview to show user is authenticated
                                textViewAuthenticated.setText("You are authenticated. You can update your email now.");

                                // disable edit text for password, button to verify and enable editext for new email and update email

                                editTextNewEmail.setEnabled(true);
                                editTextPwd.setEnabled(false);
                                buttonVerifyUser.setEnabled(false);
                                buttonUpdateEmail.setEnabled(true);
                                buttonVerifyUser.setBackgroundTintList(ContextCompat.getColorStateList(EditEmailActivity.this, R.color.darkgray));
                                buttonUpdateEmail.setBackgroundTintList(ContextCompat.getColorStateList(EditEmailActivity.this, R.color.maincolor));

                                buttonUpdateEmail.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        userNewEmail = editTextNewEmail.getText().toString();
                                        if(TextUtils.isEmpty(userNewEmail)){
                                            errortextforemail.setText("New email is required.");
                                            editTextNewEmail.setError("Please enter new email.");
                                            editTextNewEmail.requestFocus();
                                        }else if(!Patterns.EMAIL_ADDRESS.matcher(userNewEmail).matches()){
                                            errortextforemail.setText("Valid email is required.");
                                            editTextNewEmail.setError("Please enter a valid email.");
                                            editTextNewEmail.requestFocus();
                                        }else if(userOldEmail.matches(userNewEmail)){
                                            errortextforemail.setText("New email cannot be same as old email address.");
                                            editTextNewEmail.setError("Please enter new email.");
                                            editTextNewEmail.requestFocus();
                                        } else{
                                            updateEmail(firebaseUser);
                                        }
                                    }
                                });
                            }else{
                                try{
                                    throw task.getException();
                                }catch (Exception e){
                                    errortext.setText(e.getMessage());
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    private void updateEmail(FirebaseUser firebaseUser) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.updateEmail(userNewEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isComplete()){
                    // Verify email
                    user.updateEmail(userNewEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d(TAG, "User email address updated.");
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditEmailActivity.this, "Something went wrong, try again.", Toast.LENGTH_LONG).show();
                        }
                    });
                }else{
                    try{
                        throw task.getException();
                    }catch (Exception e){
                        Toast.makeText(EditEmailActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}