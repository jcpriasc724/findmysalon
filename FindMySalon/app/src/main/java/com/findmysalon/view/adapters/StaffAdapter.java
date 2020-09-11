package com.findmysalon.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.findmysalon.R;
import com.findmysalon.model.Staff;


import java.util.ArrayList;


public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.StaffHolder>{

    Context context;
    ArrayList<Staff> list;

    public StaffAdapter(Context context, ArrayList<Staff> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public StaffHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StaffHolder(LayoutInflater.from(context).inflate(R.layout.item_staff, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StaffHolder holder, int position) {

        holder.txtNameStaff.setText(list.get(position).getName());
        holder.txtCategory.setText("Service type: " + list.get(position).getCategory().getNameCategory());
        holder.txtPhoneNumber.setText("Contact: " + list.get(position).getPhoneNumber());

        if(list.get(position).getImage() != null) {
            Glide.with(holder.imgAvatar.getContext())
                    .load(list.get(position).getImage())
                    .circleCrop()
                    .placeholder(R.drawable.add_photo)
                    .into(holder.imgAvatar);
        }else {
            Glide.with(holder.imgAvatar.getContext()).clear(holder.imgAvatar);
        }
        // edit clicking
        //holder.btnEdit.setClickable(true);
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("id", list.get(position).getId());
                Navigation.findNavController(v).navigate(R.id.nav_add_staff,bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class StaffHolder extends RecyclerView.ViewHolder {

        TextView txtNameStaff, txtCategory, txtPhoneNumber;
        //ImageButton btnEdit, btnDelete;
        LinearLayout container;
        ImageView imgAvatar;

        public StaffHolder(@NonNull View itemView) {
            super(itemView);
            txtNameStaff = (TextView) itemView.findViewById(R.id.txt_name_staff);
            txtCategory = (TextView) itemView.findViewById(R.id.txt_category);
            txtPhoneNumber = (TextView) itemView.findViewById(R.id.txt_phone_number);
            container = itemView.findViewById(R.id.container_staff);
            //btnDelete = itemView.findViewById(R.id.btn_delete);
            imgAvatar = (ImageView) itemView.findViewById(R.id.img_profile_photo);
        }
    }
}
