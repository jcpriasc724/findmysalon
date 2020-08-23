package com.findmysalon.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.findmysalon.R;
import com.findmysalon.model.Staff;
import com.findmysalon.model.StaffRoster;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class RosterAdapter extends RecyclerView.Adapter<RosterAdapter.RosterHolder> {

    Context context;
    ArrayList<StaffRoster> list;

    public RosterAdapter(Context context, ArrayList<StaffRoster> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RosterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RosterHolder(LayoutInflater.from(context).inflate(R.layout.item_roster, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RosterHolder holder, int position) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(context.getResources().getString(R.string.pattern));
        String dateRoster = simpleDateFormat.format(list.get(position).getDateRoster());

        holder.swtDate.setText(dateRoster);

        holder.swtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.swtDate.isChecked()){
                    holder.containerDay.setVisibility(View.VISIBLE);
                } else {
                    holder.containerDay.setVisibility(View.GONE);
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class RosterHolder extends RecyclerView.ViewHolder {

        Switch swtDate;
        LinearLayout containerDay;
        Spinner sprHourStartDay;
        Spinner sprMinStartDay;
        Spinner sprHourEndDay;
        Spinner sprMinEndDay;


        public RosterHolder(@NonNull View itemView) {
            super(itemView);

            swtDate = itemView.findViewById(R.id.swt_date);
            containerDay = itemView.findViewById(R.id.container_day);
            sprHourStartDay = itemView.findViewById(R.id.spr_hour_start_day);
            sprMinStartDay = itemView.findViewById(R.id.spr_min_start_day);
            sprHourEndDay = itemView.findViewById(R.id.spr_hour_end_day);
            sprMinEndDay = itemView.findViewById(R.id.spr_min_end_day);
        }
    }
}
