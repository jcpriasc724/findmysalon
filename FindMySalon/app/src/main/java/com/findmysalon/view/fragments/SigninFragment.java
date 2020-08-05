package com.findmysalon.view.fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.findmysalon.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SigninFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SigninFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_signin, container, false);
        // final TextView textView = root.findViewById(R.id.text_home);
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();

        return root;
    }

    @Override
    public void onStart(){
        super.onStart();
        Button btn1 = (Button) getView().findViewById(R.id.btn_signIn);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // after login, do not go back to login page
                Navigation.findNavController(v).popBackStack();
                Navigation.findNavController(v).popBackStack();
                //
                Navigation.findNavController(v).navigate(R.id.nav_home);
            }
        });
    }
}