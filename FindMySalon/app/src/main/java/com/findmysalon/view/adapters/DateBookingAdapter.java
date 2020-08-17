package com.findmysalon.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.findmysalon.R;
import com.findmysalon.model.Service;
import com.findmysalon.model.StaffRoster;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DateBookingAdapter extends RecyclerView.Adapter<DateBookingAdapter.DateBookingHolder>{

    Context context;
    ArrayList<StaffRoster> rosterList;
    String pattern = "EEE, d MMM yyyy";

    public DateBookingAdapter(Context context, ArrayList<StaffRoster> rosterList) {
        this.context = context;
        this.rosterList = rosterList;
    }

    @NonNull
    @Override
    public DateBookingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DateBookingHolder(LayoutInflater.from(context).inflate(R.layout.item_date_booking, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DateBookingHolder holder, int position) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String dateBooking = simpleDateFormat.format(rosterList.get(position).getDateRoster());

        HourBookingAdapter hoursBookingAdapter = new HourBookingAdapter(context, rosterList.get(position).getHoursAvailable());
        holder.recHoursAvailable.setAdapter(hoursBookingAdapter);

        holder.txtDateBooking.setText(dateBooking);
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Navigation.findNavController(v).navigate(R.id.nav_appointment_confirmation);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return rosterList.size();
    }

    class DateBookingHolder extends RecyclerView.ViewHolder {

        TextView txtDateBooking;
        RecyclerView recHoursAvailable;

        public DateBookingHolder(@NonNull View itemView) {
            super(itemView);
            txtDateBooking = (TextView) itemView.findViewById(R.id.txt_date_booking);

            recHoursAvailable = (RecyclerView) itemView.findViewById(R.id.rec_hours_available);
            recHoursAvailable.setLayoutManager(new GridLayoutManager(context, 3));


        }
    }
}
