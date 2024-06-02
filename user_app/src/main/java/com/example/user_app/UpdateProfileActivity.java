package com.example.user_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateProfileActivity extends AppCompatActivity {

    private EditText editTextUpdateFirstName, editTextUpdateLastName, editTextUpdateDOB, editTextUpdateMobile, editTextUpdateAddress;
    private String textFirstname, textLastname, textDOB, textMobile, textAddress, textGender;
    static final int DATE_DIALOG_ID = 1;
    private FirebaseAuth authProfile;
    AppCompatSpinner dropdownGender;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        imageView = findViewById (R.id.imageView_profile_dp);

        editTextUpdateFirstName = findViewById(R.id.editText_update_profile_firstname);
        editTextUpdateLastName = findViewById(R.id.editText_update_profile_lastname);
        editTextUpdateDOB = findViewById(R.id.editText_update_profile_dob);
        editTextUpdateMobile = findViewById(R.id.editText_update_profile_mobile);
        editTextUpdateAddress = findViewById(R.id.editText_update_profile_address);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        // show Profile data
        showProfile(firebaseUser);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateProfileActivity.this, UploadProfilePicture.class);
                startActivity(intent);
                finish();
            }
        });

        // update email
        Button buttonUpdateEmail = findViewById(R.id.button_update_email);

        buttonUpdateEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateProfileActivity.this, UpdateEmailActivity.class);
                startActivity(intent);
                finish();
            }
        });
        // update dob

        editTextUpdateDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Extracting saved dd/mm/yyyy into different variable
                showDialog(DATE_DIALOG_ID);
                int day, month, year;
                if(textDOB == ""){
                    String textSADOB[] = textDOB.split("/");
                    day = Integer.parseInt(textSADOB[0]);
                    month = Integer.parseInt(textSADOB[1])-1;
                    year = Integer.parseInt(textSADOB[2]);
                }else{
                    final Calendar c = Calendar.getInstance();
                    day = c.get(Calendar.DAY_OF_MONTH);
                    month = c.get(Calendar.MONTH);
                    year = c.get(Calendar.YEAR);
                }
                DatePickerDialog picker;

                // date picker dialog
                picker = new DatePickerDialog(UpdateProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        editTextUpdateDOB.setText((month+1) + "/" + dayOfMonth + "/" + year);
                    }
                }, year, month, day);
                picker.show();
            }
        });

        // update profile

        Button buttonUpdateProfile = findViewById(R.id.button_update_profile);
        buttonUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile(firebaseUser);
            }
        });

        // spinner or drop down for gender
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
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                textGender = dropdownGender.getSelectedItem().toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    // update profile
    private void updateProfile(FirebaseUser firebaseUser) {

        // Validate Mobile Number using matcher and pattern
        String mobileRegex = "[9-9][0-9]{9}";
        Matcher mobileMatcher;
        Pattern mobilePattern = Pattern.compile(mobileRegex);
        mobileMatcher = mobilePattern.matcher(editTextUpdateMobile.getText());

        if (TextUtils.isEmpty(editTextUpdateFirstName.getText())){
            Toast.makeText(UpdateProfileActivity.this,"Please Enter First Name.", Toast.LENGTH_LONG).show();
            editTextUpdateFirstName.setError("First Name is required. ");
            editTextUpdateFirstName.requestFocus();
        }else if (TextUtils.isEmpty(editTextUpdateLastName.getText())){
            Toast.makeText(UpdateProfileActivity.this,"Please Enter Last Name.", Toast.LENGTH_LONG).show();
            editTextUpdateLastName.setError("Last Name is required. ");
            editTextUpdateLastName.requestFocus();
        }else if (TextUtils.isEmpty(editTextUpdateMobile.getText())){
            Toast.makeText(UpdateProfileActivity.this,"Please Enter Mobile Number.", Toast.LENGTH_LONG).show();
            editTextUpdateMobile.setError("Phone Number is required. ");
            editTextUpdateMobile.requestFocus();
        }else if (editTextUpdateMobile.getText().length() > 10){
            Toast.makeText(UpdateProfileActivity.this,"Please re-enter your mobile number.", Toast.LENGTH_LONG).show();
            editTextUpdateMobile.setError("Mobile Number should be 10 digits. ");
            editTextUpdateMobile.requestFocus();
        }else if (!mobileMatcher.find()){
            Toast.makeText(UpdateProfileActivity.this,"Please ensure that mobile number starts at 9.", Toast.LENGTH_LONG).show();
            editTextUpdateMobile.setError("Mobile Number is invalid. ");
            editTextUpdateMobile.requestFocus();
        }else if(TextUtils.isEmpty(dropdownGender.getSelectedItem().toString())){
            Toast.makeText(UpdateProfileActivity.this,"Please enter gender.", Toast.LENGTH_LONG).show();
            dropdownGender.requestFocus();
        }
        else{
            textFirstname = editTextUpdateFirstName.getText().toString();
            textLastname = editTextUpdateLastName.getText().toString();
            textMobile = editTextUpdateMobile.getText().toString();
            textDOB = editTextUpdateDOB.getText().toString();
            textAddress = editTextUpdateAddress.getText().toString();
            textGender = dropdownGender.getSelectedItem().toString();
            String userAccess = "user";

            // enter user data ino the firebase
            ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(textFirstname, textLastname, textMobile, textAddress, textDOB, userAccess, textGender, "");

            DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Users");

            String userID = firebaseUser.getUid();

            referenceProfile.child(userID).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        // setting new display name
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().
                                setDisplayName(textFirstname + " " + textLastname).build();
                        firebaseUser.updateProfile(profileUpdates);

                        // stop user from returning to update profile
                        finish();
                    }
                }
            });
        }
    }

    // fetch date from firebase
    private void showProfile(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();

        //Extract user reference from data base from registered user
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Users");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readWriteUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if (readWriteUserDetails != null) {
                    textFirstname = readWriteUserDetails.FirstName;
                    textLastname = readWriteUserDetails.LastName;
                    textAddress = readWriteUserDetails.Address;
                    textDOB = readWriteUserDetails.DOB;
                    textMobile = readWriteUserDetails.Phone;
                    textGender = readWriteUserDetails.Gender;

                    editTextUpdateFirstName.setText(textFirstname);
                    editTextUpdateLastName.setText(textLastname);
                    editTextUpdateDOB.setText(textDOB);
                    editTextUpdateAddress.setText(textAddress);
                    editTextUpdateMobile.setText(textMobile);

                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                            getApplication(),
                            R.array.spinner_title,
                            android.R.layout.simple_spinner_item
                    );

                    int spinnerPosition = adapter.getPosition(textGender);

                    dropdownGender.setSelection(spinnerPosition);


                    // set user DP AFTER USER HAS UPLOADED

                    Uri uri = firebaseUser.getPhotoUrl();

                    //image viewer set image uri
                    Picasso.with(getApplication()).load(uri).into(imageView);
                } else {
                    Toast.makeText(UpdateProfileActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateProfileActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }
}