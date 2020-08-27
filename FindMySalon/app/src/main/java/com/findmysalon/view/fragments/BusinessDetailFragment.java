package com.findmysalon.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.findmysalon.R;

public class BusinessDetailFragment extends Fragment {

    CardView btnBook;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_business_detail, container, false);
        //((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        btnBook = view.findViewById(R.id.btn_book);

        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("id", 1); // !!!!! please business id here !!!!!
                Navigation.findNavController(v).navigate(R.id.nav_list_services_by_category, bundle);
            }
        });

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();


    }




}
