package com.findmysalon.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.findmysalon.R;
import com.findmysalon.model.Service;
import com.findmysalon.model.Staff;
import com.findmysalon.model.StaffRoster;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DateBookingAdapter extends RecyclerView.Adapter<DateBookingAdapter.DateBookingHolder>{

    Context context;
    ArrayList<StaffRoster> rosterList;
    Service service;
    Staff staff;


    public DateBookingAdapter(Context context,
                              ArrayList<StaffRoster> rosterList,
                              Service service,
                              Staff staff) {
        this.context = context;
        this.rosterList = rosterList;
        this.service = service;
        this.staff = staff;
    }

    @NonNull
    @Override
    public DateBookingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DateBookingHolder(LayoutInflater.from(context).inflate(R.layout.item_date_booking, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DateBookingHolder holder, int position) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(context.getResources().getString(R.string.pattern));
        String dateBooking = simpleDateFormat.format(rosterList.get(position).getDateRoster());

        HourBookingAdapter hoursBookingAdapter =
                new HourBookingAdapter(
                        context,
                        rosterList.get(position).getHoursAvailable(),
                        service,
                        staff,
                        rosterList.get(position).getDateRoster()
                );
        holder.recHoursAvailable.setAdapter(hoursBookingAdapter);

        holder.txtDateBooking.setText(dateBooking);

        holder.imgExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.recHoursAvailable.getVisibility()==View.GONE){
                    holder.recHoursAvailable.setVisibility(View.VISIBLE);
                    holder.imgExpand.setImageResource(R.drawable.ic_expand_less);
                } else {
                    holder.recHoursAvailable.setVisibility(View.GONE);
                    holder.imgExpand.setImageResource(R.drawable.ic_expand_more);
                }

            }
        });

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
        ImageView imgExpand;

        public DateBookingHolder(@NonNull View itemView) {
            super(itemView);
            txtDateBooking = (TextView) itemView.findViewById(R.id.txt_date_booking);
            imgExpand = itemView.findViewById(R.id.img_expand);
            recHoursAvailable = (RecyclerView) itemView.findViewById(R.id.rec_hours_available);
            recHoursAvailable.setLayoutManager(new GridLayoutManager(context, 3));


        }
    }
}
