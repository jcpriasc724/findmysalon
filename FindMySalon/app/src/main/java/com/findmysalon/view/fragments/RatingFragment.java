package com.findmysalon.view.fragments;

import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import com.bumptech.glide.Glide;
import com.findmysalon.R;
import com.findmysalon.api.FavouriteBusinessApi;
import com.findmysalon.api.RatingApi;
import com.findmysalon.model.Business;
import com.findmysalon.model.Service;
import com.findmysalon.utils.RetrofitClient;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RatingFragment extends Fragment {

    CardView btnSubmit, btnCancel;
    BottomSheetDialog dialogSuccess;
    TextView txtBusinessName;
    ImageView imgProfilePhoto, imgClose;
    Business business;
    String businessNamePrompt = "How was %s?";
    RatingBar ratingBar;
    RatingApi ratingApi;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rate_service, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        btnSubmit = view.findViewById(R.id.btn_submit);
        btnCancel = view.findViewById(R.id.btn_cancel);
        imgClose = view.findViewById(R.id.img_close);
        txtBusinessName = view.findViewById(R.id.txt_business_name);
        imgProfilePhoto = view.findViewById(R.id.img_profile_photo);
        business = (Business) getArguments().getSerializable("business");
        ratingBar = view.findViewById(R.id.rtb_business);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.i("rating", String.valueOf(ratingBar.getRating()));
                submitRating(v);
//
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).popBackStack();
            }
        });

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).popBackStack();
            }
        });

        fetchBusinessDetail();
        return view;
    }

    private void submitRating(View v){
        if(ratingBar.getRating() <= 0){
            Toast.makeText(getActivity(), R.string.incorrect_rating, Toast.LENGTH_LONG).show();
            return;
        }
        // retrofit
        Retrofit retrofit = RetrofitClient.getInstance(getActivity());
        ratingApi = retrofit.create(RatingApi.class);
        Call<ResponseBody> call = ratingApi.submitRating(business.getId(),ratingBar.getRating() );
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    showPopUp(v);
                }
                else{
                    Toast.makeText(getActivity(), "Could not rate now", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Fail: ", t.getMessage());
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        // retrofit End
    }

    private void fetchBusinessDetail(){
        txtBusinessName.setText(String.format(businessNamePrompt, business.getStoreName()));
        Glide.with(this)
                .load(business.getUser().getProfilePhoto())
                .circleCrop()
                .placeholder(R.drawable.add_photo)
                .into(imgProfilePhoto);
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
