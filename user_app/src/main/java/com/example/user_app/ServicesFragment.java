package com.example.user_app;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ServicesFragment extends Fragment {

    // store data from array serviceItemNames[] and serviceItemDescription[]
    ArrayList<servicesItemValues> ServicesItemValues = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_services, container, false);;
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        setUpServicesItemValues();

        ServicesAdapter adapter = new ServicesAdapter(getContext(), ServicesItemValues);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // Inflate the layout for this fragment
        return view;

    }

    private void setUpServicesItemValues(){
        // pull data from strings.xml and assign inside the array

        String[] serviceItemNames = getResources().getStringArray(R.array.serviceName);
        String[] serviceItemDescriptions = getResources().getStringArray(R.array.serviceDescription);

        // loop through these arrays and create model base on iteration in for loop
        for (int i = 0; i < serviceItemNames.length; i++){
            // create model class for each of the items
            ServicesItemValues.add(new servicesItemValues(serviceItemNames[i], serviceItemDescriptions[i]));
        }
    }
}