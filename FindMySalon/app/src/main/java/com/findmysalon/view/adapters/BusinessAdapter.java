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
import com.findmysalon.api.FavouriteBusinessApi;
import com.findmysalon.model.Business;
import com.findmysalon.model.BusinessProfile;
import com.findmysalon.model.FavouriteBusinessProfile;
import com.findmysalon.model.Staff;
import com.findmysalon.utils.RetrofitClient;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BusinessAdapter extends RecyclerView.Adapter<BusinessAdapter.BusinessHolder> {

    private Context context;
    private ArrayList<BusinessProfile> list;
    private BusinessProfile currentBusiness;
    private FavouriteBusinessApi favouriteBusinessApi;
    private boolean isFavourite;
    private FavouriteBusinessProfile favouriteBusinessProfile;

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

                // retrofit
                Retrofit retrofit = RetrofitClient.getInstance(holder.itemView.getContext());
                favouriteBusinessApi = retrofit.create(FavouriteBusinessApi.class);

                Call<FavouriteBusinessProfile> call = favouriteBusinessApi.isFavouriteBusiness(currentBusiness.getBusinessId());
                call.enqueue(new Callback<FavouriteBusinessProfile>() {
                    @Override
                    public void onResponse(Call<FavouriteBusinessProfile> call, Response<FavouriteBusinessProfile> response) {
                        //
                        if(response.code() == 200){
                            isFavourite = true;
                        }
                        else{
                            isFavourite = false;
                        }
                        Log.d("Is Favourite : ", ""+isFavourite);
                        favouriteBusinessProfile = new FavouriteBusinessProfile(
                                currentBusiness.getBusinessId(),
                                currentBusiness.getBusinessName(),
                                currentBusiness.getAddress(),
                                currentBusiness.getPhone(),
                                currentBusiness.getRating(),
                                currentBusiness.getProfilePhoto(),
                                isFavourite);
                        bundle.putSerializable("business", favouriteBusinessProfile);
                        Navigation.findNavController(v).navigate(R.id.nav_business_detail, bundle);

                    }

                    @Override
                    public void onFailure(Call<FavouriteBusinessProfile> call, Throwable t) {
                        Log.d("Fail: ", t.getMessage());
                        Toast.makeText(holder.itemView.getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
                // retrofit End

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
