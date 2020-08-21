package com.findmysalon.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.findmysalon.R;
import com.findmysalon.model.Business;
import com.findmysalon.view.adapters.BusinessAdapter;

import java.util.ArrayList;

public class ListFavBusinessFragment extends Fragment {

    TextView txtNameBusiness, txtAddress, txtPhoneNumber;
    RatingBar rtbBusiness;

    RecyclerView recBusiness;
    ArrayList<Business> list;
    BusinessAdapter businessAdapter;

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

        txtNameBusiness = view.findViewById(R.id.txt_name_business);
        txtAddress = view.findViewById(R.id.txt_address);
        txtPhoneNumber = view.findViewById(R.id.txt_phone_number);

        recBusiness = view.findViewById(R.id.rec_business);
        recBusiness.setLayoutManager(new LinearLayoutManager(getActivity()));

        list = new ArrayList<Business>();

        Business business1 = new Business("Business 1", "Salon", "business@gmail.com","1234","1234567890","Address Number 1", 43.98888,  -23.09888, "B");
        Business business2 = new Business("Business 1234", "Barbershop", "business@gmail.com","1234","1234567890","Address Number 1", 43.98888,  -23.09888, "B");
        Business business3 = new Business("Business 1888", "Salon", "business@gmail.com","1234","1234567890","Address Number 1", 43.98888,  -23.09888, "B");


        list.add(business1);
        list.add(business2);
        list.add(business3);

        businessAdapter = new BusinessAdapter(getActivity(), list);
        recBusiness.setAdapter(businessAdapter);

        return view;
    }
}
