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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class ApprovalFragment extends Fragment {

    RecyclerView recyclerView;
    ApprovalAdapter approvalAdapter;

    private TextView numberOfPatientsToday;
    private DatabaseReference mDatabase;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_approval, container, false);

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Query query = FirebaseDatabase.getInstance().getReference().child("Approval").orderByChild("appointmentdate");

        FirebaseRecyclerOptions<GetterSetter> options =
                new FirebaseRecyclerOptions.Builder<GetterSetter>()
                        .setQuery(query, GetterSetter.class)
                        .build();

        approvalAdapter = new ApprovalAdapter(options);
        recyclerView.setAdapter(approvalAdapter);

        // check the total number of patients

        numberOfPatientsToday = (TextView) view.findViewById(R.id.numberOfPatientsToday);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Approval");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int sum = 0;
                for(DataSnapshot ds : snapshot.getChildren()) {
                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
                    Object firstname = map.get("firstname");
                    sum ++;
                    numberOfPatientsToday.setText(String.valueOf(sum));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        approvalAdapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        approvalAdapter.stopListening();
    }
}