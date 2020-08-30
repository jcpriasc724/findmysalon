package com.findmysalon.view.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.findmysalon.R;
import com.findmysalon.model.Business;
import com.findmysalon.model.Staff;
import com.findmysalon.model.TypeBusiness;
import com.findmysalon.view.adapters.BusinessAdapter;
import com.findmysalon.view.adapters.StaffAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class ListBusinessFragment extends Fragment {

    TextView txtNameBusiness, txtAddress, txtPhoneNumber;
    RatingBar rtbBusiness;

    RecyclerView recBusiness;
    ArrayList<Business> list;
    BusinessAdapter businessAdapter;

    int distanceFilter;
    int budgetFilter;

    ArrayList<TypeBusiness> typeBusinessesFilter;

    BottomSheetDialog dialogFilters;

    CardView btnFilters;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Fetch active access token to make authenticated API request
        /*TokenManager tokenManager = new TokenManager(getActivity());
        tokenManager.getAccessToken();*/
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

        btnFilters = view.findViewById(R.id.btn_filters);
        txtNameBusiness = view.findViewById(R.id.txt_name_business);
        txtAddress = view.findViewById(R.id.txt_address);
        txtPhoneNumber = view.findViewById(R.id.txt_phone_number);

        recBusiness = view.findViewById(R.id.rec_business);
        recBusiness.setLayoutManager(new LinearLayoutManager(getActivity()));

        list = new ArrayList<Business>();

        Business business1 = new Business("Business 1", "Salon", "business@gmail.com", "1234", "1234567890", "Address Number 1", 43.98888, -23.09888, "B");
        Business business2 = new Business("Business 1234", "Barbershop", "business@gmail.com", "1234", "1234567890", "Address Number 1", 43.98888, -23.09888, "B");
        Business business3 = new Business("Business 1888", "Salon", "business@gmail.com", "1234", "1234567890", "Address Number 1", 43.98888, -23.09888, "B");


        list.add(business1);
        list.add(business2);
        list.add(business3);

        businessAdapter = new BusinessAdapter(getActivity(), list);
        recBusiness.setAdapter(businessAdapter);

        btnFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUp(v);
            }
        });

        return view;
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


        LinearLayout tpAll = viewBottomSheet.findViewById(R.id.tp_all);
        LinearLayout tpBarbershop = viewBottomSheet.findViewById(R.id.tp_barbershop);
        LinearLayout tpHairSalon = viewBottomSheet.findViewById(R.id.tp_hair_salon);

        ImageView imgFilterBarbershop = viewBottomSheet.findViewById(R.id.img_filter_barbershop);
        ImageView imgFilterHairSalon = viewBottomSheet.findViewById(R.id.img_filter_hair_salon);

        TextView txtFilterAll = viewBottomSheet.findViewById(R.id.txt_filter_all);
        TextView txtFilterBarbershop = viewBottomSheet.findViewById(R.id.txt_filter_barbershop);
        TextView txtFilterHairSalon = viewBottomSheet.findViewById(R.id.txt_filter_hair_salon);

        SeekBar skbDistance = viewBottomSheet.findViewById(R.id.skb_distance);
        TextView txtDistance = viewBottomSheet.findViewById(R.id.txt_distance);

        txtDistance.setText(skbDistance.getProgress()+" Km");

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
            }
        });

        SeekBar skbBudget = viewBottomSheet.findViewById(R.id.skb_budget);
        TextView txtBudget = viewBottomSheet.findViewById(R.id.txt_budget);

        txtBudget.setText("Max $ "+skbBudget.getProgress());

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


        viewBottomSheet.findViewById(R.id.img_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFilters.dismiss();
            }
        });

        dialogFilters.setContentView(viewBottomSheet);
        dialogFilters.show();

    }
}
