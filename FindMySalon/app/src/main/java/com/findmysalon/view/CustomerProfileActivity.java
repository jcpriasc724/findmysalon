package com.findmysalon.view;

import androidx.fragment.app.Fragment;

import com.findmysalon.view.fragments.RegisterCustomerFragment;

public class CustomerProfileActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new RegisterCustomerFragment();
    }
}
