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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.findmysalon.R;
import com.findmysalon.api.AppointmentApi;
import com.findmysalon.api.UserApi;
import com.findmysalon.helpers.PlaceApi;
import com.findmysalon.model.Booking;
import com.findmysalon.model.CustomerProfile;
import com.findmysalon.utils.Helper;
import com.findmysalon.utils.RetrofitClient;
import com.findmysalon.view.CustomerActivity;
import com.findmysalon.view.LoginActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

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

public class UpdateCustomerFragment extends Fragment {

    private EditText mFirstName;
    private EditText mLastName;
    private EditText mEmail;
    private EditText mPhone;
    private AutoCompleteTextView mAddress;
    private String currentAddress = "";
    private double currentLatitude = 0.0;
    private double currentLongitude = 0.0;
    private UserApi userApi;
    private HashMap<String, Object> addressApi;
    private View v;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_update_customer, container, false);

        /*mImgView = (ImageView) v.findViewById(R.id.img_profile_photo);
        mImgView.setOnClickListener(v12 -> {
            selectImage();
        });*/

        /*Button mUpload = (Button) v.findViewById(R.id.btn_upload_photo);
        mUpload.setOnClickListener(v1 -> selectImage());*/

        // Address autocomplete suggestions
        PlaceApi placeApi = new PlaceApi();
        addressApi = placeApi.fetchAddressLatLng((AutoCompleteTextView) v.findViewById(R.id.etx_address), getActivity());
        mFirstName = (EditText) v.findViewById(R.id.etx_first_name);
        mLastName = (EditText) v.findViewById(R.id.etx_last_name);
        mEmail = (EditText) v.findViewById(R.id.etx_email);
        mPhone = (EditText) v.findViewById(R.id.etx_phone);
        mAddress = (AutoCompleteTextView) v.findViewById(R.id.etx_address);
        CardView mUpdateButton = v.findViewById(R.id.btn_update);
        mUpdateButton.setOnClickListener(v1 -> customerUpdate());

        // retrofit
        Retrofit retrofit = RetrofitClient.getInstance(getActivity());
        userApi = retrofit.create(UserApi.class);
        Call<CustomerProfile> call = userApi.getUserDetails();
        call.enqueue(new Callback<CustomerProfile>() {
            @Override
            public void onResponse(Call<CustomerProfile> call, Response<CustomerProfile> response) {
                if(response.isSuccessful()){
                    mFirstName.setText(response.body().getFirstName());
                    mLastName.setText(response.body().getLastName());
                    mEmail.setText(response.body().getEmail());
                    mAddress.setText(response.body().getAddress());
                    mPhone.setText(response.body().getPhone());
                    currentAddress = response.body().getAddress();
                    currentLatitude = response.body().getLatitude();
                    currentLongitude = response.body().getLongitude();
                }
            }

            @Override
            public void onFailure(Call<CustomerProfile> call, Throwable t) {
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

    // Method to handle customer signUp
    public void customerUpdate(){
        String firstName = mFirstName.getText().toString();
        String lastName = mLastName.getText().toString();
        String email = mEmail.getText().toString().trim();
        String phone = mPhone.getText().toString();
        Log.d("ADDRESS", addressApi+"");
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
        Log.d("Address ", ""+currentAddress.equals(mAddress.getText().toString()));

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String phonePattern = "^[0-9]{10}$";
        // Validation of empty inputs
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty())
        {
            Helper.errorMsgDialog(getActivity(), R.string.incomplete);
        }
        // Validation of email
        else if(!email.matches(emailPattern)){
            Helper.errorMsgDialog(getActivity(), R.string.invalid_email);
        }
        // Validation of phone
        else if(!phone.matches(phonePattern)){
            Helper.errorMsgDialog(getActivity(), R.string.invalid_phone);
        }
        else{
            Retrofit retrofit = RetrofitClient.getInstance(getActivity());
            userApi = retrofit.create(UserApi.class);
            CustomerProfile customerProfile = new CustomerProfile(firstName, lastName, email, address, latitude, longitude, phone);
            Call<CustomerProfile> call =  userApi.customerUpdate(customerProfile);
            call.enqueue(new Callback<CustomerProfile>() {
                @Override
                public void onResponse(Call<CustomerProfile> call, Response<CustomerProfile> response) {
                    if(response.isSuccessful()){
                        Log.d("Response: ", ""+response.body());
                        Intent intent = new Intent(getActivity(), CustomerActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                    else{
                        Helper.errorMsgDialog(getActivity(), R.string.response_error);
                        Log.d("Error: ",""+response.message());

                    }
                }

                @Override
                public void onFailure(Call<CustomerProfile> call, Throwable t) {
                    Helper.errorMsgDialog(getActivity(), R.string.network_error);
                    Log.d("Fail: ", t.getMessage());
                }
            });
        }
    }


}
