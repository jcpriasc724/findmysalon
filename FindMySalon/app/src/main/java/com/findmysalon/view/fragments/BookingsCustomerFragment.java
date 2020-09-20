package com.findmysalon.view.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.findmysalon.R;
import com.findmysalon.api.AppointmentApi;
import com.findmysalon.model.Booking;
import com.findmysalon.model.Business;
import com.findmysalon.model.Customer;
import com.findmysalon.model.Service;
import com.findmysalon.model.Staff;
import com.findmysalon.utils.RetrofitClient;
import com.findmysalon.view.adapters.BookingAdapter;
import com.findmysalon.view.adapters.BookingCustomerAdapter;

import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class BookingsCustomerFragment extends Fragment {

    RecyclerView recBookings;
    ArrayList<Booking> list;
    BookingCustomerAdapter bookingAdapter;
    AppointmentApi appointmentApi;
    TextView txtNoServices;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_bookings, container, false);

        recBookings = view.findViewById(R.id.rec_bookings);
        recBookings.setLayoutManager(new LinearLayoutManager(getActivity()));
        txtNoServices = view.findViewById(R.id.txt_no_bookings);
//        Business business1 = new Business("149 Sydney Rd, Brunswick VIC 3056", null, "Barber Black Sheep");
//        Business business2 = new Business("141 Sydney Rd, Brunswick VIC 3056", null, "Lola Fortune");
//
//        Service service1 = new Service("Basic haircut");
//        Service service2 = new Service("Stylized cut");
//
//        Staff staff1 = new Staff("Simon", null, null);
//        Staff staff2 = new Staff("Abhishek", null, null);
//
//        Booking booking1 = new Booking(new Date(), "10:00 am", "10:30 am", null, service1, staff1, business1);
//        Booking booking2 = new Booking(new Date(), "1:00 pm", "1:55 pm", null, service2, staff2, business2);

        list = new ArrayList<Booking>();
//        list.add(booking1);
//        list.add(booking2);

        bookingAdapter = new BookingCustomerAdapter(getActivity(), list);
        recBookings.setAdapter(bookingAdapter);
        // retrofit
        Retrofit retrofit = RetrofitClient.getInstance(getActivity());
        appointmentApi = retrofit.create(AppointmentApi.class);
        Call<ArrayList<Booking>> call = appointmentApi.appointmentList(1);
        call.enqueue(new Callback<ArrayList<Booking>>() {
            @Override
            public void onResponse(Call<ArrayList<Booking>> call, Response<ArrayList<Booking>> response) {
                if(response.isSuccessful()){
                    list.addAll(response.body());
//                    Log.i("appointment_LIT", new Date().toString());
                    bookingAdapter.notifyDataSetChanged();
                    if(list.size() > 0){
                        txtNoServices.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Booking>> call, Throwable t) {
                Log.d("Fail: ", t.getMessage());
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        // retrofit End
        return view;
    }
}
