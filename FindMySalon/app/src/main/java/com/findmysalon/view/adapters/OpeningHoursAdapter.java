package com.findmysalon.view.adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.findmysalon.R;
import com.findmysalon.model.BusinessHour;
import com.findmysalon.model.Service;
import com.google.android.libraries.places.api.model.OpeningHours;

import java.util.ArrayList;

public class OpeningHoursAdapter extends RecyclerView.Adapter<OpeningHoursAdapter.OpeningHoursHolder>{

    Context context;
    ArrayList<BusinessHour> openingHoursList;

    public OpeningHoursAdapter(Context context, ArrayList<BusinessHour> openingHoursList) {
        this.context = context;
        this.openingHoursList = openingHoursList;
    }

    @NonNull
    @Override
    public OpeningHoursHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OpeningHoursHolder(LayoutInflater.from(context).inflate(R.layout.item_opening_hours, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OpeningHoursHolder holder, int position) {
        holder.txtWeekday.setText(openingHoursList.get(position).getDay());
        String businessHours;
        if(openingHoursList.get(position).getStatus() == 1){
            businessHours = openingHoursList.get(position).getOpeningHour()+":"+
                    openingHoursList.get(position).getOpeningMin()+" - "+
                    openingHoursList.get(position).getClosingHour()+":"+
                    openingHoursList.get(position).getClosingMin();
        }
        else{
            businessHours = "NA";
        }

        holder.txtHours.setText(businessHours);
    }

    @Override
    public int getItemCount() {
        return openingHoursList.size();
    }

    class OpeningHoursHolder extends RecyclerView.ViewHolder {

        TextView txtWeekday;
        TextView txtHours;

        public OpeningHoursHolder(@NonNull View itemView) {
            super(itemView);
            txtWeekday = (TextView) itemView.findViewById(R.id.txt_weekday);
            txtHours = (TextView) itemView.findViewById(R.id.txt_hours);
        }
    }
}
