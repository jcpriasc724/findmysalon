package com.findmysalon.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
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
        holder.txtPrice.setText("$ "+servicesList.get(position).getPrice().toString());
        holder.txtDuration.setText(servicesList.get(position).getDuration().toString()+" min");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.nav_staff_booking);
            }
        });
    }

    @Override
    public int getItemCount() {
        return servicesList.size();
    }

    class ServiceHolder extends RecyclerView.ViewHolder {

        TextView txtNameService;
        TextView txtPrice;
        TextView txtDuration;

        public ServiceHolder(@NonNull View itemView) {
            super(itemView);
            txtNameService = (TextView) itemView.findViewById(R.id.txt_service_by_category);
            txtPrice = (TextView) itemView.findViewById(R.id.txt_price);
            txtDuration = (TextView) itemView.findViewById(R.id.txt_duration);
        }
    }
}
