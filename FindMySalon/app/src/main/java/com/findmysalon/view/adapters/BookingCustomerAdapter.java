package com.findmysalon.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.findmysalon.R;
import com.findmysalon.model.Booking;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class BookingCustomerAdapter extends RecyclerView.Adapter<BookingCustomerAdapter.BookingCustomerHolder> {

    Context context;
    ArrayList<Booking> list;

    public BookingCustomerAdapter(Context context, ArrayList<Booking> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public BookingCustomerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BookingCustomerHolder(LayoutInflater.from(context).inflate(R.layout.item_booking_customers, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BookingCustomerHolder holder, int position) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(context.getResources().getString(R.string.pattern));
        String dateBooking = simpleDateFormat.format(list.get(position).getDateBooking());

        holder.txtDateBooking.setText(dateBooking);
        holder.txtTimeBooking.setText(list.get(position).getStartTime()+" - "+list.get(position).getEndTime());
        holder.txtStaff.setText(list.get(position).getStaff().getName());
        holder.txtBusinessName.setText(list.get(position).getBusiness().getStoreName());
        holder.txtAddress.setText(list.get(position).getBusiness().getAddress());
        holder.txtService.setText(list.get(position).getService().getNameService());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Navigation.findNavController(v).navigate(R.id.nav_business_detail);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class BookingCustomerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtDateBooking, txtTimeBooking, txtStaff, txtBusinessName, txtAddress, txtService;

        public BookingCustomerHolder(@NonNull View itemView) {
            super(itemView);

            txtDateBooking = (TextView) itemView.findViewById(R.id.txt_date_booking);
            txtTimeBooking = (TextView) itemView.findViewById(R.id.txt_time_booking);
            txtStaff = (TextView) itemView.findViewById(R.id.txt_staff);
            txtBusinessName = (TextView) itemView.findViewById(R.id.txt_business);
            txtAddress = (TextView) itemView.findViewById(R.id.txt_address);
            txtService = (TextView) itemView.findViewById(R.id.txt_service);

        }

        @Override
        public void onClick(View v) {

        }
    }
}
