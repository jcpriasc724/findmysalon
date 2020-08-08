package com.findmysalon.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.findmysalon.R;

public class RegisterCustomerFragment extends Fragment {

    Button btnNext;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_customer, container, false);
        //((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        btnNext = view.findViewById(R.id.btn_next);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Navigation.findNavController(v).navigate(R.id.nav_reg_business_services);
            }
        });

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();


    }




}
