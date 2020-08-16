package com.findmysalon.view.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.findmysalon.R;
import com.findmysalon.helpers.PlaceApi;
import com.findmysalon.model.Customer;
import com.findmysalon.api.UserApi;
import com.findmysalon.utils.Helper;

import java.util.ArrayList;
import java.util.HashMap;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.findmysalon.utils.abcConstants.BASE_URL;

public class RegisterCustomerFragment extends Fragment {

    private EditText mFirstName;
    private EditText mLastName;
    private EditText mEmail;
    private EditText mPhone;
    private EditText mPassword;
    private EditText mRePassword;
    private UserApi userApi;
    private HashMap<String, Object> addressApi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register_customer, container, false);

        // Address autocomplete suggestions
        PlaceApi placeApi = new PlaceApi();
        addressApi = placeApi.fetchAddressLatLng(v.findViewById(R.id.etx_address), getActivity());

        /*final AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) v.findViewById(R.id.etx_address);
        autoCompleteTextView.setAdapter(new PlaceAutoSuggestionAdapter(getActivity(),android.R.layout.simple_list_item_1));
        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            Log.d("Address : ",autoCompleteTextView.getText().toString());
            LatLng latLng = getLatLngFromAddress(autoCompleteTextView.getText().toString());
            if(latLng!=null) {
                Log.d("Lat Lng : ", " " + latLng.latitude + " " + latLng.longitude);
                mAddress = autoCompleteTextView.getText().toString();
                mLat = latLng.latitude;
                mLng = latLng.longitude;
                *//*Address address=getAddressFromLatLng(latLng);
                if(address!=null) {

                    Log.d("Address : ", "" + address.toString());
                    Log.d("Address Line : ",""+address.getAddressLine(0));
                    Log.d("Phone : ",""+address.getPhone());
                    Log.d("Pin Code : ",""+address.getPostalCode());
                    Log.d("Feature : ",""+address.getFeatureName());
                    Log.d("More : ",""+address.getLocality());
                }
                else {
                    Log.d("Address","Address Not Found");
                }*//*
            }
            else {
                Log.d("Lat Lng","Lat Lng Not Found");
            }

        });*/

        mFirstName = (EditText) v.findViewById(R.id.etx_first_name);
        mLastName = (EditText) v.findViewById(R.id.etx_last_name);
        mEmail = (EditText) v.findViewById(R.id.etx_email);
        mPhone = (EditText) v.findViewById(R.id.etx_phone);
        mPassword = (EditText) v.findViewById(R.id.etx_password);
        mRePassword = (EditText) v.findViewById(R.id.etx_rePassword);
        Button mSubmitButton = (Button) v.findViewById(R.id.btn_submit);
        mSubmitButton.setOnClickListener(v1 -> customerSignUp());

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    // Method to handle customer signUp
    public void customerSignUp(){
        String firstName = mFirstName.getText().toString();
        String lastName = mLastName.getText().toString();
        String email = mEmail.getText().toString().trim();
        String phone = mPhone.getText().toString();
        String password = mPassword.getText().toString();
        String rePassword = mRePassword.getText().toString();
        String address = (String) addressApi.get("address");
        double latitude = (double) addressApi.get("lat");
        double longitude = (double) addressApi.get("lng");

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String phonePattern = "^[0-9]{10}$";
        // Validation of empty inputs
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || rePassword.isEmpty())
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
        else if(!password.equals(rePassword)){
            Helper.errorMsgDialog(getActivity(), R.string.password_no_match);
        }
        else{
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            userApi = retrofit.create(UserApi.class);
            userSignUp(firstName, lastName, email, password, address, latitude, longitude, phone);
        }
    }

    // Method to handle user sign up
    private void userSignUp(String firstName, String lastName, String email, String password, String address, double latitude, double longitude, String phone ){

        Customer customer = new Customer(firstName, lastName, email, password, phone, address, latitude,longitude, "C" );
        Call<Customer> call = userApi.customerSignUp(customer);

        // Using enqueue to make network call asynchronous
        call.enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                if(response.isSuccessful()){
                    Customer resp = response.body();
                    Log.d("Response: ", ""+resp);
                }
                else{
                    Helper.errorMsgDialog(getActivity(), R.string.response_error);
                    Log.d("Error: ",""+response.message());
                }

            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                Helper.errorMsgDialog(getActivity(), R.string.network_error);
                Log.d("Fail: ", t.getMessage());
            }
        });
    }

    /*// Method to get latitude and longitude from address
    private LatLng getLatLngFromAddress(String address){

        Geocoder geocoder=new Geocoder(getActivity());
        List<Address> addressList;

        try {
            addressList = geocoder.getFromLocationName(address, 1);
            if(addressList!=null){
                Address singleAddress = addressList.get(0);
                LatLng latLng = new LatLng(singleAddress.getLatitude(),singleAddress.getLongitude());
                return latLng;
            }
            else{
                return null;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }*/

    /*private Address getAddressFromLatLng(LatLng latLng){
        Geocoder geocoder=new Geocoder(getActivity());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 5);
            if(addresses!=null){
                Address address = addresses.get(0);
                return address;
            }
            else{
                return null;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }*/
}
