package com.findmysalon.view.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.findmysalon.R;
import com.findmysalon.model.Staff;

import java.util.ArrayList;

public class StaffRosterAdapter extends RecyclerView.Adapter<StaffRosterAdapter.StaffHolder>{

    Context context;
    ArrayList<Staff> list;

    public StaffRosterAdapter(Context context, ArrayList<Staff> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public StaffHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StaffHolder(LayoutInflater.from(context).inflate(R.layout.item_staff_booking2, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StaffHolder holder, int position) {

        holder.txtFirstNameStaff.setText(list.get(position).getName());
//        holder.rtbStaff.setRating(list.get(position).getRating());

        //holder.rtbStaff.setVisibility(View.GONE);
        if(list.get(position).getImage() != null) {
            holder.staffProfile.setImageTintMode(null);
            Glide.with(holder.staffProfile.getContext())
                    .load(list.get(position).getImage())
                    .circleCrop()
                    .placeholder(R.drawable.add_photo)
                    .into(holder.staffProfile);
        }else {
            Glide.with(holder.staffProfile.getContext()).clear(holder.staffProfile);
        }

        holder.containerStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("id", list.get(position).getId());
                bundle.putString("name", list.get(position).getName());
                Navigation.findNavController(v).navigate(R.id.nav_set_roster, bundle);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class StaffHolder extends RecyclerView.ViewHolder {

        TextView txtFirstNameStaff;
        TextView txtLastNameStaff;
        //RatingBar rtbStaff;
        LinearLayout containerStaff;
        ImageView staffProfile;

        public StaffHolder(@NonNull View itemView) {
            super(itemView);
            txtFirstNameStaff = (TextView) itemView.findViewById(R.id.txt_first_name_staff);
            //txtLastNameStaff = (TextView) itemView.findViewById(R.id.txt_last_name_staff);
            //rtbStaff = (RatingBar) itemView.findViewById(R.id.rtb_staff);
            containerStaff = itemView.findViewById(R.id.container_staff);
            staffProfile = (ImageView) itemView.findViewById(R.id.img_staff);
        }
    }
}
