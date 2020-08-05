package com.findmysalon.view.fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.findmysalon.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignupFragment extends Fragment {

    private RadioGroup rg;
    private EditText emailEditText;
    private EditText phoneEditText;
    private EditText shopNameEditText;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText passwordEditText;
    private EditText rePasswordEditText;
    private TextView shopNameTextView;
    private TextView firstNameTextView;
    private TextView lastNameTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_signup, container, false);
        // final TextView textView = root.findViewById(R.id.text_home);
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();


        return root;
    }

    @Override
    public void onStart(){
        super.onStart();

        rg = (RadioGroup) getActivity().findViewById(R.id.rolesRadio);

        emailEditText = (EditText) getActivity().findViewById(R.id.emailEditText);
        phoneEditText = (EditText) getActivity().findViewById(R.id.phoneEditText);

        shopNameEditText = (EditText) getActivity().findViewById(R.id.shopNameEditText);
        firstNameEditText = (EditText) getActivity().findViewById(R.id.firstNameEditText);
        lastNameEditText = (EditText) getActivity().findViewById(R.id.lastNameEditText);

        passwordEditText = (EditText) getActivity().findViewById(R.id.passwordEditText);
        rePasswordEditText = (EditText) getActivity().findViewById(R.id.rePasswordEditText);

        // text view
        shopNameTextView = (TextView) getActivity().findViewById(R.id.shopNameTextView);
        firstNameTextView = (TextView) getActivity().findViewById(R.id.firstNameTextView);
        lastNameTextView = (TextView) getActivity().findViewById(R.id.lastNameTextView);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected

                switch(checkedId) {
                    case R.id.rb_Customer:
                        // switch to fragment 1
                        firstNameTextView.setVisibility(View.VISIBLE);
                        lastNameTextView.setVisibility(View.VISIBLE);
                        firstNameEditText.setVisibility(View.VISIBLE);
                        lastNameEditText.setVisibility(View.VISIBLE);

                        shopNameTextView.setVisibility(View.GONE);
                        shopNameEditText.setVisibility(View.GONE);
                        break;
                    case R.id.rb_Business:
                        // Fragment 2
                        firstNameTextView.setVisibility(View.GONE);
                        lastNameTextView.setVisibility(View.GONE);
                        firstNameEditText.setVisibility(View.GONE);
                        lastNameEditText.setVisibility(View.GONE);
                        shopNameTextView.setVisibility(View.VISIBLE);
                        shopNameEditText.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
    }
}