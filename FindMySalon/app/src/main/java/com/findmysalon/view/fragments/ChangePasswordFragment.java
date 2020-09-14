package com.findmysalon.view.fragments;

import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import com.findmysalon.R;
import com.findmysalon.api.UserApi;
import com.findmysalon.model.CustomerProfile;
import com.findmysalon.utils.Helper;
import com.findmysalon.utils.RetrofitClient;
import com.findmysalon.view.BusinessActivity;
import com.findmysalon.view.CustomerActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChangePasswordFragment extends Fragment {
    private EditText mCurrentPassword;
    private EditText mNewPassword;
    private EditText mRePassword;
    BottomSheetDialog dialogSuccess;
    private View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        //((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        v = view;
        mCurrentPassword = (EditText) v.findViewById(R.id.etx_current_password);
        mNewPassword = (EditText) v.findViewById(R.id.etx_new_password);
        mRePassword = (EditText) v.findViewById(R.id.etx_rePassword);
        CardView mUpdateButton = v.findViewById(R.id.btn_update);
        mUpdateButton.setOnClickListener(v1 -> changePassword());
        return view;
    }

    public void showPopUp(View v, String userType) {
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

                if(userType.equals("B")){
                    Navigation.findNavController(getActivity(), R.id.nav_business_host_fragment).navigate(R.id.nav_list_bookings);
                }else if(userType.equals("C")){
                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_list_business);
                }



            }
        });

        dialogSuccess.setContentView(viewBottomSheet);
        dialogSuccess.show();

    }

    private void changePassword() {
        String currentPassword = mCurrentPassword.getText().toString();
        String newPassword = mNewPassword.getText().toString();
        String rePassword = mRePassword.getText().toString();

        if(currentPassword.isEmpty() && newPassword.isEmpty() && rePassword.isEmpty()){
            Helper.errorMsgDialog(getActivity(), R.string.incomplete);
        }
        else if(!newPassword.equals(rePassword)){
            Helper.errorMsgDialog(getActivity(), R.string.password_no_match);
        }
        else{
            Retrofit retrofit = RetrofitClient.getInstance(getActivity());
            UserApi userApi = retrofit.create(UserApi.class);
            Call<JsonObject> call =  userApi.changePassword(currentPassword, newPassword);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if(response.isSuccessful()){
                        Log.d("Response: ", ""+response.body());
                        // Convert String to json object
                        JsonObject resp = response.body();
                        String userType = resp.get("user_type").getAsString();

                        Intent intent;
                        // If user is business then redirect them to their corresponding dashboard
//                        if(userType.equals("B")){
////                            intent = new Intent(getActivity(), BusinessActivity.class);
////                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                            startActivity(intent);
//
//                        }
//                        // If user is customer then redirect them to their corresponding dashboard
//                        if(userType.equals("C")){
//                            intent = new Intent(getActivity(), CustomerActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivity(intent);
//                        }

                        showPopUp(v, userType);

                    }
                    else{
                        Helper.errorMsgDialog(getActivity(), R.string.incorrect_current_password);
                        Log.d("Error: ",""+response.message());
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Helper.errorMsgDialog(getActivity(), R.string.network_error);
                    Log.d("Fail: ", t.getMessage());
                }
            });
        }
    }


    @Override
    public void onStart() {
        super.onStart();


    }
}
