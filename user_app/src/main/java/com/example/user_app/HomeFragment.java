package com.example.user_app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.PeriodicSync;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.content.AsyncTaskLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;


public class HomeFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    private ImageView imageView;
    private FirebaseAuth authProfile;
    private TextView fullnametext;
    private String fullname, dob;

    ImageView ic_facebook, ic_messenger;
    TextView fblink, messengerlink;

    Uri uri;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // display image/profile
        imageView = (ImageView) view.findViewById (R.id.imageView_profile_dp);
        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        // display fullname and age
        fullnametext = (TextView) view.findViewById (R.id.fullnametext);

        showUserProfile(firebaseUser);

        // social media

        ic_facebook = view.findViewById(R.id.ic_facebook);
        ic_messenger = view.findViewById(R.id.ic_messenger);
        fblink = view.findViewById(R.id.fblink);
        messengerlink = view.findViewById(R.id.messengerlink);

        ic_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.facebook.com/abeygordondental";

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        fblink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.facebook.com/abeygordondental";

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        messengerlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.messenger.com/t/100883478139426";

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        ic_messenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.facebook.com/messages/t/100883478139426";

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        return view;
    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();
        // Extracting user reference from data base from registered users.

        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Users");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readWriteUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if(readWriteUserDetails != null){
                    fullname = firebaseUser.getDisplayName();
                    dob = readWriteUserDetails.DOB;

                    // compute age
                    // check if dob is empty or not
                    if (!dob.isEmpty()){
                        // Extracting saved dd/mm/yyyy into different variable

                        String splitDOB[] = dob.split("/");
                        int day = Integer.parseInt(splitDOB[0]);
                        int month = Integer.parseInt(splitDOB[1])-1;
                        int year = Integer.parseInt(splitDOB[2]);

                        // get the birthdate
                        LocalDate birthdate = LocalDate.of(year, day, month);

                        // Current Date
                        LocalDate currentDate = LocalDate.now();

                        // Calculate Age
                        int age = calculateAge(birthdate, currentDate);

                        // set name and age to display
                        fullnametext.setText(fullname + ", " + age);
                    }else{
                        fullnametext.setText(fullname);
                    }

                    // set user DP AFTER USER HAS UPLOADED image

                    uri = firebaseUser.getPhotoUrl();

                    //image viewer set image uri
                    Picasso.with(getContext()).load(uri).into(imageView);
                } else{
                    Toast.makeText(getActivity(),"Something went wrong. ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(),"Something went wrong. User's details are not available. ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static int calculateAge(LocalDate birthdate, LocalDate currentDate) {
        // Calculate period between birthdate and current date
        Period period = Period.between(birthdate, currentDate);

        return period.getYears();
    }

}