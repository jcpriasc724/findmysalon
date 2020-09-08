package com.findmysalon.view.adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.findmysalon.R;
import com.findmysalon.model.Service;

import java.util.ArrayList;
import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceHolder>{

    Context context;
    List<Service> servicesList;

    public ServiceAdapter(Context context, List<Service> servicesList) {
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
        holder.txtPrice.setText("$ " + servicesList.get(position).getPrice().toString());
        holder.txtDuration.setText(servicesList.get(position).getDuration().toString() + " mins");
        holder.txtDescription.setText(servicesList.get(position).getDescription());
        holder.txtStatus.setText(servicesList.get(position).getDisplayStatus().compareTo("S") == 0 ? "Enable" : "Disable");
//        holder.txtEdit.setClickable(true);
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("id", servicesList.get(position).getId());
                Navigation.findNavController(v).navigate(R.id.nav_add_service,bundle);
            }
        });

    }

    @Override
    public int getItemCount() {
        return servicesList.size();
    }

    class ServiceHolder extends RecyclerView.ViewHolder {

        TextView txtNameService, txtCategory, txtPrice, txtDuration, txtDescription, txtStatus;
        LinearLayout container;

        public ServiceHolder(@NonNull View itemView) {
            super(itemView);
            txtNameService = (TextView) itemView.findViewById(R.id.txt_name_service);
            txtCategory = (TextView) itemView.findViewById(R.id.txt_category);
            txtPrice = (TextView) itemView.findViewById(R.id.txt_price);
            txtDuration = (TextView) itemView.findViewById(R.id.txt_duration);
            txtDescription = (TextView) itemView.findViewById(R.id.txt_description);
            txtStatus = (TextView) itemView.findViewById(R.id.txt_status);
            container = itemView.findViewById(R.id.container_service);
            //txtEdit = (TextView) itemView.findViewById(R.id.txt_edit);
        }
    }
}
