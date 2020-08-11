package com.findmysalon.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.findmysalon.R;
import com.findmysalon.model.Staff;
import com.findmysalon.view.adapters.StaffAdapter;
import com.findmysalon.view.adapters.StaffBookingAdapter;

import java.util.ArrayList;

public class StaffBookingFragment extends Fragment {

    TextView txtNameStaff;
    RatingBar rtbStaff;

    RecyclerView recStaff;
    ArrayList<Staff> list;
    StaffBookingAdapter staffAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_staff_booking, container, false);
        //((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        txtNameStaff = view.findViewById(R.id.txt_name_staff);
        rtbStaff = view.findViewById(R.id.rtb_staff);

        recStaff = view.findViewById(R.id.rec_staff_booking);
        recStaff.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        list = new ArrayList<Staff>();

        Staff staff1 = new Staff("Juan", 4.5f);
        Staff staff2 = new Staff("Simon", 1.5f);
        Staff staff3 = new Staff("Abhishek", 2.5f);


        list.add(staff1);
        list.add(staff2);
        list.add(staff3);

        staffAdapter = new StaffBookingAdapter(getActivity(), list);
        recStaff.setAdapter(staffAdapter);

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();


    }




}
