package com.findmysalon.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.findmysalon.R;

public class TypeUserFragment  extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_type_user, container, false);
        //((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

        CardView cardBusiness = (CardView) getActivity().findViewById(R.id.business_card);
        CardView cardCustomer = (CardView) getActivity().findViewById(R.id.customer_card);

        cardBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.nav_reg_business);

            }
        });

        cardCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.nav_reg_customer);
            }
        });
    }
}
