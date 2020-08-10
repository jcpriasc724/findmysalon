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
import com.findmysalon.model.Staff;
import com.findmysalon.view.adapters.BusinessAdapter;
import com.findmysalon.view.adapters.StaffAdapter;

import java.util.ArrayList;

public class ListBusinessFragment extends Fragment {

    TextView txtNameBusiness, txtAddress, txtPhoneNumber;
    RatingBar rtbBusiness;

    RecyclerView recBusiness;
    ArrayList<Business> list;
    BusinessAdapter businessAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_business, container, false);
        //((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        txtNameBusiness = view.findViewById(R.id.txt_name_business);
        txtAddress = view.findViewById(R.id.txt_address);
        txtPhoneNumber = view.findViewById(R.id.txt_phone_number);

        recBusiness = view.findViewById(R.id.rec_business);
        recBusiness.setLayoutManager(new LinearLayoutManager(getActivity()));

        list = new ArrayList<Business>();

        /*Business business1 = new Business("Business 1", "092830498", "Address Number 1", 4.5f);
        Business business2 = new Business("Business 2", "092830498", "Address Number 2", 3.5f);
        Business business3 = new Business("Business 3", "092830498", "Address Number 3", 1.5f);


        list.add(business1);
        list.add(business2);
        list.add(business3);*/

        businessAdapter = new BusinessAdapter(getActivity(), list);
        recBusiness.setAdapter(businessAdapter);

        return view;
    }
}
