package com.findmysalon.view.fragments;

import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import com.findmysalon.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class RatingFragment extends Fragment {

    CardView btnSubmit, btnCancel;
    BottomSheetDialog dialogSuccess;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rate_service, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        btnSubmit = view.findViewById(R.id.btn_submit);
        btnCancel = view.findViewById(R.id.btn_cancel);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUp(v);
            }
        });

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    public void showPopUp(View v) {
        dialogSuccess = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);

        View viewBottomSheet = LayoutInflater.from(getContext())
                .inflate(
                        R.layout.popup_success,
                        (LinearLayout) v.findViewById(R.id.bottomSheetContainer)
                );

        CardView btnFinish = (CardView) viewBottomSheet.findViewById(R.id.btn_finish);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ImageView imgDone = viewBottomSheet.findViewById(R.id.img_done);

                Drawable drawable = imgDone.getDrawable();

                if (drawable instanceof AnimatedVectorDrawableCompat){
                    AnimatedVectorDrawableCompat avdc = (AnimatedVectorDrawableCompat) drawable;
                    avdc.start();
                } else if (drawable instanceof AnimatedVectorDrawable){
                    AnimatedVectorDrawable avd = (AnimatedVectorDrawable) drawable;
                    avd.start();
                }
            }
        }, 1000);



        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), "Success", Toast.LENGTH_LONG).show();
                //Navigation.findNavController(v).popBackStack();
                dialogSuccess.dismiss();
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_list_appointment_history);
            }
        });

        dialogSuccess.setContentView(viewBottomSheet);
        dialogSuccess.show();

    }

}
