package com.findmysalon.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.findmysalon.R;
import com.findmysalon.model.Category;
import com.findmysalon.model.Service;
import com.findmysalon.model.Staff;
import com.findmysalon.view.adapters.ServiceAdapter;
import com.findmysalon.view.adapters.StaffAdapter;

import java.util.ArrayList;

public class StaffFragment extends Fragment {

    TextView txtNameStaff, txtEmail, txtPhoneNumber;

    RecyclerView recStaff;
    ArrayList<Staff> list;
    StaffAdapter staffAdapter;

    Button btnNext;
    Button btnAddStaff;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_staff, container, false);
        //((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        txtNameStaff = view.findViewById(R.id.txt_name_staff);
        txtEmail = view.findViewById(R.id.txt_email);
        txtPhoneNumber = view.findViewById(R.id.txt_phone_number);

        recStaff = view.findViewById(R.id.rec_staff);
        recStaff.setLayoutManager(new LinearLayoutManager(getActivity()));

        btnNext = view.findViewById(R.id.btn_next);
        btnAddStaff = view.findViewById(R.id.btn_add_staff);


        btnAddStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.nav_add_staff);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Navigation.findNavController(v).navigate(R.id.nav_reg_business_staff);
            }
        });

        list = new ArrayList<Staff>();

        Staff staff1 = new Staff("Juan Camilo Puente","0413460105","camilo479@gmail.com");
        Staff staff2 = new Staff("Simon","0413460105","simon@gmail.com");
        Staff staff3 = new Staff("Abhishek","0413460105","abhishek@gmail.com");


        list.add(staff1);
        list.add(staff2);
        list.add(staff3);

        staffAdapter = new StaffAdapter(getActivity(), list);
        recStaff.setAdapter(staffAdapter);

        return view;
    }
}
