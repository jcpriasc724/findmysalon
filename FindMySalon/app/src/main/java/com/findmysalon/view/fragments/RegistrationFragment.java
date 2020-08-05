package com.findmysalon.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.findmysalon.R;

public class RegistrationFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_registration, container, false);
//        final TextView textView = root.findViewById(R.id.text_home);
//        setHasOptionsMenu(true);
//        getActivity().setTitle("ww");
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        return root;

    }

    @Override
    public void onStart(){
        super.onStart();
        Button btn1 = (Button) getView().findViewById(R.id.btn_signUp);
        Button btn2 = (Button) getView().findViewById(R.id.btn_signIn);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.nav_signup);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.nav_signin);
            }
        });
    }
}
