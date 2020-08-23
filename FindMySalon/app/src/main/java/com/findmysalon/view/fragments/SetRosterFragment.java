package com.findmysalon.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.findmysalon.R;
import com.findmysalon.model.StaffRoster;
import com.findmysalon.view.adapters.RosterAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SetRosterFragment extends Fragment {

    RecyclerView recRoster;
    ArrayList<StaffRoster> list;
    RosterAdapter rosterAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_roster, container, false);
        //((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        recRoster = view.findViewById(R.id.rec_roster);
        recRoster.setLayoutManager(new LinearLayoutManager(getActivity()));

        list = new ArrayList<StaffRoster>();

        Calendar c = Calendar.getInstance();

        for (int i = 1; i <= 7; i++) {
            Date dt = new Date();
            c.setTime(dt);
            c.add(Calendar.DATE, i);
            dt = c.getTime();
            StaffRoster staffRoster = new StaffRoster(dt);
            list.add(staffRoster);
        }

        rosterAdapter = new RosterAdapter(getActivity(), list);
        recRoster.setAdapter(rosterAdapter);

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();


    }




}
