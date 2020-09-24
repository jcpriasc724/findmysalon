package com.findmysalon.view.fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.findmysalon.view.adapters.BookingCustomerAdapter;

public class PageAdapterBookings extends FragmentStatePagerAdapter {

    private int behavior;

    public PageAdapterBookings(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.behavior = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 1 :
                BookingsCustomerFragment bookingsCustomerFragment = new BookingsCustomerFragment();
                return bookingsCustomerFragment;
            case 2 :
                AppointmentHistoryFragment appointmentHistoryFragment = new AppointmentHistoryFragment();
                return appointmentHistoryFragment;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return behavior;
    }
}
