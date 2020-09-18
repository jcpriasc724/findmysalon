package com.findmysalon.view.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.findmysalon.R;
import com.findmysalon.api.BusinessApi;
import com.findmysalon.api.ServiceApi;
import com.findmysalon.api.StaffApi;
import com.findmysalon.model.Business;
import com.findmysalon.model.BusinessProfile;
import com.findmysalon.model.Category;
import com.findmysalon.model.CustomerProfile;
import com.findmysalon.model.FavouriteBusinessProfile;
import com.findmysalon.model.FilterParameters;
import com.findmysalon.model.Language;
import com.findmysalon.model.Service;
import com.findmysalon.model.Staff;
import com.findmysalon.model.TypeBusiness;
import com.findmysalon.utils.RetrofitClient;
import com.findmysalon.view.adapters.BusinessAdapter;
import com.findmysalon.view.adapters.StaffAdapter;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.findmysalon.utils.abcConstants.INITIAL_DISTANCE;
import static com.findmysalon.utils.abcConstants.INITIAL_MAX_BUDGET;

public class ListBusinessFragment extends Fragment {

    private static final String[] LOCATION_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,};
    private static final int REQUEST_LOCATION_PERMISSIONS = 0;

    private EditText txtKeyword;
    private RecyclerView recBusiness;
    private ArrayList<BusinessProfile> businessList;
    private BusinessAdapter businessAdapter;
    private ArrayList<TypeBusiness> typeBusinessesFilter;
    private CardView btnFilters;


    private BusinessApi businessApi;
    private StaffApi staffApi;

    private double latitude;
    private double longitude;
    private int distance;
    private int budget;
    private int languageId;
    private int categoryId = 0;
    private String keyword;
    private FilterParameters filterParameters;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermissions(LOCATION_PERMISSIONS, REQUEST_LOCATION_PERMISSIONS);
    }

    @Override
    public void onResume() {
        super.onResume();

        //Simon here you have to receive the bundle with all filters and call the service to apply the filters

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_business, container, false);
        //((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        typeBusinessesFilter = new ArrayList<>();
        typeBusinessesFilter.add(new TypeBusiness("A", true));
        typeBusinessesFilter.add(new TypeBusiness("B", false));
        typeBusinessesFilter.add(new TypeBusiness("H", false));

        txtKeyword = view.findViewById(R.id.txt_keyword);
        txtKeyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e("TextWatcherTest", "afterTextChanged:\t" + s.toString());
                //loadBusinessList();
            }
        });
        btnFilters = view.findViewById(R.id.btn_filters);

        recBusiness = view.findViewById(R.id.rec_business);
        recBusiness.setLayoutManager(new LinearLayoutManager(getActivity()));

        btnFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_list_business_to_nav_filters);
                //showPopUp(v);
            }
        });

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSIONS:
                if (hasLocationPermission()) {
                    getCurrentLocation();
                }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    // Check if app has permission to access user's location
    private boolean hasLocationPermission() {
        int result = ContextCompat
                .checkSelfPermission(getActivity(), LOCATION_PERMISSIONS[0]);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void getCurrentLocation() {

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        distance = R.integer.max_distance;
        budget = R.integer.max_budget;

        LocationServices.getFusedLocationProviderClient(getActivity())
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(getActivity())
                                .removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int latestLocatioIndex = locationResult.getLocations().size() - 1;
                            latitude = locationResult.getLocations().get(latestLocatioIndex).getLatitude();
                            longitude = locationResult.getLocations().get(latestLocatioIndex).getLongitude();
                            Log.d("Lat lng:", latitude + "" + longitude);

                            loadBusinessList(null, latitude, longitude, distance, budget, 0, 0, "");
                        }
                    }
                }, Looper.getMainLooper());
    }

    private void loadBusinessList(String businessTypeSelected, double lat, double lng, int distanceSelected, int budgetSelected, int languageSelected, int categoryIdSelected, String keywordTyped) {

        businessList = new ArrayList<>();
        businessAdapter = new BusinessAdapter(getActivity(), businessList);
        recBusiness.setAdapter(businessAdapter);

        // retrofit
        Retrofit retrofit = RetrofitClient.getInstance(getActivity());
        businessApi = retrofit.create(BusinessApi.class);

        Log.d("Lat lng ", " " + latitude + longitude);
        Call<ArrayList<BusinessProfile>> call = businessApi.businessList(businessTypeSelected, lat, lng, distanceSelected, budgetSelected, languageSelected, categoryIdSelected, keywordTyped);
        call.enqueue(new Callback<ArrayList<BusinessProfile>>() {
            @Override
            public void onResponse(Call<ArrayList<BusinessProfile>> call, Response<ArrayList<BusinessProfile>> response) {
                if (response.code() == 200) {
                    businessList.addAll(response.body());
                    Log.i("SERVICE_LIST", businessList.toString());
                    businessAdapter.notifyDataSetChanged();

                    /*if(businessList.size() > 0){
                        txtNoServices.setVisibility(View.GONE);
                    }*/
                }
//
            }

            @Override
            public void onFailure(Call<ArrayList<BusinessProfile>> call, Throwable t) {
                Log.d("Fail: ", t.getMessage());
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        // retrofit End

    }


}
