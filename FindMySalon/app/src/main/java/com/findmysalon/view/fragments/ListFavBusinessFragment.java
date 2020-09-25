package com.findmysalon.view.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.findmysalon.R;
import com.findmysalon.api.BusinessApi;
import com.findmysalon.api.FavouriteBusinessApi;
import com.findmysalon.model.Business;
import com.findmysalon.model.BusinessProfile;
import com.findmysalon.utils.RetrofitClient;
import com.findmysalon.view.adapters.BusinessAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ListFavBusinessFragment extends Fragment {

    TextView txtNameBusiness, txtAddress, txtPhoneNumber, txtNoBusinessLabel;
    RatingBar rtbBusiness;

    private RecyclerView recBusiness;
    private ArrayList<BusinessProfile> favBusinessList;
    private BusinessAdapter businessAdapter;
    private FavouriteBusinessApi favouriteBusinessApi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Fetch active access token to make authenticated API request
        /*TokenManager tokenManager = new TokenManager(getActivity());
        tokenManager.getAccessToken();*/
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_fav_business, container, false);
        //((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        txtNoBusinessLabel = view.findViewById(R.id.txt_no_business);

        txtNameBusiness = view.findViewById(R.id.txt_name_business);
        txtAddress = view.findViewById(R.id.txt_address);
        txtPhoneNumber = view.findViewById(R.id.txt_phone_number);

        recBusiness = view.findViewById(R.id.rec_business);
        recBusiness.setLayoutManager(new LinearLayoutManager(getActivity()));

        /*Business business1 = new Business("Business 1", "Salon", "business@gmail.com","1234","1234567890","Address Number 1", 43.98888,  -23.09888, "B");
        Business business2 = new Business("Business 1234", "Barbershop", "business@gmail.com","1234","1234567890","Address Number 1", 43.98888,  -23.09888, "B");
        Business business3 = new Business("Business 1888", "Salon", "business@gmail.com","1234","1234567890","Address Number 1", 43.98888,  -23.09888, "B");


        list.add(business1);
        list.add(business2);
        list.add(business3);*/

        loadFavBusinessList();

        return view;
    }

    private void loadFavBusinessList() {
        favBusinessList = new ArrayList<BusinessProfile>();

        businessAdapter = new BusinessAdapter(getActivity(), favBusinessList);
        recBusiness.setAdapter(businessAdapter);

        // retrofit
        Retrofit retrofit = RetrofitClient.getInstance(getActivity());
        favouriteBusinessApi = retrofit.create(FavouriteBusinessApi.class);

        Call<ArrayList<BusinessProfile>> call = favouriteBusinessApi.getFavouriteBusiness();
        call.enqueue(new Callback<ArrayList<BusinessProfile>>() {
            @Override
            public void onResponse(Call<ArrayList<BusinessProfile>> call, Response<ArrayList<BusinessProfile>> response) {
                if(response.isSuccessful()){
                    favBusinessList.addAll(response.body());
                    businessAdapter.notifyDataSetChanged();
                    // Hide label if favourite business listed
                    if(favBusinessList.size() > 0){
                        txtNoBusinessLabel.setVisibility(View.INVISIBLE);
                    }
                    else{
                        txtNoBusinessLabel.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<BusinessProfile>> call, Throwable t) {
                Log.d("Fail: ", t.getMessage());
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        // retrofit End
    }
}
