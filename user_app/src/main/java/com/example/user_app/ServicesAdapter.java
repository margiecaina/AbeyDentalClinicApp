package com.example.user_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.MyViewHolder> {

    Context MyContext;
    ArrayList<servicesItemValues> MyServiceItemValues;
    public ServicesAdapter(Context context, ArrayList<servicesItemValues> ServiceItemValues){
        this.MyContext = context;
        this.MyServiceItemValues = ServiceItemValues;
    }

    public ServicesAdapter() {

    }

    @NonNull
    @Override
    public ServicesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate the layout (Giving a look to our rows)
        LayoutInflater inflater = LayoutInflater.from(MyContext);
        View view = inflater.inflate(R.layout.services_item, parent, false);
        return new ServicesAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServicesAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // assigning values to the view we created in the recyclerview - service_item.xml
        // based on the position of the recyclerview

        holder.tvServiceName.setText(MyServiceItemValues.get(position).getServicesName());
        holder.tvServiceDescription.setText(MyServiceItemValues.get(position).getServicesDescription());

        // on click of the button get the service name and open the activity_book_appointment
        holder.btBookNow.setOnClickListener(v -> {
            Intent intent = new Intent(MyContext, BookAppointment.class);
            intent.putExtra("keyServiceType", holder.tvServiceName.getText());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        // to know the number of items to be displayed
        return MyServiceItemValues.size();
    }

    public static class MyViewHolder extends  RecyclerView.ViewHolder{
        // kinda like an oncreate method
        // grabbing views from the service_item.xml file

        TextView tvServiceName, tvServiceDescription;
        AppCompatButton btBookNow;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvServiceName = itemView.findViewById(R.id.serviceNameTextView);
            tvServiceDescription = itemView.findViewById(R.id.serviceDescriptionTextView);
            btBookNow = itemView.findViewById(R.id.bookNowButton);
        }
    }
}
