package com.findmysalon.view.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.findmysalon.R;
import com.findmysalon.api.UserApi;
import com.findmysalon.helpers.PlaceApi;
import com.findmysalon.model.Business;
import com.findmysalon.model.Customer;
import com.findmysalon.utils.Helper;
import com.findmysalon.view.RegistrationActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.findmysalon.utils.abcConstants.BASE_URL;

public class RegisterBusinessFragment extends Fragment {

    private EditText mBusinessName;
    private RadioButton mRadioButton;
    private EditText mEmail;
    private EditText mPhone;
    private EditText mPassword;
    private EditText mRePassword;
    private HashMap<String, Object> addressApi;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register_business, container, false);
        //((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        // Address autocomplete suggestions
        PlaceApi placeApi = new PlaceApi();
        addressApi = placeApi.fetchAddressLatLng(v.findViewById(R.id.etx_address), getActivity());

        mBusinessName = (EditText) v.findViewById(R.id.etx_business_name);
        RadioGroup mRadioGroup = (RadioGroup) v.findViewById(R.id.rdg_type_business);
        // find the radiobutton by returned id
        mRadioButton = (RadioButton) v.findViewById(mRadioGroup.getCheckedRadioButtonId());
        mEmail = (EditText) v.findViewById(R.id.etx_email);
        mPhone = (EditText) v.findViewById(R.id.etx_phone);
        mPassword = (EditText) v.findViewById(R.id.etx_password);
        mRePassword = (EditText) v.findViewById(R.id.etx_rePassword);
        Button mSubmitButton = (Button) v.findViewById(R.id.btn_submit);
        mSubmitButton.setOnClickListener(v1 -> businessSignUp());

        Button btnNext = v.findViewById(R.id.btn_next);

        btnNext.setOnClickListener(v12 -> Navigation.findNavController(v12).navigate(R.id.nav_reg_business_services));

        return v;
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    // Method to handle signUp for business
    public void businessSignUp(){
        String businessName = mBusinessName.getText().toString();
        String businessType = mRadioButton.getText().toString();
        String bType;
        if(businessType.equals(getString(R.string.lbl_salons))){
            bType = "S";
        }
        else{
            bType = "B";
        }
        String email = mEmail.getText().toString().trim();
        String phone = mPhone.getText().toString();
        String password = mPassword.getText().toString();
        String rePassword = mRePassword.getText().toString();
        String address = "";
        double latitude = 0.0;
        double longitude = 0.0;

        if(!addressApi.isEmpty()){
            address = (String) addressApi.get("address");
            latitude = (double) addressApi.get("lat");
            longitude = (double) addressApi.get("lng");
        }

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String phonePattern = "^[0-9]{10}$";
        // Validation of empty inputs
        if (businessName.isEmpty() || businessType.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty() || password.isEmpty() || rePassword.isEmpty())
        {
            Helper.errorMsgDialog(getActivity(), R.string.incomplete);
        }
        // Validation of email
        else if(!email.matches(emailPattern)){
            Helper.errorMsgDialog(getActivity(), R.string.invalid_email);
        }
        // Validation of address
        else if(!addressApi.containsKey("address") || !addressApi.containsKey("lat") || !addressApi.containsKey("lng")){
            Helper.errorMsgDialog(Objects.requireNonNull(getActivity()), R.string.address_incomplete);
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

            UserApi userApi = retrofit.create(UserApi.class);

            Business business = new Business(businessName, bType, email, password, phone, address, latitude,longitude, "B" );
            Log.d("Business type:", businessType);
            Log.d("ADDRESS API", addressApi.get("lat")+"");
            Call<Business> call = userApi.businessSignUp(business);

            // Using enqueue to make network call asynchronous
            call.enqueue(new Callback<Business>() {
                @Override
                public void onResponse(Call<Business> call, Response<Business> response) {
                    if(response.isSuccessful()){
                        Business resp = response.body();
                        Log.d("Response: ", ""+resp);
                    }
                    else{
                        Helper.errorMsgDialog(getActivity(), R.string.response_error);
                        Log.d("Error: ",""+response.message());
                    }
                }

                @Override
                public void onFailure(Call<Business> call, Throwable t) {
                    Helper.errorMsgDialog(getActivity(), R.string.network_error);
                    Log.d("Fail: ", t.getMessage());
                }
            });
        }

    }

}
