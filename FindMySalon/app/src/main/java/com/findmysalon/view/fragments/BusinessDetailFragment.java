package com.findmysalon.view.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.findmysalon.R;
import com.findmysalon.api.BusinessApi;
import com.findmysalon.api.BusinessHourApi;
import com.findmysalon.api.FavouriteBusinessApi;
import com.findmysalon.model.BusinessHour;
import com.findmysalon.model.BusinessProfile;
import com.findmysalon.model.FavouriteBusinessProfile;
import com.findmysalon.model.OpeningHoursItem;
import com.findmysalon.model.Service;
import com.findmysalon.utils.RetrofitClient;
import com.findmysalon.view.adapters.BusinessAdapter;
import com.findmysalon.view.adapters.OpeningHoursAdapter;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BusinessDetailFragment extends Fragment {

    private CardView btnBook;
    private FavouriteBusinessProfile business;
    private TextView txtNameBusiness, txtAddress, txtPhoneNumber;
    private RatingBar rtbBusiness;
    private ImageView imgAvatar;;
    private ImageButton btnFavBusiness;
    private FavouriteBusinessApi favouriteBusinessApi;
    private BusinessHourApi businessHourApi;
    private boolean isFavourite;
    private ArrayList<BusinessHour> openingHoursList;
    private OpeningHoursAdapter openingHoursAdapter;
    private RecyclerView recOpeningHours;
    private LinearLayout hoursContainer;
    private ImageView imgExpand;
    private TextView txtOpToday;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_business_detail, container, false);
        //((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        // Get bundle arguments sent from BusinessAdapter
        business = (FavouriteBusinessProfile) getArguments().getSerializable("business");
        //isFavourite = (boolean) getArguments().getSerializable("is_favourite");

        txtNameBusiness = (TextView) view.findViewById(R.id.txt_name_business);
        txtNameBusiness.setText(business.getBusinessName());

        txtAddress = (TextView) view.findViewById(R.id.txt_address);
        txtAddress.setText(business.getAddress());

        txtPhoneNumber = (TextView) view.findViewById(R.id.txt_phone_number);
        txtPhoneNumber.setText(business.getPhone());

        rtbBusiness = (RatingBar) view.findViewById(R.id.rtb_business);
        rtbBusiness.setRating(business.getRating());

        imgAvatar = (ImageView) view.findViewById(R.id.img_profile_photo);
        // Plugin to display image
        if(business.getProfilePhoto() != null) {
            Glide.with(imgAvatar.getContext())
                    .load(business.getProfilePhoto())
                    .circleCrop()
                    .placeholder(R.drawable.add_photo)
                    .into(imgAvatar);
        }else {
            Glide.with(imgAvatar.getContext()).clear(imgAvatar);
        }

        btnBook = view.findViewById(R.id.btn_book);
        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("id", business.getBusinessId()); // !!!!! please business id here !!!!!
                Navigation.findNavController(v).navigate(R.id.nav_list_services_by_category, bundle);
            }
        });

        // Make favourite business
        btnFavBusiness = (ImageButton) view.findViewById(R.id.btn_favourite);
        btnFavBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFavourite = business.isFavourite();
                Log.d("Working here"," bool status: "+isFavourite);
                // If the business is not set favourite then make it favourite on button click
                if(!isFavourite){
                    addFavouriteBusiness(business.getBusinessId());
                }
                // Remove it from favourite other
                else{
                    removeFavouriteBusiness(business.getBusinessId());
                }
            }
        });

        // Set the icon based on favourite
        if(business.isFavourite()){
            btnFavBusiness.setImageResource(android.R.drawable.btn_star_big_on);
        }
        else{
            btnFavBusiness.setImageResource(android.R.drawable.btn_star_big_off);
        }

        recOpeningHours = view.findViewById(R.id.rec_opening_hours);
        recOpeningHours.setLayoutManager(new LinearLayoutManager(getActivity()));
        recOpeningHours.setNestedScrollingEnabled(false);

        imgExpand = view.findViewById(R.id.img_expand);
        txtOpToday = view.findViewById(R.id.txt_op_today);

        //txtOpToday.setText("Friday 8:00 - 18:00");

        hoursContainer = view.findViewById(R.id.hours_container);
        hoursContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recOpeningHours.getVisibility()==View.GONE){
                    recOpeningHours.setVisibility(View.VISIBLE);
                    imgExpand.setImageResource(R.drawable.ic_expand_less);
                } else {
                    recOpeningHours.setVisibility(View.GONE);
                    imgExpand.setImageResource(R.drawable.ic_expand);
                }
            }
        });

        openingHoursList = new ArrayList<>();
        /*openingHoursList.add(new OpeningHoursItem("Saturday", "10:00 - 15:00"));
        openingHoursList.add(new OpeningHoursItem("Sunday", "10:00 - 15:00"));
        openingHoursList.add(new OpeningHoursItem("Monday", "10:00 - 18:00"));
        openingHoursList.add(new OpeningHoursItem("Tuesday", "10:00 - 18:00"));
        openingHoursList.add(new OpeningHoursItem("Wednesday", "10:00 - 18:00"));
        openingHoursList.add(new OpeningHoursItem("Thursday", "10:00 - 18:00"));*/

        openingHoursAdapter = new OpeningHoursAdapter(getActivity(), openingHoursList);
        recOpeningHours.setAdapter(openingHoursAdapter);

        // retrofit
        Retrofit retrofit = RetrofitClient.getInstance(getActivity());
        businessHourApi = retrofit.create(BusinessHourApi.class);
        Call<ArrayList<BusinessHour>> call = businessHourApi.businessHourList(business.getBusinessId());
        call.enqueue(new Callback<ArrayList<BusinessHour>>() {
            @Override
            public void onResponse(Call<ArrayList<BusinessHour>> call, Response<ArrayList<BusinessHour>> response) {
                if(response.code() == 200){
                    openingHoursList.addAll(response.body());
                    openingHoursAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<BusinessHour>> call, Throwable t) {
                Log.d("Fail: ", t.getMessage());
            }
        });
        // retrofit End

        return view;
    }

    private void addFavouriteBusiness(int businessId) {
        // retrofit
        Retrofit retrofit = RetrofitClient.getInstance(getActivity());
        favouriteBusinessApi = retrofit.create(FavouriteBusinessApi.class);
        Log.d("Business id",""+businessId);
        Call<ResponseBody> call = favouriteBusinessApi.addFavouriteBusiness(businessId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    business.setFavourite(true);
                    btnFavBusiness.setImageResource(android.R.drawable.btn_star_big_on);
                    Toast.makeText(getActivity(), "Successfully added in Favourite List", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getActivity(), "Could not add to Favourite List", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Fail: ", t.getMessage());
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        // retrofit End
    }

    private void removeFavouriteBusiness(int businessId) {
        // retrofit
        Retrofit retrofit = RetrofitClient.getInstance(getActivity());
        favouriteBusinessApi = retrofit.create(FavouriteBusinessApi.class);
        Log.d("Business id",""+businessId);
        Call<ResponseBody> call = favouriteBusinessApi.deleteFavouriteBusiness(businessId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    business.setFavourite(false);
                    btnFavBusiness.setImageResource(android.R.drawable.btn_star_big_off);
                    Toast.makeText(getActivity(), "Successfully removed from Favourite List", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getActivity(), "Could not remove from Favourite List", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Fail: ", t.getMessage());
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        // retrofit End
    }

    @Override
    public void onStart() {
        super.onStart();
    }

}
