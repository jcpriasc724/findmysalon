package com.findmysalon.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.findmysalon.R;
import com.findmysalon.view.fragments.RegisterCustomerFragment;
import com.google.android.material.navigation.NavigationView;

import java.util.UUID;

public class CustomerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;
    private int userId;
    private Uri profilePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_customer);
        NavigationView navigationView = findViewById(R.id.nav_view_customer);
        navigationView.setNavigationItemSelectedListener(this);


        // Accessing intent passed from LoginActivity
        Bundle extras = getIntent().getExtras();

        if (extras != null)
        {
            userId = (int) extras.get("user_id");
            String pPhoto = (String) extras.get("profile_photo");
            // Parsing URL of profile photo in string to Uri type
            profilePhoto = Uri.parse(pPhoto);
        }

        // Accessing the navigation header
        View header = navigationView.getHeaderView(0);
        ImageView imgView = header.findViewById(R.id.img_profile_photo);
        // Plugin to display profile photo
        Glide.with(getApplicationContext())
                .load(profilePhoto)
                .circleCrop()
                .placeholder(R.drawable.photos_default)
                .into(imgView);
        // Accessing the profile edit button
        ImageButton editButton = header.findViewById(R.id.btn_edit);
        editButton.setOnClickListener(v -> {
            Intent intent = new Intent(CustomerActivity.this, CustomerProfileActivity.class);
            intent.putExtra(RegisterCustomerFragment.EXTRAS_USER_ID, userId);
            startActivity(intent);
        });

//        View header = navigationView.getHeaderView(0);
//        ImageButton editButton = header.findViewById(R.id.btn_edit);
//
//        editButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(CustomerActivity.this, "Hello", Toast.LENGTH_LONG).show();
//            }
//        });


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                Navigation.findNavController(CustomerActivity.this, R.id.nav_host_fragment).navigate(R.id.nav_type_business);
                break;
            case R.id.nav_bookings:
                Navigation.findNavController(CustomerActivity.this, R.id.nav_host_fragment).navigate(R.id.nav_list_customer_bookings);
                break;
            case R.id.nav_favorites:
                Navigation.findNavController(CustomerActivity.this, R.id.nav_host_fragment).navigate(R.id.nav_list_fav_business);
                break;
<<<<<<< HEAD
=======
            case R.id.nav_update_profile:
                Navigation.findNavController(CustomerActivity.this, R.id.nav_host_fragment).navigate(R.id.nav_update_customer);
                break;
            case R.id.nav_change_password:
                Navigation.findNavController(CustomerActivity.this, R.id.nav_host_fragment).navigate(R.id.nav_change_password_customer);
                break;


>>>>>>> 62b77e3a685a394b01018056fc5323440f2a121f
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
}