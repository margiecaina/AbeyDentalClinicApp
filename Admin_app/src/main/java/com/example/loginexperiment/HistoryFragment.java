package com.example.loginexperiment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class HistoryFragment extends Fragment {

    RecyclerView recyclerView;
    historyAdapter historyAdapter;
    private TextView numberOfPatientsToday;

    private DatabaseReference mDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerViewHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Query to order appointments by date
        Query query = FirebaseDatabase.getInstance().getReference().child("history").orderByChild("appointmentdate");

        FirebaseRecyclerOptions<GetterSetter> options =
                new FirebaseRecyclerOptions.Builder<GetterSetter>()
                        .setQuery(query, GetterSetter.class)
                        .build();

        historyAdapter = new historyAdapter(options);
        recyclerView.setAdapter(historyAdapter);

        numberOfPatientsToday = view.findViewById(R.id.numberOfPatientsToday);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("history");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int sum = 0;
                for (DataSnapshot ds : snapshot.getChildren()) {
                    sum++;
                }
                numberOfPatientsToday.setText(String.valueOf(sum));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error in getting the total number of completed appointment.", Toast.LENGTH_SHORT).show();
            }
        });

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
