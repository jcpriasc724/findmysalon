package com.findmysalon.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.findmysalon.R;
import com.findmysalon.model.Business;
import com.findmysalon.model.BusinessProfile;
import com.findmysalon.model.Staff;

import java.util.ArrayList;

public class BusinessAdapter extends RecyclerView.Adapter<BusinessAdapter.BusinessHolder> {

    Context context;
    ArrayList<BusinessProfile> list;
    BusinessProfile currentBusiness;

    public BusinessAdapter(Context context, ArrayList<BusinessProfile> list) {
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
        holder.txtPhoneNumber.setText(list.get(position).getPhone());
        holder.rtbBusiness.setRating(list.get(position).getRating());
        // Plugin to display image
        if(list.get(position).getProfilePhoto() != null) {
            Glide.with(holder.imgAvatar.getContext())
                    .load(list.get(position).getProfilePhoto())
                    .circleCrop()
                    .placeholder(R.drawable.add_photo)
                    .into(holder.imgAvatar);
        }else {
            Glide.with(holder.imgAvatar.getContext()).clear(holder.imgAvatar);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                currentBusiness = list.get(position);
                bundle.putSerializable("business", currentBusiness);
                Navigation.findNavController(v).navigate(R.id.nav_business_detail, bundle);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class BusinessHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtNameBusiness, txtAddress, txtPhoneNumber;
        RatingBar rtbBusiness;
        ImageView imgAvatar;

        public BusinessHolder(@NonNull View itemView) {
            super(itemView);
            txtNameBusiness = (TextView) itemView.findViewById(R.id.txt_name_business);
            txtAddress = (TextView) itemView.findViewById(R.id.txt_address);
            txtPhoneNumber = (TextView) itemView.findViewById(R.id.txt_phone_number);
            rtbBusiness = (RatingBar) itemView.findViewById(R.id.rtb_business);
            imgAvatar = (ImageView) itemView.findViewById(R.id.img_profile_photo);

        }

        @Override
        public void onClick(View v) {
            Log.d("Business clicked","");
            /*Intent intent = new Intent(getActivity(), VenueDetailsActivity.class);
            intent.putExtra(VenueDetailsFragment.EXTRAS_VENUE_ID, mVenue.getId());

            startActivity(intent);*/
        }
    }
}
