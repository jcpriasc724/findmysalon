package com.findmysalon.view.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.findmysalon.R;
import com.findmysalon.api.ServiceApi;
import com.findmysalon.model.Category;
import com.findmysalon.model.FilterParameters;
import com.findmysalon.model.Language;
import com.findmysalon.model.TypeBusiness;
import com.findmysalon.utils.RetrofitClient;
import com.findmysalon.view.adapters.LanguagesAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FiltersFragment extends Fragment {

    private CardView btnFilters;
    private LinearLayout tpAll;
    private LinearLayout tpBarbershop;
    private LinearLayout tpHairSalon;
    private TextView txtFilterAll;
    private TextView txtFilterBarbershop;
    private TextView txtFilterHairSalon;
    private ImageView imgFilterBarbershop;
    private ImageView imgFilterHairSalon;

    private SeekBar skbDistance;
    private TextView txtDistance;
    private SeekBar skbBudget;
    private TextView txtBudget;
    private int distanceFilter = 5; // Initial distance
    private int budgetFilter = 10; //  Initial budget
    private ArrayList<TypeBusiness> typeBusinessesFilter;
    private ServiceApi serviceApi;

    private ArrayList<Category> servicesCategoryList;
    private ArrayList<Language> languageList;
    private ArrayAdapter<String> dataAdapter;
    private ArrayAdapter<String> languageAdapter;
    private Spinner categorySpinner;
    //private Spinner languageSpinner;
    private String businessType;
    private double latitude;
    private double longitude;
    private int distance;
    private int budget;
    private String language;
    private int categoryId = 0;
    private int categorySpinnerVal = 0;
    private int languageSpinnerVal = 0;

    AutoCompleteTextView actvLanguages;
    ArrayList<String> languageListStrings;
    RecyclerView recLanguages;
    LanguagesAdapter languagesAdapter;

    private static final String[] LANGUAGES = new String[]{
            "English","Spanish", "Italian","Nepali","Chinese","Japanese"
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filters, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        languageListStrings = new ArrayList<>();
        typeBusinessesFilter = new ArrayList<>();
        typeBusinessesFilter.add(new TypeBusiness("A", true));
        typeBusinessesFilter.add(new TypeBusiness("B", false));
        typeBusinessesFilter.add(new TypeBusiness("H", false));

        tpAll = view.findViewById(R.id.tp_all);
        tpBarbershop = view.findViewById(R.id.tp_barbershop);
        tpHairSalon = view.findViewById(R.id.tp_hair_salon);

        imgFilterBarbershop = view.findViewById(R.id.img_filter_barbershop);
        imgFilterHairSalon = view.findViewById(R.id.img_filter_hair_salon);

        txtFilterAll = view.findViewById(R.id.txt_filter_all);
        txtFilterBarbershop = view.findViewById(R.id.txt_filter_barbershop);
        txtFilterHairSalon = view.findViewById(R.id.txt_filter_hair_salon);

        skbDistance = view.findViewById(R.id.skb_distance);
        txtDistance = view.findViewById(R.id.txt_distance);

        skbDistance.setProgress(distanceFilter);
        txtDistance.setText(distanceFilter+" Km");

        actvLanguages = view.findViewById(R.id.actv_languages);

        recLanguages = view.findViewById(R.id.rec_languages);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recLanguages.setLayoutManager(layoutManager);

        languagesAdapter = new LanguagesAdapter(getContext(), languageListStrings);
        recLanguages.setAdapter(languagesAdapter);

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

        skbBudget = view.findViewById(R.id.skb_budget);
        txtBudget = view.findViewById(R.id.txt_budget);

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

//        languageSpinner = (Spinner) view.findViewById(R.id.spr_language);
//        // set up language spinner
        languageSpinnerSetting();

        categorySpinner = (Spinner) view.findViewById(R.id.spr_categories);
        // set up category spinner
        categorySpinnerSetting();

        // Apply filter button
        CardView btnConfirm = (CardView) view.findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Load business list by making HTTP request to server juancamilo

                ArrayList<String> languages = new ArrayList<>();
                languages.add(language);

                FilterParameters filterParameters = new FilterParameters(businessType, 0, 0, distanceFilter, budgetFilter, languages, categoryId, "");

                Bundle bundle=new Bundle();
                //bundle.put

                ListBusinessFragment listBusinessFragment =new ListBusinessFragment();
                listBusinessFragment.setArguments(bundle);

                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.add(R.id.nav_host_fragment, listBusinessFragment);
                ft.addToBackStack(null);
                ft.commit();

            }
        });


        // Reset filter button
        CardView btnReset = (CardView) view.findViewById(R.id.btn_reset);

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call to reset filter
                resetPopUpFilter(v);
            }
        });

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    /**
     *  language Spinner Set up
     */
    private void languageSpinnerSetting() {
        List<String> items = new ArrayList<String>();
        items.add("English");
        items.add("Spanish");
        items.add("Nepali");
        items.add("Chinese");
        items.add("Japanese");

        languageAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, items);
        languageAdapter.notifyDataSetChanged();
        actvLanguages.setAdapter(languageAdapter);

        // listener
        actvLanguages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                languageListStrings.add(actvLanguages.getText().toString());
                languageAdapter.notifyDataSetChanged();
                actvLanguages.setText("");
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

    private void resetPopUpFilter(View view) {

        // Reset business type to all
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
        //languageSpinner.setSelection(languageSpinnerVal);

        // Load business after filter reset
        //loadBusinessList();
    }

}
