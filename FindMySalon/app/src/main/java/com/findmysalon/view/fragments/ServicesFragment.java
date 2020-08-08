package com.findmysalon.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.findmysalon.R;
import com.findmysalon.model.Category;
import com.findmysalon.model.Service;
import com.findmysalon.view.adapters.ServiceAdapter;

import java.util.ArrayList;

public class ServicesFragment extends Fragment {

    TextView txtNameService, txtCategory, txtPrice, txtDuration, txtDescription;

    RecyclerView recServices;
    ArrayList<Service> servicesList;
    ServiceAdapter serviceAdapter;

    Button btnNext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_services, container, false);
        //((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        txtNameService = view.findViewById(R.id.txt_name_service);
        txtCategory = view.findViewById(R.id.txt_category);
        txtPrice = view.findViewById(R.id.txt_price);
        txtDuration = view.findViewById(R.id.txt_duration);
        txtDescription = view.findViewById(R.id.txt_description);

        recServices = view.findViewById(R.id.rec_services);
        recServices.setLayoutManager(new LinearLayoutManager(getActivity()));

        btnNext = view.findViewById(R.id.btn_next);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.nav_reg_business_staff);
            }
        });

        servicesList = new ArrayList<Service>();

        Category category = new Category("Basicos", "","");

        Service service1 = new Service("Basic hair cut", category, 20d, 20l, "The hair cut is beautiful");
        Service service2 = new Service("Hair cut with style", category, 30d, 30l, "The hair cut is beautiful");
        Service service3 = new Service("Hair cut and Beard", category, 50d, 50l, "The hair cut is beautiful");

        servicesList.add(service1);
        servicesList.add(service2);
        servicesList.add(service3);

        serviceAdapter = new ServiceAdapter(getActivity(), servicesList);
        recServices.setAdapter(serviceAdapter);

        return view;
    }
}
