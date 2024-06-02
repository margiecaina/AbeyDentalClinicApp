package com.example.loginexperiment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;


public class ProfileFragment extends Fragment {

    Button logout, ChangePassword, accessCodeGenerator;
    ImageView imageView;
    private TextView textViewWelcome, textViewFirstName, textViewLastName, textViewFullname, textViewEmail, textViewPhone, textViewDob, textViewAddress, textView_specialization;
    private String firstname, lastname, fullname, email, phone, dob, address, specialization;
    private FirebaseAuth authProfile;

    ImageView UpdateProfile;
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


        // Set OnclickLister on image view to open upload profile pic
        imageView = (ImageView) view.findViewById (R.id.imageView_profile_dp);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), UploadProfilePicture.class));
            }
        });

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

        UpdateProfile = view.findViewById(R.id.UpdateProfile);

        UpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), UpdateProfileActivity.class));
            }
        });


        // access code generator to register new admin
        accessCodeGenerator  = (Button) view.findViewById(R.id.accessCodeGenerator);
        accessCodeGenerator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), accessCodeGenerator.class));
            }
        });

        textViewWelcome = (TextView) view.findViewById (R.id.textView_show_Welcome);
        textViewFullname = (TextView) view.findViewById (R.id.textView_show_fullname);
        textViewEmail = (TextView) view.findViewById (R.id.textView_show_email);
        textViewPhone = (TextView) view.findViewById (R.id.textView_show_phone);
        textViewAddress = (TextView) view.findViewById (R.id.textView_show_address);
        textView_specialization = (TextView) view.findViewById(R.id.textView_specialization);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        if(firebaseUser == null){
            Toast.makeText(getActivity(),"Something went wrong. User's details are not available. ", Toast.LENGTH_SHORT).show();
        } else {
            checkifEmailVerified(firebaseUser);
            showUserProfile(firebaseUser);
        }

        // get address and set address as the main address of the clinic
        address = textViewAddress.getText().toString();

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
        // Extracting user reference from data base from registered admin.
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Users");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readWriteUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                fullname = firebaseUser.getDisplayName();
                email = firebaseUser.getEmail();
                phone = readWriteUserDetails.Phone;
                firstname = readWriteUserDetails.FirstName;
                address = readWriteUserDetails.Address;
                specialization = readWriteUserDetails.textdescription;

                textViewWelcome.setText("Hello Admin, " + firstname);
                textViewFullname.setText(fullname);
                textViewEmail.setText(email);
                textViewPhone.setText(phone);
                textViewAddress.setText(address);
                textView_specialization.setText(specialization);


                // set user DP AFTER USER HAS UPLOADED

                Uri uri = firebaseUser.getPhotoUrl();

                //image viewer set image uri
                Picasso.with(getActivity()).load(uri).into(imageView);

                // if admin invisible the access code generator button
                // if super admin access code generator button became visible
                if (Objects.equals(readWriteUserDetails.UserAccess, "superadmin")){
                    accessCodeGenerator.setVisibility(View.VISIBLE);
                } else if (Objects.equals(readWriteUserDetails.UserAccess, "admin")){
                    accessCodeGenerator.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(),"Something went wrong. User's details are not available. ", Toast.LENGTH_SHORT).show();
            }
        });

    }
}