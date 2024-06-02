package com.example.user_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class StartActivity extends AppCompatActivity {
    private AppCompatButton register;
    private AppCompatButton login;
    private EditText EditTextemail;
    private EditText EditTextpassword;
    private TextView forgotPasswordBtn;
    private FirebaseAuth auth;
    private static final String TAG = "StartActivity";

    private TextView bookwithoutSigningUp;
    private TextView errorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        register = findViewById(R.id.registerBtn);
        forgotPasswordBtn = findViewById(R.id.forgotPasswordBtn);

        EditTextemail = findViewById(R.id.email);
        EditTextpassword = findViewById(R.id.password);

        EditTextemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorText.setText("");
            }
        });
        EditTextpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorText.setText("");
            }
        });
        login = findViewById(R.id.login);
        auth = FirebaseAuth.getInstance();
        bookwithoutSigningUp = findViewById(R.id.bookwithoutSigningUp);
        // SHow Hide Password Using Eye Icon
        ImageView show_hide_password = findViewById(R.id.show_hide_password);
        show_hide_password.setImageResource(R.drawable.ic_hide_pwd);
        show_hide_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(EditTextpassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    // if password is visible then hide it
                    EditTextpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    // Change Icon
                    show_hide_password.setImageResource(R.drawable.ic_hide_pwd);
                }else{
                    EditTextpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    show_hide_password.setImageResource(R.drawable.ic_show_pwd);
                }
            }
        });

        bookwithoutSigningUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, manualSchedule.class));
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, TermsConditions.class));
            }
        });

        forgotPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, ForgotPassword.class));
            }
        });

        errorText = findViewById(R.id.errorText);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email = EditTextemail.getText().toString();
                String txt_password = EditTextpassword.getText().toString();
                if(txt_email.isEmpty() || txt_password.isEmpty()){
                    errorText.setText("*Please enter email and password.");

                } else if (!Patterns.EMAIL_ADDRESS.matcher(txt_email).matches()){
                    errorText.setText("*Please enter a valid email.");
                    EditTextemail.setError("Valid email is required. ");
                    EditTextemail.requestFocus();
                } else {
                    loginUser(txt_email, txt_password);
                }
            }
        });
    }

    // To avoid on keeping logging in

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            startActivity(new Intent(StartActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
        }
    }

    private void loginUser(String emailtxt, String passwordtxt) {
        auth.signInWithEmailAndPassword(emailtxt, passwordtxt).addOnCompleteListener(StartActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Get instance of the current user
                    FirebaseUser firebaseUser = auth.getCurrentUser();


                        // get uid after login
                        String userID = firebaseUser.getUid();
                        // check who is the user
                        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Users");
                        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ReadWriteUserDetails readWriteUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                                if(readWriteUserDetails != null){
                                    // check user access level if user
                                    if (Objects.equals(readWriteUserDetails.UserAccess, "user")) {
                                        // Check if email is verified
                                        if (firebaseUser.isEmailVerified()) {
                                            startActivity(new Intent(StartActivity.this, MainActivity.class));
                                            finish();

                                        } else {
                                            firebaseUser.sendEmailVerification();
                                            auth.signOut(); //SignOut User
                                            showAlertDialog();
                                        }
                                    }else if (Objects.equals(readWriteUserDetails.UserAccess, "admin") || Objects.equals(readWriteUserDetails.UserAccess, "superadmin")){
                                        // if user is admin error then sign out the account
                                        errorText.setText("*Access level mismatch.");
                                        EditTextemail.setError("Access level mismatch.");
                                        FirebaseAuth.getInstance().signOut();
                                    }
                                } else{
                                    Toast.makeText(getApplicationContext(),"Something went wrong. ", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getApplicationContext(),"Something went wrong. User's details are not available. ", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                    try{
                        throw task.getException();
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        errorText.setText("*Invalid credentials. Kindly check and re-enter. ");
                        EditTextemail.setError("Invalid credentials. Kindly check and re-enter. ");
                        EditTextemail.requestFocus();
                    }catch(FirebaseAuthInvalidUserException e){
                        EditTextemail.setError("User doesn't exist or not valid. ");
                        errorText.setText("*User doesn't exist or not valid. ");
                        EditTextemail.requestFocus();
                    }catch (Exception e){
                        Log.e(TAG, e.getMessage());
                        errorText.setText("*" + e.getMessage());
                    }
                }
            }
        });
    }

    private void showAlertDialog() {
        // Set up alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this);
        builder.setTitle("Email Not Verified");
        builder.setMessage("Please verify your email now. You can not login without email verification. ");

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
