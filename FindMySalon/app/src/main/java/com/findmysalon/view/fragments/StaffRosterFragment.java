package com.findmysalon.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.findmysalon.R;
import com.findmysalon.model.Staff;
import com.findmysalon.model.StaffRoster;
import com.findmysalon.view.adapters.StaffBookingAdapter;
import com.findmysalon.view.adapters.StaffRosterAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class StaffRosterFragment extends Fragment {

    TextView txtNameStaff;
    RatingBar rtbStaff;

    RecyclerView recStaff;
    ArrayList<Staff> list;
    StaffRosterAdapter staffAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_staff_roster, container, false);
        //((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        txtNameStaff = view.findViewById(R.id.txt_name_staff);
        rtbStaff = view.findViewById(R.id.rtb_staff);

        recStaff = view.findViewById(R.id.rec_staff_roster);
//        recStaff.setLayoutManager(new LinearLayoutManager(getActivity()));
        recStaff.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        list = new ArrayList<Staff>();

        ArrayList<String> listHours1 = new ArrayList<>();
        listHours1.add("12:00 pm");
        listHours1.add("1:00 pm");
        listHours1.add("3:00 pm");
        listHours1.add("4:00 pm");

        StaffRoster staffRoster = new StaffRoster(new Date(), listHours1);
        ArrayList<StaffRoster> listRoster = new ArrayList<StaffRoster> ();
        listRoster.add(staffRoster);

        Staff staff1 = new Staff("Juan", 4.5f, listRoster);

        ArrayList<String> listHours2 = new ArrayList<>();
        listHours2.add("10:00 am");
        listHours2.add("11:00 am");

        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, 1);
        dt = c.getTime();

        StaffRoster staffRoster2 = new StaffRoster(dt, listHours2);

        ArrayList<StaffRoster> listRoster2 = new ArrayList<StaffRoster> ();
        listRoster2.add(staffRoster);
        listRoster2.add(staffRoster2);

        Staff staff2 = new Staff("Simon", 1.5f, listRoster2);

        ArrayList<StaffRoster> listRoster3 = new ArrayList<StaffRoster> ();
        listRoster3.add(staffRoster);
        listRoster3.add(staffRoster);
        listRoster3.add(staffRoster);
        listRoster3.add(staffRoster);

        Staff staff3 = new Staff("Abhishek", 2.5f, listRoster3);


        list.add(staff1);
        list.add(staff2);
        list.add(staff3);
        list.add(staff3);
        list.add(staff3);
        list.add(staff3);
        list.add(staff3);

        staffAdapter = new StaffRosterAdapter(getActivity(), list);
        recStaff.setAdapter(staffAdapter);

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();


    }




}
