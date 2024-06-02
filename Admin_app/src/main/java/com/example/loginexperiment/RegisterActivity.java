package com.example.loginexperiment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText RegisterEditTextEmail, RegisterEditTextadminAccessCode;
    private EditText RegisterEditTextPassword;
    private Button SignUpBtn;
    private FirebaseAuth auth;
    String textAddress = "", userAccess = "";
    String accesscodefirebase = "";
    AppCompatSpinner dropdownGender;

    private EditText RegisterEditTextFirstName, RegisterEditTextLastName, RegisterEditTextConfirmPassword, RegisterEditTextPhone, registerBirthdate;
    static final int DATE_DIALOG_ID = 1;
    final Calendar cal = Calendar.getInstance();
    private int year, month, day;

    private static final String TAG = "RegisterActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // Declare New Added Items for SignUp
        RegisterEditTextFirstName = findViewById(R.id.RegisterEditTextFirstName);
        RegisterEditTextLastName = findViewById(R.id.RegisterEditTextLastName);
        RegisterEditTextEmail = findViewById(R.id.RegisterEditTextEmail);
        RegisterEditTextPhone = findViewById(R.id.RegisterEditTextPhone);
        RegisterEditTextPassword = findViewById(R.id.RegisterEditTextPassword);
        RegisterEditTextConfirmPassword = findViewById(R.id.RegisterEditTextConfirmPassword);
        RegisterEditTextadminAccessCode = findViewById(R.id.adminAccessCode);
        registerBirthdate = findViewById(R.id.registerBirthdate);

        // birthdate code on click
        registerBirthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });

        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);

        // for spinner code
        dropdownGender = findViewById(R.id.dropdownGender);
        // Create an ArrayAdapter using the string array and a default spinner layout.
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.spinner_title,
                android.R.layout.simple_spinner_item
        );
        // Specify the layout to use when the list of choices appears.
        adapter.setDropDownViewResource(R.layout.spin_item);
        // Apply the adapter to the spinner.
        dropdownGender.setAdapter(adapter);

        dropdownGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK); /* if you want your item to be white */
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // SHow Hide Password Using Eye Icon
        ImageView show_hide_password = findViewById(R.id.show_hide_password), show_hide_password2 = findViewById(R.id.show_hide_password2);
        show_hide_password.setImageResource(R.drawable.ic_hide_pwd);
        show_hide_password2.setImageResource(R.drawable.ic_hide_pwd);
        show_hide_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(RegisterEditTextPassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    // if password is visible then hide it
                    RegisterEditTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    // Change Icon
                    show_hide_password.setImageResource(R.drawable.ic_hide_pwd);
                }else{
                    RegisterEditTextPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    show_hide_password.setImageResource(R.drawable.ic_show_pwd);
                }
            }
        });

        show_hide_password2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(RegisterEditTextConfirmPassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    // if password is visible then hide it
                    RegisterEditTextConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    // Change Icon
                    show_hide_password2.setImageResource(R.drawable.ic_hide_pwd);
                }else{
                    RegisterEditTextConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    show_hide_password2.setImageResource(R.drawable.ic_show_pwd);
                }
            }
        });

        SignUpBtn = findViewById(R.id.SignUpBtn);

        auth = FirebaseAuth.getInstance();
        DatabaseReference dataRef_accesscode = FirebaseDatabase.getInstance().getReference("Main");

        SignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtain the Entered Data
                String textFirstName, textLastName, textConfirmPassword, textPhone;
                String txt_email, txt_password, Gender, textadminAccessCode;
                String textAddress = "", DOB;

                textFirstName = RegisterEditTextFirstName.getText().toString();
                textLastName = RegisterEditTextLastName.getText().toString();
                textPhone = RegisterEditTextPhone.getText().toString();
                txt_email = RegisterEditTextEmail.getText().toString();
                txt_password = RegisterEditTextPassword.getText().toString();
                textConfirmPassword = RegisterEditTextConfirmPassword.getText().toString();
                DOB = registerBirthdate.getText().toString();
                Gender = dropdownGender.getSelectedItem().toString();
                textadminAccessCode = RegisterEditTextadminAccessCode.getText().toString();
                // get access code
                dataRef_accesscode.child("AccessCode").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Access Code Error...", task.getException());
                        }
                        else {
                            Log.d("firebase", String.valueOf(task.getResult().getValue()));
                            accesscodefirebase = String.valueOf(task.getResult().getValue());
                        }
                    }
                });
                // Validate Mobile Number using matcher and pattern
                String mobileRegex = "[9-9][0-9]{9}";
                Matcher mobileMatcher;
                Pattern mobilePattern = Pattern.compile(mobileRegex);
                mobileMatcher = mobilePattern.matcher(textPhone);

                if (TextUtils.isEmpty(textFirstName)){
                    Toast.makeText(RegisterActivity.this,"Please Enter First Name.", Toast.LENGTH_LONG).show();
                    RegisterEditTextFirstName.setError("First Name is required. ");
                    RegisterEditTextFirstName.requestFocus();
                }else if (TextUtils.isEmpty(textLastName)){
                    Toast.makeText(RegisterActivity.this,"Please Enter Last Name.", Toast.LENGTH_LONG).show();
                    RegisterEditTextLastName.setError("Last Name is required. ");
                    RegisterEditTextLastName.requestFocus();
                }else if(TextUtils.isEmpty(DOB)){
                    Toast.makeText(RegisterActivity.this,"Please Enter Date Of Birth.", Toast.LENGTH_LONG).show();
                    registerBirthdate.setError("Date of birth is required. ");
                    registerBirthdate.requestFocus();
                }else if(TextUtils.isEmpty(Gender)){
                    Toast.makeText(RegisterActivity.this,"Please enter gender.", Toast.LENGTH_LONG).show();
                    dropdownGender.requestFocus();
                }else if(TextUtils.isEmpty(txt_email)){
                    Toast.makeText(RegisterActivity.this,"Please Enter Email Address.", Toast.LENGTH_LONG).show();
                    RegisterEditTextEmail.setError("Email Address is required. ");
                    RegisterEditTextEmail.requestFocus();
                }else if (!Patterns.EMAIL_ADDRESS.matcher(txt_email).matches()){
                    Toast.makeText(RegisterActivity.this,"Please re-enter your email address.", Toast.LENGTH_LONG).show();
                    RegisterEditTextEmail.setError("Valid email is required. ");
                    RegisterEditTextEmail.requestFocus();
                }else if (TextUtils.isEmpty(textPhone)){
                    Toast.makeText(RegisterActivity.this,"Please Enter Mobile Number.", Toast.LENGTH_LONG).show();
                    RegisterEditTextPhone.setError("Phone Number is required. ");
                    RegisterEditTextPhone.requestFocus();
                }else if (textPhone.length() != 10){
                    Toast.makeText(RegisterActivity.this,"Please re-enter your mobile number.", Toast.LENGTH_LONG).show();
                    RegisterEditTextPhone.setError("Mobile Number should be 10 digits. ");
                    RegisterEditTextPhone.requestFocus();
                }else if (!mobileMatcher.find()){
                    Toast.makeText(RegisterActivity.this,"Please ensure that mobile number starts at 9.", Toast.LENGTH_LONG).show();
                    RegisterEditTextPhone.setError("Mobile Number is invalid. ");
                    RegisterEditTextPhone.requestFocus();
                } else if (txt_password.length() < 6) {
                    Toast.makeText(RegisterActivity.this,"Password be at least 8 characters.", Toast.LENGTH_SHORT).show();
                    RegisterEditTextPassword.setError("Password must be at least 6 characters.");
                    RegisterEditTextPassword.requestFocus();
                } else if (TextUtils.isEmpty(txt_password)){
                    Toast.makeText(RegisterActivity.this,"Please Enter Password!", Toast.LENGTH_LONG).show();
                    RegisterEditTextPassword.setError("Password is required.");
                    RegisterEditTextPassword.requestFocus();
                }else if (TextUtils.isEmpty(textConfirmPassword)){
                    Toast.makeText(RegisterActivity.this,"Please Enter Confirm Password.", Toast.LENGTH_LONG).show();
                    RegisterEditTextConfirmPassword.setError("Confirm Password is required. ");
                    RegisterEditTextConfirmPassword.requestFocus();
                } else if (!txt_password.equals(textConfirmPassword)){
                    Toast.makeText(RegisterActivity.this,"Password doesn't match.", Toast.LENGTH_LONG).show();
                    RegisterEditTextConfirmPassword.setError("Password doesn't match. ");
                    RegisterEditTextConfirmPassword.requestFocus();

                    // Clear the entered password
                    RegisterEditTextPassword.clearComposingText();
                    RegisterEditTextConfirmPassword.clearComposingText();
                } else if (textadminAccessCode.isEmpty()){
                    RegisterEditTextadminAccessCode.setError("Access code is empty. ");
                    RegisterEditTextadminAccessCode.requestFocus();
                } else if(!textadminAccessCode.equals(accesscodefirebase)){
                    RegisterEditTextadminAccessCode.setError("Invalid access or access code has expired.");
                    RegisterEditTextadminAccessCode.requestFocus();
                } else {
                    userAccess = "admin";
                    registerUser(txt_email, txt_password, textFirstName, textLastName, textPhone, userAccess, DOB, Gender, textAddress);
                }
            }
        });



    }
    private void registerUser(String email, String password,String textFirstName, String textLastName, String textPhone, String userAccess, String DOB, String Gender, String textAddress) {

        // Firebase Account Creation
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this,"Sign Up Successful!", Toast.LENGTH_SHORT).show();

                     // Login User
                     FirebaseUser firebaseUser = auth.getCurrentUser();

                     //Update display name user
                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(textFirstName+ " " + textLastName).build();
                    firebaseUser.updateProfile(profileChangeRequest);

                    //Enter User Data into the Firebase Realtime Database
                    ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(textFirstName, textLastName, textPhone, textAddress, userAccess, DOB, Gender, "");

                    // register as an admin
                    DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Users");
                    referenceProfile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Intent intent = new Intent(RegisterActivity.this, accountCreationConfirmation.class);
                                // To prevent user from returning back to register activity on pressing back button after registration
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                                // Send Email Verification
                                firebaseUser.sendEmailVerification();

                                Toast.makeText(RegisterActivity.this,"Sign Up Successful! Please verify your email. ", Toast.LENGTH_SHORT).show();

                                // Redirect Page to the Main Page
                                startActivity(intent);
                                finish(); // to close register activity
                            } else {
                                Toast.makeText(RegisterActivity.this,"Sign Up Unsuccessful. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        RegisterEditTextPassword.setError("Your password is too weak. Kindly use a mix of alphabets, numbers and special characters. ");
                        RegisterEditTextPassword.requestFocus();
                    } catch (FirebaseAuthInvalidCredentialsException e){
                        RegisterEditTextEmail.setError("Your email is invalid or already in use. ");
                        RegisterEditTextEmail.requestFocus();
                    }catch (FirebaseAuthUserCollisionException e){
                        RegisterEditTextEmail.setError("User is already registered with this email. Please use different email.");
                        RegisterEditTextEmail.requestFocus();
                    }catch (Exception e){
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_LONG);
                    }
                }
            }
        });
    }
    private void updateDate() {
        registerBirthdate.setText(new StringBuilder().append(month + 1).append('/').append(day).append('/')
                .append(year));
    }
    private DatePickerDialog.OnDateSetListener dateListener =
            new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int yr, int monthOfYear,
                                      int dayOfMonth) {

                    year = yr;
                    month = monthOfYear;
                    day = dayOfMonth;
                    updateDate();
                }
            };
    // Method to display dialog for date
    protected Dialog onCreateDialog(int id){
        switch(id) {

            case DATE_DIALOG_ID:
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE,0);
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, dateListener, year, month, day);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                return datePickerDialog;
        }
        return null;
    }
}