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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.findmysalon.R;
import com.findmysalon.api.BusinessApi;
import com.findmysalon.api.ServiceApi;
import com.findmysalon.model.Business;
import com.findmysalon.model.BusinessProfile;
import com.findmysalon.model.Category;
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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Field;

public class ListBusinessFragment extends Fragment {

    private static final String[] LOCATION_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,};
    private static final int REQUEST_LOCATION_PERMISSIONS = 0;

    private TextView txtNameBusiness, txtAddress, txtPhoneNumber;
    private EditText txtKeyword;
    private RatingBar rtbBusiness;
    private RecyclerView recBusiness;
    private ArrayList<BusinessProfile> businessList;
    private BusinessAdapter businessAdapter;
    private int distanceFilter = 5; // Initial distance
    private int budgetFilter = 10; //  Initial budget
    private ArrayList<TypeBusiness> typeBusinessesFilter;
    private BottomSheetDialog dialogFilters;
    private CardView btnFilters;
    private LinearLayout tpAll;
    private TextView txtFilterAll;
    private SeekBar skbDistance;
    private TextView txtDistance;
    private SeekBar skbBudget;
    private TextView txtBudget;

    private BusinessApi businessApi;
    private ServiceApi serviceApi;

    private ArrayList<Category> servicesCategoryList;
    private ArrayList<Language> languageList;
    private ArrayAdapter<String> dataAdapter;
    private ArrayAdapter<String> languageAdapter;
    private Spinner categorySpinner;
    private Spinner languageSpinner;
    private String businessType;
    private double latitude;
    private double longitude;
    private int distance;
    private int budget;
    private String language;
    private int categoryId = 0;
    private int categorySpinnerVal = 0;
    private int languageSpinnerVal = 0;
    private String keyword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get default coordinate saved in DB, if user didn't allow location permission
        // Replace code below with values from DB
        latitude = -37.77156;
        longitude = 144.9687;
        distance = 20;
        requestPermissions(LOCATION_PERMISSIONS, REQUEST_LOCATION_PERMISSIONS);
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
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) {
                Log.e("TextWatcherTest", "afterTextChanged:\t" +s.toString());
                loadBusinessList();
            }
        });
        btnFilters = view.findViewById(R.id.btn_filters);
        txtNameBusiness = view.findViewById(R.id.txt_name_business);
        txtAddress = view.findViewById(R.id.txt_address);
        txtPhoneNumber = view.findViewById(R.id.txt_phone_number);

        recBusiness = view.findViewById(R.id.rec_business);
        recBusiness.setLayoutManager(new LinearLayoutManager(getActivity()));

        btnFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUp(v);
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
        LocationServices.getFusedLocationProviderClient(getActivity())
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(getActivity())
                                .removeLocationUpdates(this);
                        if(locationResult != null && locationResult.getLocations().size() > 0){
                            int latestLocatioIndex = locationResult.getLocations().size() - 1;
                            latitude = locationResult.getLocations().get(latestLocatioIndex).getLatitude();
                            longitude = locationResult.getLocations().get(latestLocatioIndex).getLongitude();
                            Log.d("Lat lng:",latitude+""+longitude);

                            loadBusinessList();
                        }
                    }
                }, Looper.getMainLooper());
    }

    private void loadBusinessList() {
        businessList = new ArrayList<>();

        /*Business business1 = new Business("Business 1", "Salon", "business@gmail.com", "1234", "1234567890", "Address Number 1", 43.98888, -23.09888, "B");
        Business business2 = new Business("Business 1234", "Barbershop", "business@gmail.com", "1234", "1234567890", "Address Number 1", 43.98888, -23.09888, "B");
        Business business3 = new Business("Business 1888", "Salon", "business@gmail.com", "1234", "1234567890", "Address Number 1", 43.98888, -23.09888, "B");

        list.add(business1);
        list.add(business2);
        list.add(business3);*/

        businessAdapter = new BusinessAdapter(getActivity(), businessList);
        recBusiness.setAdapter(businessAdapter);

        // retrofit
        Retrofit retrofit = RetrofitClient.getInstance(getActivity());
        businessApi = retrofit.create(BusinessApi.class);

        keyword = txtKeyword.getText().toString();
        distance = distanceFilter;
        budget = budgetFilter;

        Log.d("Lat lng ", " "+ latitude+ longitude);
        Call<ArrayList<BusinessProfile>> call = businessApi.businessList(businessType, latitude, longitude, distance, budget, language, categoryId, keyword);
        call.enqueue(new Callback<ArrayList<BusinessProfile>>() {
            @Override
            public void onResponse(Call<ArrayList<BusinessProfile>> call, Response<ArrayList<BusinessProfile>> response) {
                if(response.code() == 200){
                    businessList.addAll(response.body());
                    businessAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<ArrayList<BusinessProfile>> call, Throwable t) {
                Log.d("Fail: ", t.getMessage());
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        // retrofit End
    }

    private void setupFullHeight(BottomSheetDialog bottomSheetDialog) {
        FrameLayout bottomSheet = (FrameLayout) bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();

        int windowHeight = getWindowHeight();
        if (layoutParams != null) {
            layoutParams.height = windowHeight;
        }
        bottomSheet.setLayoutParams(layoutParams);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public void showPopUp(View v) {

        dialogFilters = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);
        dialogFilters.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialog;
                setupFullHeight(bottomSheetDialog);
            }
        });

        View viewBottomSheet = LayoutInflater.from(getContext())
                .inflate(
                        R.layout.popup_filters,
                        (LinearLayout) v.findViewById(R.id.bottomSheetContainer)
                );


        tpAll = viewBottomSheet.findViewById(R.id.tp_all);
        LinearLayout tpBarbershop = viewBottomSheet.findViewById(R.id.tp_barbershop);
        LinearLayout tpHairSalon = viewBottomSheet.findViewById(R.id.tp_hair_salon);

        ImageView imgFilterBarbershop = viewBottomSheet.findViewById(R.id.img_filter_barbershop);
        ImageView imgFilterHairSalon = viewBottomSheet.findViewById(R.id.img_filter_hair_salon);

        txtFilterAll = viewBottomSheet.findViewById(R.id.txt_filter_all);
        TextView txtFilterBarbershop = viewBottomSheet.findViewById(R.id.txt_filter_barbershop);
        TextView txtFilterHairSalon = viewBottomSheet.findViewById(R.id.txt_filter_hair_salon);

        skbDistance = viewBottomSheet.findViewById(R.id.skb_distance);
        txtDistance = viewBottomSheet.findViewById(R.id.txt_distance);

        skbDistance.setProgress(distanceFilter);
        txtDistance.setText(distanceFilter+" Km");

        skbDistance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int progressValue;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressValue = progress;
                txtDistance.setText(progressValue+" Km");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                txtDistance.setText(progressValue+" Km");
                distanceFilter = progressValue;

                Log.d("Final SeekBar",""+distanceFilter);
            }
        });

        skbBudget = viewBottomSheet.findViewById(R.id.skb_budget);
        txtBudget = viewBottomSheet.findViewById(R.id.txt_budget);

        //txtBudget.setText("Max $ "+skbBudget.getProgress());
        skbBudget.setProgress(budgetFilter);
        txtBudget.setText("Max $ "+budgetFilter);

        skbBudget.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressValue;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressValue = progress;
                txtBudget.setText("Max $ "+progressValue);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                txtBudget.setText("Max $ "+progressValue);
                budgetFilter = progressValue;

            }
        });

        for (TypeBusiness typeBusiness : typeBusinessesFilter) {
            if (typeBusiness.isSelected()) {
                if (typeBusiness.getTypeBusinessID().equals("A")) {
                    tpAll.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_tb_circle_black));
                    txtFilterAll.setTextColor(ContextCompat.getColor(getContext(), R.color.colorSecondary));
                    break;
                } else if (typeBusiness.getTypeBusinessID().equals("B")) {
                    tpBarbershop.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_tb_circle_black));
                    txtFilterBarbershop.setTextColor(ContextCompat.getColor(getContext(), R.color.colorSecondary));
                    imgFilterBarbershop.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorSecondary));
                    break;
                } else if (typeBusiness.getTypeBusinessID().equals("H")) {
                    tpHairSalon.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_tb_circle_black));
                    txtFilterHairSalon.setTextColor(ContextCompat.getColor(getContext(), R.color.colorSecondary));
                    imgFilterHairSalon.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorSecondary));
                    break;
                }
            }
        }

        tpAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (TypeBusiness typeBusiness : typeBusinessesFilter) {
                    if (typeBusiness.getTypeBusinessID().equals("A")) {
                        typeBusiness.setSelected(true);
                        businessType = "A";
                        tpAll.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_tb_circle_black));
                        txtFilterAll.setTextColor(ContextCompat.getColor(getContext(), R.color.colorSecondary));


                    }else {
                        typeBusiness.setSelected(false);

                        tpBarbershop.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_tb_circle_gray));
                        txtFilterBarbershop.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                        imgFilterBarbershop.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimary));

                        tpHairSalon.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_tb_circle_gray));
                        txtFilterHairSalon.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                        imgFilterHairSalon.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                    }
                }
            }
        });

        tpBarbershop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (TypeBusiness typeBusiness : typeBusinessesFilter) {
                    if (typeBusiness.getTypeBusinessID().equals("B")) {
                        typeBusiness.setSelected(true);
                        businessType = "B";
                        tpBarbershop.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_tb_circle_black));
                        txtFilterBarbershop.setTextColor(ContextCompat.getColor(getContext(), R.color.colorSecondary));
                        imgFilterBarbershop.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorSecondary));
                    }else {
                        typeBusiness.setSelected(false);

                        tpAll.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_tb_circle_gray));
                        txtFilterAll.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

                        tpHairSalon.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_tb_circle_gray));
                        txtFilterHairSalon.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                        imgFilterHairSalon.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                    }
                }
            }
        });

        tpHairSalon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (TypeBusiness typeBusiness : typeBusinessesFilter) {
                    if (typeBusiness.getTypeBusinessID().equals("H")) {
                        typeBusiness.setSelected(true);
                        businessType = "S";
                        tpHairSalon.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_tb_circle_black));
                        txtFilterHairSalon.setTextColor(ContextCompat.getColor(getContext(), R.color.colorSecondary));
                        imgFilterHairSalon.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorSecondary));
                    }else {
                        typeBusiness.setSelected(false);

                        tpAll.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_tb_circle_gray));
                        txtFilterAll.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

                        tpBarbershop.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_tb_circle_gray));
                        txtFilterBarbershop.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                        imgFilterBarbershop.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimary));


                    }
                }
            }
        });

        languageSpinner = (Spinner) viewBottomSheet.findViewById(R.id.spr_language);
        // set up language spinner
        languageSpinnerSetting();

        categorySpinner = (Spinner) viewBottomSheet.findViewById(R.id.spr_categories);
        // set up category spinner
        categorySpinnerSetting();

        // Reset filter button
        CardView btnReset = (CardView) viewBottomSheet.findViewById(R.id.btn_reset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call to reset filter
                resetPopUpFilter(viewBottomSheet);
                // Dismiss the filter
                dialogFilters.dismiss();
            }
        });

        // Apply filter button
        CardView btnConfirm = (CardView) viewBottomSheet.findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Load business list by making HTTP request to server
                loadBusinessList();
                // Dismiss the filter
                dialogFilters.dismiss();
            }
        });

        viewBottomSheet.findViewById(R.id.img_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFilters.dismiss();
            }
        });

        dialogFilters.setContentView(viewBottomSheet);
        dialogFilters.show();

    }

    /**
     *  language Spinner Set up
     */
    private void languageSpinnerSetting() {
        List<String> items = new ArrayList<String>();
        items.add("Choose a language");
        items.add("English");
        items.add("Spanish");
        items.add("Nepali");
        items.add("Chinese");
        items.add("Japanese");

        languageAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        languageAdapter.notifyDataSetChanged();
        languageSpinner.setAdapter(languageAdapter);
        languageSpinner.setSelection(languageSpinnerVal);

        // listener
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //String info = (String) categorySpinner.getSelectedItem();
                //categoryId = languageList.get(position).getId();
                languageSpinnerVal = position;
                language = languageSpinner.getSelectedItem().toString();
                //Toast.makeText(getActivity(), String.valueOf(categoryId), Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     *  category Spinner Set up
     */
    private void categorySpinnerSetting(){
        // fetch services category list
        servicesCategoryList = new ArrayList<Category>();
        // retrofit
        Retrofit retrofit = RetrofitClient.getInstance(getActivity());
        serviceApi = retrofit.create(ServiceApi.class);

        Call<ArrayList<Category>> call = serviceApi.serviceCategoryList();
        call.enqueue(new Callback<ArrayList<Category>>() {
            @Override
            public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {
                if(response.isSuccessful()){
                    servicesCategoryList.addAll(response.body());
                    List<String> items = new ArrayList<String>();
                    //items.add("Choose a category");
                    for(int i = 0; i < servicesCategoryList.size(); i++){
                        items.add(servicesCategoryList.get(i).getNameCategory());
                    }
                    dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
                    dataAdapter.notifyDataSetChanged();
                    categorySpinner.setAdapter(dataAdapter);
                    categorySpinner.setSelection(categorySpinnerVal);

                    // listener
                    categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            //String info = (String) categorySpinner.getSelectedItem();
                            categoryId = servicesCategoryList.get(position).getId();
                            categorySpinnerVal = position;
                            //Toast.makeText(getActivity(), String.valueOf(categoryId), Toast.LENGTH_LONG).show();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Category>> call, Throwable t) {
                Log.d("Fail: ", t.getMessage());
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        // retrofit End
    }

    /**
     *  Reset filters of popup
     */

    private void resetPopUpFilter(View viewBottomSheet) {

        // Reset business type to all
        for (TypeBusiness typeBusiness : typeBusinessesFilter) {
            if (typeBusiness.getTypeBusinessID().equals("A")) {
                typeBusiness.setSelected(true);
                businessType = "A";
                tpAll.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_tb_circle_black));
                txtFilterAll.setTextColor(ContextCompat.getColor(getContext(), R.color.colorSecondary));
            }
        }

        // Reset distance to 5km
        distanceFilter = 5;
        txtDistance.setText(distanceFilter+" Km");
        skbDistance.setProgress(distanceFilter);
        // Reset budget to $10
        budgetFilter = 10;
        skbBudget.setProgress(budgetFilter);
        txtBudget.setText("Max $ "+budgetFilter);
        // Reset category to first choice
        categorySpinnerVal = 0;
        categoryId = 0;
        categorySpinner.setSelection(categorySpinnerVal);

        // Reset language to first choice
        languageSpinnerVal = 0;
        language = "";
        languageSpinner.setSelection(languageSpinnerVal);

        // Load business after filter reset
        loadBusinessList();
    }
}
