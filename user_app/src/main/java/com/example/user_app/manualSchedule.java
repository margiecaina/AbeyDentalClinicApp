package com.example.user_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class manualSchedule extends AppCompatActivity {
    EditText ScheduleDate;
    EditText ScheduleTime;
    CheckBox acceptTerms;

    EditText FirstName, LastName, Address, Phone, Email;

    Button submitSchedule, backBtn;
    static final int DATE_DIALOG_ID = 1;
    static final int TIME_DIALOG_ID = 2;

    private int year, month, day;
    private int hours, min;


    // Services Listdown

    String[] item = {"Consultation", "Oral Examinations", "Cleaning", "Fluoride treatments", "Teeth Whitening", "Oral Hygiene Instructions", "Oral Screening", "Oral Medicine", "Dental Crown", "Cavity Treatment", "Periodontics", "Orthodontics", "Periodontics", "Dental Bonding" , "Braces", "Brace Adjustment", "Fillings", "Tooth Extraction", "Root Canal", "Dentures", "Bridges", "Dental Implant"};

    AutoCompleteTextView auto_completeTxt;

    ArrayAdapter<String> adapterItems;
    String selected_service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_schedule);

        ScheduleDate = (EditText) findViewById(R.id.ScheduleDate);
        ScheduleTime = (EditText) findViewById(R.id.ScheduleTime);

        //On click of Schedule button set date
        ScheduleDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });

        final Calendar cal = Calendar.getInstance();

        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);

        //On click of Schedule button set time

        ScheduleTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(TIME_DIALOG_ID);
            }
        });

        hours = cal.get(Calendar.HOUR);
        min = cal.get(Calendar.MINUTE);

        // On click of Submit Schedule Button

        Button submitSchedule;

        FirstName = (EditText) findViewById(R.id.FirstName);
        LastName = (EditText) findViewById(R.id.LastName);
        Address = (EditText) findViewById(R.id.Address);
        Phone = (EditText) findViewById(R.id.Phone);
        Email = (EditText) findViewById(R.id.Email);
        submitSchedule = (Button) findViewById(R.id.submitSchedule);
        backBtn = (Button) findViewById(R.id.backBtn);
        acceptTerms = findViewById(R.id.acceptTerms);

        submitSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Validate Mobile Number using matcher and pattern
                String mobileRegex = "[9-9][0-9]{9}";
                Matcher mobileMatcher;
                Pattern mobilePattern = Pattern.compile(mobileRegex);
                mobileMatcher = mobilePattern.matcher(Phone.getText().toString());

                if(TextUtils.isEmpty(FirstName.getText())){
                    Toast.makeText(manualSchedule.this,"Please Enter First Name.", Toast.LENGTH_LONG).show();
                    FirstName.setError("First Name is required. ");
                    FirstName.requestFocus();
                }else if (TextUtils.isEmpty(LastName.getText())){
                    Toast.makeText(manualSchedule.this,"Please Enter Last Name.", Toast.LENGTH_LONG).show();
                    LastName.setError("Last Name is required. ");
                    LastName.requestFocus();
                }else if (!Patterns.EMAIL_ADDRESS.matcher(Email.getText()).matches()){
                    Toast.makeText(manualSchedule.this,"Please re-enter your email address.", Toast.LENGTH_LONG).show();
                    Email.setError("Valid email is required. ");
                    Email.requestFocus();
                }else if (TextUtils.isEmpty(Phone.getText())){
                    Toast.makeText(manualSchedule.this,"Please Enter Mobile Number.", Toast.LENGTH_LONG).show();
                    Phone.setError("Phone Number is required. ");
                    Phone.requestFocus();
                }else if (Phone.length() != 10){
                    Toast.makeText(manualSchedule.this,"Please re-enter your mobile number.", Toast.LENGTH_LONG).show();
                    Phone.setError("Mobile Number should be 10 digits. ");
                    Phone.requestFocus();
                }else if (!mobileMatcher.find()){
                    Toast.makeText(manualSchedule.this,"Please ensure that mobile number starts at 9.", Toast.LENGTH_LONG).show();
                    Phone.setError("Mobile Number is invalid. ");
                    Phone.requestFocus();
                }else if (TextUtils.isEmpty(ScheduleDate.getText())){
                    Toast.makeText(manualSchedule.this,"Please enter appointment date.", Toast.LENGTH_LONG).show();
                    ScheduleDate.setError("Appointment date is required. ");
                    ScheduleDate.requestFocus();
                }else if (TextUtils.isEmpty(ScheduleTime.getText())){
                    Toast.makeText(manualSchedule.this,"Please enter appointment time.", Toast.LENGTH_LONG).show();
                    ScheduleTime.setError("Appointment time is required. ");
                    ScheduleTime.requestFocus();
                }else if(TextUtils.isEmpty(selected_service)){
                    Toast.makeText(manualSchedule.this,"Please enter services. ", Toast.LENGTH_LONG).show();
                    auto_completeTxt.setError("Type of service is required.");
                    auto_completeTxt.requestFocus();
                } else if(!acceptTerms.isChecked()){
                    Toast.makeText(manualSchedule.this,"Please accept terms and conditions to proceed.", Toast.LENGTH_LONG).show();
                    acceptTerms.setError("Accept terms and conditions is required. ");
                }else{
                    insertData();
                    Intent intent = new Intent(manualSchedule.this, RequestConfirmation.class);
                    startActivity(intent);
                }
            }
        });

        acceptTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(manualSchedule.this, TermsConditions.class);
                startActivity(intent);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Services dropdown code on click
        auto_completeTxt = findViewById(R.id.auto_complete_txt);
        adapterItems = new ArrayAdapter<String>(this, R.layout.list_services, item);
        auto_completeTxt.setAdapter(adapterItems);

        auto_completeTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                selected_service = item;
            }
        });
    }

    //// Add Walk in Patient method
    private void insertData() {

        if(TextUtils.isEmpty(Address.getText())){
            Address.setText("N/A");
        }
        String BookingType = "Book Without Sign Up";

        Map<String, Object> map = new HashMap<>();
        map.put("firstname", FirstName.getText().toString());
        map.put("lastname", LastName.getText().toString());
        map.put("address", Address.getText().toString());
        map.put("phonenumber", Phone.getText().toString());
        map.put("appointmentdate", ScheduleDate.getText().toString());
        map.put("appointmenttime", ScheduleTime.getText().toString());
        map.put("services", selected_service);
        map.put("BookingType", BookingType);

        FirebaseDatabase.getInstance().getReference().child("Approval").push()
                .setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(manualSchedule.this, "Error while Insertion.", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    // Set Date Schedule
    private void updateDate() {
        ScheduleDate.setText(new StringBuilder().append(month + 1).append('/').append(day).append('/')
                .append(year));
    }
    // Set Time Schedule
    private void updateTime() {
        String time;
        try {
            time = hours + ":" + min;
            final SimpleDateFormat _24HourSDF  = new SimpleDateFormat("HH:mm");
            SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
            Date _24HourDt = _24HourSDF.parse(time);
            time = _12HourSDF.format(_24HourDt);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        ScheduleTime.setText(time);
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

    private TimePickerDialog.OnTimeSetListener timeListener =
            new TimePickerDialog.OnTimeSetListener() {

                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    hours = hourOfDay;
                    min = minute;
                    updateTime();
                }

            };

    // Method to display dialog for date and time schedule
    protected Dialog onCreateDialog(int id){
        switch(id) {

            case DATE_DIALOG_ID:
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE,1);
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, dateListener, year, month, day);
                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                return datePickerDialog;
            case TIME_DIALOG_ID:
                return new TimePickerDialog(this, timeListener, hours, min, false);
        }
        return null;
    }
}