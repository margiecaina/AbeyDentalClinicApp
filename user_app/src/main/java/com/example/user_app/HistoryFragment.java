package com.example.user_app;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class HistoryFragment extends Fragment {


    private FirebaseAuth authProfile;
    HistoryAdapter historyAdapter;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        String userID = firebaseUser.getUid();
        FirebaseRecyclerOptions<GetterSetter> options =
                new FirebaseRecyclerOptions.Builder<GetterSetter>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("Users").child(userID).child("history"), GetterSetter.class)
                        .build();

        historyAdapter = new HistoryAdapter(options);
        recyclerView.setAdapter(historyAdapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        historyAdapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        historyAdapter.stopListening();
    }
}