package com.example.user_app;

import static android.content.ContentValues.TAG;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class DentistFragment extends Fragment {
    private TextView textView_show_fullname, textView_show_address, textView_show_email, textView_show_phone, textSpecialization;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dentist, container, false);
        // connect main clinic information to the patients dentist dashboard so that patient could see the updated information of the clinic.
        textView_show_fullname = view.findViewById(R.id.textView_show_fullname);
        textView_show_address = view.findViewById(R.id.textView_show_address);
        textView_show_email = view.findViewById(R.id.textView_show_email);
        textView_show_phone = view.findViewById(R.id.textView_show_phone);
        textSpecialization = view.findViewById(R.id.textSpecialization);

        showProfile();
        return view;
    }
    private void showProfile() {

        //Extract user reference from data base from registered user
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Main");
        referenceProfile.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readWriteUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if (readWriteUserDetails != null) {
                    textView_show_fullname.setText("Dr. "+ readWriteUserDetails.FirstName + " " + readWriteUserDetails.LastName);
                    textView_show_address.setText(readWriteUserDetails.Address);
                    textView_show_phone.setText(readWriteUserDetails.Phone);
                    textSpecialization.setText(readWriteUserDetails.textdescription);

//                    Uri uri = firebaseUser.getPhotoUrl();
//                    imageView = findViewById (R.id.imageView_profile_dp);
//
//                    //image viewer set image uri
//                    Picasso.with(getApplication()).load(uri).into(imageView);

                } else {
                    Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }
}