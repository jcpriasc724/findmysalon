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
import com.findmysalon.model.Business;
import com.findmysalon.model.Staff;

import java.util.ArrayList;

public class BusinessAdapter extends RecyclerView.Adapter<BusinessAdapter.BusinessHolder> {

    Context context;
    ArrayList<Business> list;

    public BusinessAdapter(Context context, ArrayList<Business> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public BusinessHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BusinessHolder(LayoutInflater.from(context).inflate(R.layout.item_business, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BusinessHolder holder, int position) {

        holder.txtNameBusiness.setText(list.get(position).getBusinessName());
        holder.txtAddress.setText(list.get(position).getAddress());
        holder.txtPhoneNumber.setText(list.get(position).getPhoneNumber());
        holder.rtbBusiness.setRating(list.get(position).getRating());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class BusinessHolder extends RecyclerView.ViewHolder {

        TextView txtNameBusiness, txtAddress, txtPhoneNumber;
        RatingBar rtbBusiness;

        public BusinessHolder(@NonNull View itemView) {
            super(itemView);

            txtNameBusiness = (TextView) itemView.findViewById(R.id.txt_name_business);
            txtAddress = (TextView) itemView.findViewById(R.id.txt_address);
            txtPhoneNumber = (TextView) itemView.findViewById(R.id.txt_phone_number);
            rtbBusiness = (RatingBar) itemView.findViewById(R.id.rtb_business);

        }
    }
}
