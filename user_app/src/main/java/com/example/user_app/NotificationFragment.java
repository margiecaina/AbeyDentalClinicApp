package com.example.user_app;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class NotificationFragment extends Fragment {
    private FirebaseAuth authProfile;
    NotificationAdapter notificationAdapter;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        String userID = firebaseUser.getUid();
        FirebaseRecyclerOptions<GetterSetter> options =
                new FirebaseRecyclerOptions.Builder<GetterSetter>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("Users").child(userID).child("Appointment"), GetterSetter.class)
                        .build();

        notificationAdapter = new NotificationAdapter(options);
        recyclerView.setAdapter(notificationAdapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        notificationAdapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        notificationAdapter.stopListening();
    }
}