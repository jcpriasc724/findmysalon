package com.findmysalon.view.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.findmysalon.R;
import com.findmysalon.api.AppointmentApi;
import com.findmysalon.model.Service;
import com.findmysalon.model.Staff;
import com.findmysalon.model.StaffAvailable;
import com.findmysalon.utils.RetrofitClient;
import com.findmysalon.view.CustomerActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AppiontmentConfirmationFragment extends Fragment {

    CardView btnConfirm;
    Service service;
    Staff staff;
    Date selectedDate;
    String selectedTime;
    TextView txtNameService, txtNameStaff, txtDateBooking, txtTimeBooking;
    AppointmentApi appointmentApi;
    SimpleDateFormat simpleDateFormat;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment_confirmation, container, false);
        //((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        simpleDateFormat = new SimpleDateFormat(getContext().getResources().getString(R.string.pattern));

        txtNameService = (TextView) view.findViewById(R.id.txt_name_service);
        txtNameStaff = (TextView) view.findViewById(R.id.txt_name_staff);
        txtDateBooking = (TextView) view.findViewById(R.id.txt_date_booking);
        txtTimeBooking = (TextView) view.findViewById(R.id.txt_time_booking);

        service         = (Service) getArguments().getSerializable("service");
        staff           = (Staff) getArguments().getSerializable("staff");
        selectedDate    = (Date) getArguments().getSerializable("selectedDate");
        selectedTime    =  getArguments().getString("selectedTime");

        txtNameService.setText(service.getNameService());
        txtNameStaff.setText(staff.getName());
        txtDateBooking.setText(simpleDateFormat.format(selectedDate));
        txtTimeBooking.setText(selectedTime);


        btnConfirm = view.findViewById(R.id.btn_confirm);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Navigation.findNavController(v).navigate(R.id.nav_type_business);
                simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                // retrofit
                Retrofit retrofit = RetrofitClient.getInstance(getActivity());
                appointmentApi = retrofit.create(AppointmentApi.class);
                Call<ResponseBody> call = appointmentApi.submitAppointment(
                        service.getId(),
                        service.getBusiness().getId(),
                        staff.getId(),
                        simpleDateFormat.format(selectedDate),
                        selectedTime
                );
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(getActivity(), R.string.submit_success, Toast.LENGTH_LONG).show();
                            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_list_customer_bookings);
                        } else {

                            try {
                                Toast.makeText(getContext(), response.errorBody().string(), Toast.LENGTH_LONG).show();
                            } catch (IOException e) {
                                // handle failure to read error
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.d("Fail: ", t.getMessage());
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
                // retrofit End
            }
        });

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();


    }




}
