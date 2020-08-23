package com.findmysalon.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.findmysalon.R;

public class SetRosterFragment extends Fragment {

    //Button btnConfirm;

    private Switch swtMonday;
    private Switch swtTuesday;
    private Switch swtWednesday;
    private Switch swtThursday;
    private Switch swtFriday;
    private Switch swtSaturday;
    private Switch swtSunday;

    private LinearLayout containerMonday;
    private LinearLayout containerTuesday;
    private LinearLayout containerWednesday;
    private LinearLayout containerThursday;
    private LinearLayout containerFriday;
    private LinearLayout containerSaturday;
    private LinearLayout containerSunday;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_roster, container, false);
        //((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        swtMonday = view.findViewById(R.id.swt_monday);
        swtTuesday = view.findViewById(R.id.swt_tuesday);
        swtWednesday = view.findViewById(R.id.swt_wednesday);
        swtThursday = view.findViewById(R.id.swt_thursday);
        swtFriday = view.findViewById(R.id.swt_friday);
        swtSaturday = view.findViewById(R.id.swt_saturday);
        swtSunday = view.findViewById(R.id.swt_sunday);
        containerMonday = view.findViewById(R.id.container_monday);
        containerTuesday = view.findViewById(R.id.container_tuesday);
        containerWednesday = view.findViewById(R.id.container_wednesday);
        containerThursday = view.findViewById(R.id.container_thursday);
        containerFriday = view.findViewById(R.id.container_friday);
        containerSaturday = view.findViewById(R.id.container_saturday);
        containerSunday = view.findViewById(R.id.container_sunday);

        swtMonday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (swtMonday.isChecked()){
                    containerMonday.setVisibility(View.VISIBLE);
                } else {
                    containerMonday.setVisibility(View.GONE);
                }

            }
        });

        swtTuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (swtTuesday.isChecked()){
                    containerTuesday.setVisibility(View.VISIBLE);
                } else {
                    containerTuesday.setVisibility(View.GONE);
                }

            }
        });

        swtWednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (swtWednesday.isChecked()){
                    containerWednesday.setVisibility(View.VISIBLE);
                } else {
                    containerWednesday.setVisibility(View.GONE);
                }

            }
        });


        swtThursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (swtThursday.isChecked()){
                    containerThursday.setVisibility(View.VISIBLE);
                } else {
                    containerThursday.setVisibility(View.GONE);
                }

            }
        });

        swtFriday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (swtFriday.isChecked()){
                    containerFriday.setVisibility(View.VISIBLE);
                } else {
                    containerFriday.setVisibility(View.GONE);
                }

            }
        });

        swtSaturday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (swtSaturday.isChecked()){
                    containerSaturday.setVisibility(View.VISIBLE);
                } else {
                    containerSaturday.setVisibility(View.GONE);
                }

            }
        });

        swtSunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (swtSunday.isChecked()){
                    containerSunday.setVisibility(View.VISIBLE);
                } else {
                    containerSunday.setVisibility(View.GONE);
                }

            }
        });

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();


    }




}
