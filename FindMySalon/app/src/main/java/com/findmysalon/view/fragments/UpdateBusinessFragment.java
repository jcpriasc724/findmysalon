package com.findmysalon.view.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.findmysalon.R;
import com.findmysalon.api.UserApi;
import com.findmysalon.helpers.PlaceApi;
import com.findmysalon.model.BusinessProfile;
import com.findmysalon.model.CustomerProfile;
import com.findmysalon.utils.Helper;
import com.findmysalon.utils.RetrofitClient;
import com.findmysalon.view.BusinessActivity;
import com.findmysalon.view.CustomerActivity;
import com.findmysalon.view.LoginActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;
import static com.findmysalon.utils.abcConstants.BASE_URL;

public class UpdateBusinessFragment extends Fragment {

    private EditText mBusinessName;
    private EditText mBusinessType;
    private RadioGroup mRadioGroup;

    private RadioButton mRadioButton;
    private AutoCompleteTextView mAddress;
    private EditText mEmail;
    private EditText mPhone;
    private HashMap<String, Object> addressApi;
    private String currentAddress = "";
    private double currentLatitude = 0.0;
    private double currentLongitude = 0.0;
    private UserApi userApi;
    private View v;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_update_business, container, false);

        // Address autocomplete suggestions
        PlaceApi placeApi = new PlaceApi();
        addressApi = placeApi.fetchAddressLatLng(v.findViewById(R.id.etx_address), getActivity());

        mBusinessName = (EditText) v.findViewById(R.id.etx_business_name);
        mRadioGroup = (RadioGroup) v.findViewById(R.id.rdg_type_business);

        mEmail = (EditText) v.findViewById(R.id.etx_email);
        mPhone = (EditText) v.findViewById(R.id.etx_phone);
        mAddress = (AutoCompleteTextView) v.findViewById(R.id.etx_address);
        CardView mUpdateButton = v.findViewById(R.id.btn_update);
        mUpdateButton.setOnClickListener(v1 -> businessUpdate());

        // retrofit
        Retrofit retrofit = RetrofitClient.getInstance(getActivity());
        userApi = retrofit.create(UserApi.class);
        Call<BusinessProfile> call = userApi.getBusinessDetails();
        call.enqueue(new Callback<BusinessProfile>() {
            @Override
            public void onResponse(Call<BusinessProfile> call, Response<BusinessProfile> response) {
                if(response.isSuccessful()){
                    mBusinessName.setText(response.body().getBusinessName());
                    //mBusinessType.setText(response.body().getBusinessType());
                    if(response.body().getBusinessType().equals("S")){
                        mRadioGroup.check(R.id.rb_hair_salon);
                    }
                    else{
                        mRadioGroup.check(R.id.rb_barbershop);
                    }

                    mEmail.setText(response.body().getEmail());
                    mAddress.setText(response.body().getAddress());
                    mPhone.setText(response.body().getPhone());
                    currentAddress = response.body().getAddress();
                    currentLatitude = response.body().getLatitude();
                    currentLongitude = response.body().getLongitude();
                }
            }

            @Override
            public void onFailure(Call<BusinessProfile> call, Throwable t) {
                Log.d("Fail: ", t.getMessage());
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        return v;
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    // Method to handle update for business
    public void businessUpdate(){
        String businessName = mBusinessName.getText().toString();
        // find the radiobutton by returned id
        mRadioButton = (RadioButton) v.findViewById(mRadioGroup.getCheckedRadioButtonId());
        String bType = mRadioButton.getText().toString();
        Log.d("Type: ", ""+bType);
        String businessType;
        if(bType.equals(getString(R.string.lbl_salons))){
            businessType = "S";
        }
        else{
            businessType = "B";
        }
        String email = mEmail.getText().toString().trim();
        String phone = mPhone.getText().toString();
        String address = "";
        double latitude = currentLatitude;
        double longitude = currentLongitude;

        // If user fill new address in the form, then use it
        if(!addressApi.isEmpty()){
            address = (String) addressApi.get("address");
            latitude = (double) addressApi.get("lat");
            longitude = (double) addressApi.get("lng");
        }
        // Else use the old address
        else if(currentAddress.equals(mAddress.getText().toString())){
            address = mAddress.getText().toString();
        }

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String phonePattern = "^[0-9]{10}$";
        // Validation of empty inputs
        if (businessName.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty())
        {
            Helper.errorMsgDialog(getActivity(), R.string.incomplete);
        }
        // Validation of email
        else if(!email.matches(emailPattern)){
            Helper.errorMsgDialog(getActivity(), R.string.invalid_email);
        }
        // Validation of address
        /*else if(!addressApi.containsKey("address") || !addressApi.containsKey("lat") || !addressApi.containsKey("lng")){
            Helper.errorMsgDialog(Objects.requireNonNull(getActivity()), R.string.address_incomplete);
        }*/
        // Validation of phone
        else if(!phone.matches(phonePattern)){
            Helper.errorMsgDialog(getActivity(), R.string.invalid_phone);
        }
        else{
            Retrofit retrofit = RetrofitClient.getInstance(getActivity());
            userApi = retrofit.create(UserApi.class);
            BusinessProfile businessProfile = new BusinessProfile(businessName, businessType, email, address, latitude, longitude, phone,"B");
            Call<BusinessProfile> call =  userApi.businessUpdate(businessProfile);
            call.enqueue(new Callback<BusinessProfile>() {
                @Override
                public void onResponse(Call<BusinessProfile> call, Response<BusinessProfile> response) {
                    if(response.isSuccessful()){
                        Log.d("Response: ", ""+response.body());
                        Intent intent = new Intent(getActivity(), BusinessActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                    else{
                        Helper.errorMsgDialog(getActivity(), R.string.response_error);
                        Log.d("Error: ",""+response.message());

                    }
                }

                @Override
                public void onFailure(Call<BusinessProfile> call, Throwable t) {
                    Helper.errorMsgDialog(getActivity(), R.string.network_error);
                    Log.d("Fail: ", t.getMessage());
                }
            });

        }

    }

}
