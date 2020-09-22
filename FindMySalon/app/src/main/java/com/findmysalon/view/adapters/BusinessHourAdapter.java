package com.findmysalon.view.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.findmysalon.R;
import com.findmysalon.model.Business;
import com.findmysalon.model.BusinessHour;
import com.findmysalon.model.StaffRoster;

import java.util.ArrayList;

public class BusinessHourAdapter extends RecyclerView.Adapter<BusinessHourAdapter.BusinessHourHolder> {

    Context context;
    ArrayList<BusinessHour> list;

    public BusinessHourAdapter(Context context, ArrayList<BusinessHour> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public BusinessHourHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BusinessHourHolder(LayoutInflater.from(context).inflate(R.layout.item_business_hour, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BusinessHourHolder holder, int position) {

        // Set day text
        String day = list.get(position).getDay();
        holder.swtDate.setText(day);

        // if status == 1, set visibility
        if(list.get(position).getStatus() == 1){
            holder.containerDay.setVisibility(View.VISIBLE);
            holder.swtDate.setChecked(true);
            // set up Spinner
            holder.sprOpeningHour.setSelection(getIndex(holder.sprOpeningHour, list.get(position).getOpeningHour()));
            holder.sprOpeningMin.setSelection(getIndex(holder.sprOpeningMin, list.get(position).getOpeningMin()));
            holder.sprClosingHour.setSelection(getIndex(holder.sprClosingHour, list.get(position).getClosingHour()));
            holder.sprClosingMin.setSelection(getIndex(holder.sprClosingMin, list.get(position).getClosingMin()));
        }
        else{
            holder.containerDay.setVisibility(View.GONE);
        }

        holder.sprOpeningHour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int sprPosition, long id) {
                // your code here
                list.get(position).setOpeningHour(holder.sprOpeningHour.getSelectedItem().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        holder.sprOpeningMin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int sprPosition, long id) {
                // your code here
                list.get(position).setOpeningMin(holder.sprOpeningMin.getSelectedItem().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        holder.sprClosingHour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int sprPosition, long id) {
                // your code here
                list.get(position).setClosingHour(holder.sprClosingHour.getSelectedItem().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        holder.sprClosingMin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int sprPosition, long id) {
                // your code here
                list.get(position).setClosingMin(holder.sprClosingMin.getSelectedItem().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        holder.swtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.swtDate.isChecked()){
                    holder.containerDay.setVisibility(View.VISIBLE);
                    list.get(position).setStatus(1);
                } else {
                    holder.containerDay.setVisibility(View.GONE);
                    list.get(position).setStatus(0);
                }

            }
        });

    }

    private int getIndex(Spinner spinner, String myString) {
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class BusinessHourHolder extends RecyclerView.ViewHolder {

        Switch swtDate;
        LinearLayout containerDay;
        Spinner sprOpeningHour;
        Spinner sprOpeningMin;
        Spinner sprClosingHour;
        Spinner sprClosingMin;

        public BusinessHourHolder(@NonNull View itemView) {
            super(itemView);

            swtDate = itemView.findViewById(R.id.swt_date);
            containerDay = itemView.findViewById(R.id.container_day);
            sprOpeningHour = itemView.findViewById(R.id.spr_opening_hour);
            sprOpeningMin = itemView.findViewById(R.id.spr_opening_min);
            sprClosingHour = itemView.findViewById(R.id.spr_closing_hour);
            sprClosingMin = itemView.findViewById(R.id.spr_closing_min);
        }
    }
}
