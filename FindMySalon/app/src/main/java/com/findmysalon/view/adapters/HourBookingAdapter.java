package com.findmysalon.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.findmysalon.R;
import com.findmysalon.model.StaffRoster;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class HourBookingAdapter extends RecyclerView.Adapter<HourBookingAdapter.HourBookingHolder>{

    Context context;
    ArrayList<String> hoursList;

    public HourBookingAdapter(Context context, ArrayList<String> hoursList) {
        this.context = context;
        this.hoursList = hoursList;
    }

    @NonNull
    @Override
    public HourBookingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HourBookingHolder(LayoutInflater.from(context).inflate(R.layout.item_hour_booking, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HourBookingHolder holder, int position) {

        holder.txtHourBooking.setText(hoursList.get(position));
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Navigation.findNavController(v).navigate(R.id.nav_staff_booking);
//            }
//        });
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
