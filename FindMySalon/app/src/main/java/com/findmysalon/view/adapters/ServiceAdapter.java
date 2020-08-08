package com.findmysalon.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.findmysalon.R;
import com.findmysalon.model.Service;

import java.util.ArrayList;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceHolder>{

    Context context;
    ArrayList<Service> servicesList;

    public ServiceAdapter(Context context, ArrayList<Service> servicesList) {
        this.context = context;
        this.servicesList = servicesList;
    }

    @NonNull
    @Override
    public ServiceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ServiceHolder(LayoutInflater.from(context).inflate(R.layout.item_service, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceHolder holder, int position) {

        holder.txtNameService.setText(servicesList.get(position).getNameService());
        holder.txtCategory.setText(servicesList.get(position).getCategory().getNameCategory());
        holder.txtPrice.setText(servicesList.get(position).getPrice().toString());
        holder.txtDuration.setText(servicesList.get(position).getDuration().toString());
        holder.txtDescription.setText(servicesList.get(position).getDescription());

    }

    @Override
    public int getItemCount() {
        return servicesList.size();
    }

    class ServiceHolder extends RecyclerView.ViewHolder {

        TextView txtNameService, txtCategory, txtPrice, txtDuration, txtDescription;

        public ServiceHolder(@NonNull View itemView) {
            super(itemView);
            txtNameService = (TextView) itemView.findViewById(R.id.txt_name_service);
            txtCategory = (TextView) itemView.findViewById(R.id.txt_category);
            txtPrice = (TextView) itemView.findViewById(R.id.txt_price);
            txtDuration = (TextView) itemView.findViewById(R.id.txt_duration);
            txtDescription = (TextView) itemView.findViewById(R.id.txt_description);
        }
    }
}
