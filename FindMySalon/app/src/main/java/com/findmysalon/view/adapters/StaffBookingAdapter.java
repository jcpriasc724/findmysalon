package com.findmysalon.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.findmysalon.R;
import com.findmysalon.model.Staff;

import java.util.ArrayList;

public class StaffBookingAdapter extends RecyclerView.Adapter<StaffBookingAdapter.StaffHolder>{

    Context context;
    ArrayList<Staff> list;

    public StaffBookingAdapter(Context context, ArrayList<Staff> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public StaffHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StaffHolder(LayoutInflater.from(context).inflate(R.layout.item_staff_booking, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StaffHolder holder, int position) {

        holder.txtNameStaff.setText(list.get(position).getName());
        holder.rtbStaff.setRating(list.get(position).getRating());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class StaffHolder extends RecyclerView.ViewHolder {

        TextView txtNameStaff;
        RatingBar rtbStaff;

        public StaffHolder(@NonNull View itemView) {
            super(itemView);
            txtNameStaff = (TextView) itemView.findViewById(R.id.txt_name_staff);
            rtbStaff = (RatingBar) itemView.findViewById(R.id.rtb_staff);
        }
    }
}
