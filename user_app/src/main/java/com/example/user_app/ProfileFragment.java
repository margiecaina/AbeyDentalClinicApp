package com.example.user_app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    Button logout, ChangePassword;

    ImageView UpdateProfile;
    private TextView textViewWelcome, textViewFirstName, textViewLastName, textViewFullname, textViewEmail, textViewPhone, textViewDob, textViewAddress;
    private TextView textView_gender;
    private String firstname, lastname, fullname, email, phone, dob, address, gender;
    private ImageView imageView;
    private FirebaseAuth authProfile;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // find button by id
        logout = (Button) view.findViewById(R.id.logout);

        // set onclick listener on logout button
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), StartActivity.class));
            }
        });

        // update password button
        ChangePassword = (Button) view.findViewById(R.id.ChangePassword);
        ChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ChangePasswordActivity.class));
            }
        });

        // update profile button

        UpdateProfile = (ImageView) view.findViewById(R.id.UpdateProfile);

        UpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), UpdateProfileActivity.class));
            }
        });


        textViewWelcome = (TextView) view.findViewById (R.id.textView_show_Welcome);
        textViewFullname = (TextView) view.findViewById (R.id.textView_show_fullname);
        textViewEmail = (TextView) view.findViewById (R.id.textView_show_email);
        textViewPhone = (TextView) view.findViewById (R.id.textView_show_phone);
        textViewDob = (TextView) view.findViewById (R.id.textView_show_dob);
        textViewAddress = (TextView) view.findViewById (R.id.textView_show_address);
        textView_gender = (TextView) view.findViewById(R.id.textView_gender);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        if(firebaseUser == null){
            Toast.makeText(getActivity(),"Something went wrong. User's details are not available. ", Toast.LENGTH_SHORT).show();
        } else {
            checkifEmailVerified(firebaseUser);
            showUserProfile(firebaseUser);
        }

        // Set OnclickLister on image view to open upload profile pic
        imageView = view.findViewById (R.id.imageView_profile_dp);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), UploadProfilePicture.class));
            }
        });

        return view;
    }

    //Users coming to ProfileFragment after successful registration
    private void checkifEmailVerified(FirebaseUser firebaseUser) {
        if(!firebaseUser.isEmailVerified()){
            showAlertDialog();
        }
    }
    private void showAlertDialog() {
        // Set up alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Email Not Verified");
        builder.setMessage("Please verify your email now. You can not login without email verification next time. ");

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
                    email = firebaseUser.getEmail();
                    phone = readWriteUserDetails.Phone;
                    firstname = readWriteUserDetails.FirstName;
                    dob = readWriteUserDetails.DOB;
                    address = readWriteUserDetails.Address;
                    gender = readWriteUserDetails.Gender;

                    textViewWelcome.setText("Welcome, " + firstname);
                    textViewFullname.setText(fullname);
                    textViewEmail.setText(email);
                    textViewPhone.setText(phone);
                    textViewAddress.setText(address);
                    textViewDob.setText(dob);
                    textView_gender.setText(gender);

                    // set user DP AFTER USER HAS UPLOADED

                    Uri uri = firebaseUser.getPhotoUrl();

                    //image viewer set image uri
                    Picasso.with(getActivity()).load(uri).into(imageView);
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

}