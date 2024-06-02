package com.example.loginexperiment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FeedbackFragment extends Fragment {
    RecyclerView recyclerView;
    feedbackAdapter feedbackAdapt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feedback, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<GetterSetter> options =
                new FirebaseRecyclerOptions.Builder<GetterSetter>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("feedback").orderByChild("appointmentdate"), GetterSetter.class)
                        .build();

        feedbackAdapt = new feedbackAdapter(options);
        recyclerView.setAdapter(feedbackAdapt);
    }

    @Override
    public void onStart() {
        super.onStart();
        feedbackAdapt.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        feedbackAdapt.stopListening();
    }
}