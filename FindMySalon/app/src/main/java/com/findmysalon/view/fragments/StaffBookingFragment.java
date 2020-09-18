package com.findmysalon.view.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.findmysalon.R;
import com.findmysalon.api.AppointmentApi;
import com.findmysalon.api.ServiceApi;
import com.findmysalon.model.Category;
import com.findmysalon.model.Service;
import com.findmysalon.model.Staff;
import com.findmysalon.model.StaffAvailable;
import com.findmysalon.model.StaffRoster;
import com.findmysalon.utils.RetrofitClient;
import com.findmysalon.view.adapters.StaffAdapter;
import com.findmysalon.view.adapters.StaffBookingAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class StaffBookingFragment extends Fragment {

    TextView txtNameStaff, txtNoStaff;
    //RatingBar rtbStaff;

    RecyclerView recStaff;
    ArrayList<StaffAvailable> list;
    StaffBookingAdapter staffAdapter;
    Service service;
    AppointmentApi appointmentApi;
    int curStaffId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_staff_booking, container, false);
        //((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        txtNameStaff = view.findViewById(R.id.txt_name_staff);
        txtNoStaff = view.findViewById(R.id.txt_no_staff);

        recStaff = view.findViewById(R.id.rec_staff_booking);
        recStaff.setLayoutManager(new LinearLayoutManager(getActivity()));

        service = (Service) getArguments().getSerializable("service");
//        recStaff.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        list = new ArrayList<StaffAvailable>();
//
//        ArrayList<String> listHours1 = new ArrayList<>();
//        listHours1.add("12:00 pm");
//        listHours1.add("1:00 pm");
//        listHours1.add("3:00 pm");
//        listHours1.add("4:00 pm");
//
//        StaffRoster staffRoster = new StaffRoster(new Date(), listHours1);
//        ArrayList<StaffRoster> listRoster = new ArrayList<StaffRoster> ();
//        listRoster.add(staffRoster);
//
//        Staff staff1 = new Staff("Juan", 4.5f, listRoster);
//
//        ArrayList<String> listHours2 = new ArrayList<>();
//        listHours2.add("10:00 am");
//        listHours2.add("11:00 am");
//
//        Date dt = new Date();
//        Calendar c = Calendar.getInstance();
//        c.setTime(dt);
//        c.add(Calendar.DATE, 1);
//        dt = c.getTime();
//
//        StaffRoster staffRoster2 = new StaffRoster(dt, listHours2);
//
//        ArrayList<StaffRoster> listRoster2 = new ArrayList<StaffRoster> ();
//        listRoster2.add(staffRoster);
//        listRoster2.add(staffRoster2);
//
//        Staff staff2 = new Staff("Simon", 1.5f, listRoster2);
//
//        ArrayList<StaffRoster> listRoster3 = new ArrayList<StaffRoster> ();
//        listRoster3.add(staffRoster);
//        listRoster3.add(staffRoster);
//        listRoster3.add(staffRoster);
//        listRoster3.add(staffRoster);
//
//        Staff staff3 = new Staff("Abhishek", 2.5f, listRoster3);
//
//
//        list.add(staff1);
//        list.add(staff2);
//        list.add(staff3);

        staffAdapter = new StaffBookingAdapter(getActivity(), list, service);
        recStaff.setAdapter(staffAdapter);


        // retrofit
        Retrofit retrofit = RetrofitClient.getInstance(getActivity());
        appointmentApi = retrofit.create(AppointmentApi.class);
        Call<ArrayList<StaffAvailable>> call =
                appointmentApi.availableTimeTable(service.getId(),service.getBusiness().getId());
        call.enqueue(new Callback<ArrayList<StaffAvailable>>() {
            @Override
            public void onResponse(Call<ArrayList<StaffAvailable>> call, Response<ArrayList<StaffAvailable>> response) {
                if(response.isSuccessful()){
                    list.addAll(response.body());
                    if(list.size() > 0){
                        txtNoStaff.setVisibility(View.GONE);
                    }
                    staffAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<StaffAvailable>> call, Throwable t) {
                Log.d("Fail: ", t.getMessage());
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        // retrofit End

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();


    }




}
