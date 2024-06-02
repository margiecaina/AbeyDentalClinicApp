package com.example.loginexperiment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.database.FirebaseDatabase;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class manualSchedule extends AppCompatActivity{
    EditText ScheduleDate;
    EditText FirstName, LastName, Address, Phone;

    Button backBtn;
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

        hours = cal.get(Calendar.HOUR);
        min = cal.get(Calendar.MINUTE);

        // On click of Submit Schedule Button

        Button submitSchedule;

        FirstName = (EditText) findViewById(R.id.FirstName);
        LastName = (EditText) findViewById(R.id.LastName);
        Address = (EditText) findViewById(R.id.Address);
        Phone = (EditText) findViewById(R.id.Phone);

        submitSchedule = (Button) findViewById(R.id.submitSchedule);
        backBtn = (Button) findViewById(R.id.backBtn);
        String textPhone = Phone.getText().toString();

        submitSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(FirstName.getText())){
                    Toast.makeText(manualSchedule.this,"Please enter first name. ", Toast.LENGTH_LONG).show();
                    FirstName.setError("Firstname is required.");
                    FirstName.requestFocus();
                }else if(TextUtils.isEmpty(LastName.getText())){
                    Toast.makeText(manualSchedule.this,"Please enter lastname. ", Toast.LENGTH_LONG).show();
                    LastName.setError("lastname is required.");
                    LastName.requestFocus();
                }else if(TextUtils.isEmpty(Address.getText())){
                    Toast.makeText(manualSchedule.this,"Please enter complete address. ", Toast.LENGTH_LONG).show();
                    Address.setError("Complete Address is required.");
                    Address.requestFocus();
                }else if(TextUtils.isEmpty(ScheduleDate.getText())){
                    Toast.makeText(manualSchedule.this,"Please enter appointment date. ", Toast.LENGTH_LONG).show();
                    ScheduleDate.setError("Appointment date is required.");
                    ScheduleDate.requestFocus();
                }else if(TextUtils.isEmpty(Phone.getText())){
                    Toast.makeText(manualSchedule.this,"Please enter phone number. ", Toast.LENGTH_LONG).show();
                    Phone.setError("Phone Number is required.");
                    Phone.requestFocus();
                }else if (Phone.length() != 10){
                    Toast.makeText(manualSchedule.this,"Please re-enter your mobile number.", Toast.LENGTH_LONG).show();
                    Phone.setError("Mobile Number should be 10 digits. ");
                    Phone.requestFocus();
                }else if(TextUtils.isEmpty(selected_service)){
                    Toast.makeText(manualSchedule.this,"Please enter services. ", Toast.LENGTH_LONG).show();
                    auto_completeTxt.setError("Type of service is required.");
                    auto_completeTxt.requestFocus();
                }else{
                    insertData();
                    finish();
                }
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

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("KK:mm aaa");
        String dateTime = simpleDateFormat.format(calendar.getTime()).toString();

        Map<String, Object> map = new HashMap<>();
        map.put("firstname", FirstName.getText().toString());
        map.put("lastname", LastName.getText().toString());
        map.put("address", Address.getText().toString());
        map.put("phonenumber", Phone.getText().toString());
        map.put("appointmentdate", ScheduleDate.getText().toString());
        map.put("services", selected_service);
        map.put("BookingType", "Walk-in");
        map.put("realtime", dateTime);

        FirebaseDatabase.getInstance().getReference().child("Appointment").push()
                .setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(manualSchedule.this, "Error", Toast.LENGTH_SHORT).show();
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
                calendar.add(Calendar.DATE,0);
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, dateListener, year, month, day);
                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                return datePickerDialog;
            case TIME_DIALOG_ID:
                return new TimePickerDialog(this, timeListener, hours, min, false);
        }
        return null;
    }
}