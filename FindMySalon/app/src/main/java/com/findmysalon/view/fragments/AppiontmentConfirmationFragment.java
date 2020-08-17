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

public class AppiontmentConfirmationFragment extends Fragment {

    //Button btnSave;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment_confirmation, container, false);
        //((AppCompatActivity)getActivity()).getSupportActionBar().hide();

//        btnSave = view.findViewById(R.id.btn_save);
//
//        btnSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Navigation.findNavController(v).navigate(R.id.nav_reg_business_staff);
//            }
//        });

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();


    }




}
