package com.example.user_app;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BookAppointment extends AppCompatActivity {

    ImageButton backbutton;
    CardView morningButton, afternoonButton;
    TextView errorTv;
    AppCompatButton nextBtn;
    String serviceTypeName;

    String datetext ="", session="";
    CalendarView calendarView;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);
        Calendar calendar1 = Calendar.getInstance();
        SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");
        String currentDate1 = sdf1.format(calendar1.getTime());
        datetext = currentDate1;
        calendarView = findViewById(R.id.calendarView);
        calendarView.setMinDate(System.currentTimeMillis() - 1000);
        // deselect current date
        calendarView.setSelected(false);
        // get type of service string
        serviceTypeName = getIntent().getStringExtra("keyServiceType");

        // activity exit
        backbutton = findViewById( R.id.backbutton);
        backbutton.setOnClickListener(v -> finish());

        // select schedule

        morningButton = findViewById(R.id.morningButton);

        // morning button active
        morningButton.setOnClickListener(v -> {
            afternoonButton.setCardElevation(10);
            morningButton.setCardElevation(30);
            session = "Morning Session 9am - 12pm";
        });

        afternoonButton = findViewById(R.id.afternoonButton);
        // afternoon button active
        afternoonButton.setOnClickListener(v -> {
            afternoonButton.setCardElevation(30);
            morningButton.setCardElevation(10);
            session = "Afternoon Session 1pm - 5pm";
        });

        // error textview
        errorTv = findViewById(R.id.displayError);


        // get calendar date selected
        calendarView.setOnDateChangeListener((arg0, year, month, date) -> {
            month += 1;
            String monthtext = String.valueOf(month);
            String datetxt = String.valueOf(date);
            if (month < 10){ monthtext = "0" + month;}
            if (date < 10){ datetxt = "0" + date;}
            datetext = monthtext + "/" + datetxt + "/" + year;
        });

        // next button
        nextBtn = findViewById(R.id.nextButton);

        // pass data to confirmation page
        // after clicking next

        nextBtn.setOnClickListener(v -> {
            // set error text if empty
            if(datetext.isEmpty() || session.isEmpty()){
                errorTv.setText("Please select date and session.");
            }else{
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                Calendar calendar;
                calendar = Calendar.getInstance();
                String currentDate = sdf.format(calendar.getTime());

                // check if appointment is the same day
                if(currentDate.equals(datetext)){
                    calendar = Calendar.getInstance();
                    SimpleDateFormat timeformat = new SimpleDateFormat("hh:mm:ss aaa");
                    String currentTime = timeformat.format(calendar.getTime());
                    String endTime;
                    if(session.equals("Morning Session 9am - 12pm")){
                        // if appointment is same day
                        //get current date

                        // if appointment is after 12pm
                        endTime = "12:00:00 PM";

                        if(currentTime.compareTo(endTime) < 0){
                            errorTv.setText("Appointment time is passed 12pm.");
                        }else{
                            Intent bookingData = new Intent(BookAppointment.this, RequestConfirmation.class);
                            bookingData.putExtra("appointmentdate", datetext);
                            bookingData.putExtra("services", serviceTypeName);
                            bookingData.putExtra("session", session);
                            startActivity(bookingData);
                            finish();
                        }

                    }else{
                        // if appointment is same day
                        //get current date

                        // if appointment is after 12pm
                        endTime = "5:00:00 PM";

                        if(currentTime.compareTo(endTime) < 0){
                            Intent bookingData = new Intent(BookAppointment.this, RequestConfirmation.class);
                            bookingData.putExtra("appointmentdate", datetext);
                            bookingData.putExtra("services", serviceTypeName);
                            bookingData.putExtra("session", session);
                            startActivity(bookingData);
                            finish();
                        }else{
                            errorTv.setText("Appointment time is passed 5pm. Clinic is closed.");
                        }
                    }
                }else{
                    Intent bookingData = new Intent(BookAppointment.this, RequestConfirmation.class);
                    bookingData.putExtra("appointmentdate", datetext);
                    bookingData.putExtra("services", serviceTypeName);
                    bookingData.putExtra("session", session);
                    startActivity(bookingData);
                    finish();
                }
            }
        });
    }

}