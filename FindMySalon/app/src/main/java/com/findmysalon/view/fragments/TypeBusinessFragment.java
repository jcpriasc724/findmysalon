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

public class TypeBusinessFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_type_business, container, false);
        //((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

        CardView cardSalon = (CardView) getActivity().findViewById(R.id.salon_card);
        CardView cardBarbershop = (CardView) getActivity().findViewById(R.id.barbershop_card);

        cardSalon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Navigation.findNavController(v).navigate(R.id.nav_reg_business);

            }
        });

        cardBarbershop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Navigation.findNavController(v).navigate(R.id.nav_reg_customer);
            }
        });
    }
}
