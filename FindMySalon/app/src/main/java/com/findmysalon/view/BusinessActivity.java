package com.findmysalon.view;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.findmysalon.R;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class BusinessActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_business);
        NavigationView navigationView = findViewById(R.id.nav_view_business);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.nav_account_settings:
                //Navigation.findNavController(BusinessActivity.this, R.id.nav_business_host_fragment).navigate(R.id.nav_list_bookings);
                break;
            case R.id.nav_bookings:
                Navigation.findNavController(BusinessActivity.this, R.id.nav_business_host_fragment).navigate(R.id.nav_list_bookings);
                break;
            case R.id.nav_staff:
                Navigation.findNavController(BusinessActivity.this, R.id.nav_business_host_fragment).navigate(R.id.nav_staff_list_business);
                break;
            case R.id.nav_services:
                Navigation.findNavController(BusinessActivity.this, R.id.nav_business_host_fragment).navigate(R.id.nav_service_list_services);
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Fetch Fragments that belong to Activity
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments == null)
        {
            return;
        }
        // search Fragment onRequestPermissionsResult method
        for (Fragment fragment : fragments)
        {
            if (fragment != null)
            {
                // class Fragment onRequestPermissionsResult method
                fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }
}