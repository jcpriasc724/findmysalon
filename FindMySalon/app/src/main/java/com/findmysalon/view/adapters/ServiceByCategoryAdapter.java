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

public class ServiceByCategoryAdapter extends RecyclerView.Adapter<ServiceByCategoryAdapter.ServiceHolder>{

    Context context;
    ArrayList<Service> servicesList;

    public ServiceByCategoryAdapter(Context context, ArrayList<Service> servicesList) {
        this.context = context;
        this.servicesList = servicesList;
    }

    @NonNull
    @Override
    public ServiceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ServiceHolder(LayoutInflater.from(context).inflate(R.layout.item_service_by_category, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceHolder holder, int position) {
        holder.txtNameService.setText(servicesList.get(position).getNameService());
    }

    @Override
    public int getItemCount() {
        return servicesList.size();
    }

    class ServiceHolder extends RecyclerView.ViewHolder {

        TextView txtNameService;

        public ServiceHolder(@NonNull View itemView) {
            super(itemView);
            txtNameService = (TextView) itemView.findViewById(R.id.txt_service_by_category);
        }
    }
}