package com.findmysalon.view.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.findmysalon.R;
import com.findmysalon.model.Service;
import com.findmysalon.model.Staff;
import com.findmysalon.model.StaffRoster;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HourBookingAdapter extends RecyclerView.Adapter<HourBookingAdapter.HourBookingHolder>{

    Context context;
    ArrayList<String> hoursList;
    Service service;
    Staff staff;
    Date selectedDate;

    public HourBookingAdapter(
            Context context,
            ArrayList<String> hoursList,
            Service service,
            Staff staff,
            Date selectedDateStr
    ) {
        this.context = context;
        this.hoursList = hoursList;
        this.service = service;
        this.staff = staff;
        this.selectedDate = selectedDateStr;
    }

    @NonNull
    @Override
    public HourBookingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HourBookingHolder(LayoutInflater.from(context).inflate(R.layout.item_hour_booking, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HourBookingHolder holder, int position) {

        holder.txtHourBooking.setText(hoursList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("service", service);
                bundle.putSerializable("staff", staff);
                bundle.putSerializable("selectedDate", selectedDate);
                bundle.putString("selectedTime", hoursList.get(position));
                Navigation.findNavController(v).navigate(R.id.nav_appointment_confirmation, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return hoursList.size();
    }

    class HourBookingHolder extends RecyclerView.ViewHolder {

        TextView txtHourBooking;

        public HourBookingHolder(@NonNull View itemView) {
            super(itemView);
            txtHourBooking = (TextView) itemView.findViewById(R.id.txt_hour_booking);
        }
    }
}
