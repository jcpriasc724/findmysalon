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
import com.findmysalon.model.Booking;
import com.findmysalon.model.Customer;
import com.findmysalon.model.Service;
import com.findmysalon.model.Staff;
import com.findmysalon.view.adapters.BookingAdapter;

import java.util.ArrayList;
import java.util.Date;


public class BookingsFragment extends Fragment {

    RecyclerView recBookings;
    ArrayList<Booking> list;
    BookingAdapter bookingAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_bookings, container, false);

        recBookings = view.findViewById(R.id.rec_bookings);
        recBookings.setLayoutManager(new LinearLayoutManager(getActivity()));

        Customer customer1 = new Customer("Juan Camilo","Puente");
        Customer customer2 = new Customer("Lina","Hannanh");

        Service service1 = new Service("Basic haircut");
        Service service2 = new Service("Stylized cut");

        Staff staff1 = new Staff("Simon", null, null);
        Staff staff2 = new Staff("Abhishek", null, null);

        Booking booking1 = new Booking(new Date(), "10:00 am", "10:30 am", customer1, service1, staff1, null);
        Booking booking2 = new Booking(new Date(), "1:00 pm", "1:55 pm", customer2, service2, staff2, null);

        list = new ArrayList<Booking>();
        list.add(booking1);
        list.add(booking2);

        bookingAdapter = new BookingAdapter(getActivity(), list);
        recBookings.setAdapter(bookingAdapter);

        return view;
    }
}
