package com.findmysalon.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.findmysalon.R;
import com.findmysalon.api.UserApi;
import com.findmysalon.model.BusinessProfile;
import com.findmysalon.model.CustomerProfile;
import com.findmysalon.utils.RetrofitClient;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BusinessActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;
    private Uri profilePhoto;
    private UserApi userApi;
    private ImageView imgView;
    private TextView txtBusinessName;
    private int businessId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_business);
        NavigationView navigationView = findViewById(R.id.nav_view_business);
        navigationView.setNavigationItemSelectedListener(this);

        // Accessing the navigation header
        View header = navigationView.getHeaderView(0);
        imgView = header.findViewById(R.id.img_profile_photo);
        txtBusinessName = header.findViewById(R.id.txt_name_user);

        // retrofit
        Retrofit retrofit = RetrofitClient.getInstance(getApplicationContext());
        userApi = retrofit.create(UserApi.class);
        Call<BusinessProfile> call = userApi.getBusinessDetails();
        call.enqueue(new Callback<BusinessProfile>() {
            @Override
            public void onResponse(Call<BusinessProfile> call, Response<BusinessProfile> response) {
                if(response.isSuccessful()){
                    profilePhoto = Uri.parse(response.body().getProfilePhoto());
                    // Plugin to display profile photo
                    Glide.with(getApplicationContext())
                            .load(profilePhoto)
                            .circleCrop()
                            .placeholder(R.drawable.add_photo)
                            .into(imgView);
                    txtBusinessName.setText(response.body().getBusinessName());
                }
            }

            @Override
            public void onFailure(Call<BusinessProfile> call, Throwable t) {
                Log.d("Fail: ", t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        /*String id;
        Bundle bundle1 = getIntent().getExtras();
        if (bundle1 != null) {
            id = bundle1.getString("id");
            Log.d("Budiness id", id);
        }
        Log.d("Budiness id", "null");*/
        businessId = 1;
        switch (menuItem.getItemId()) {
            case R.id.nav_update_profile:
                Navigation.findNavController(BusinessActivity.this, R.id.nav_business_host_fragment).navigate(R.id.nav_update_business);
                break;
            case R.id.nav_change_password:
                Navigation.findNavController(BusinessActivity.this, R.id.nav_business_host_fragment).navigate(R.id.nav_change_password_user);
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
            case R.id.nav_roster_staff:
                Navigation.findNavController(BusinessActivity.this, R.id.nav_business_host_fragment).navigate(R.id.nav_roster_staff);
                break;
            case R.id.nav_business_hour:
                /*Bundle bundle = new Bundle();
                bundle.putInt("id", businessId);*/
                Navigation.findNavController(BusinessActivity.this, R.id.nav_business_host_fragment).navigate(R.id.nav_business_hour);
                break;
            case R.id.nav_log_out:
                Navigation.findNavController(BusinessActivity.this, R.id.nav_business_host_fragment).navigate(R.id.nav_logout_user);
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