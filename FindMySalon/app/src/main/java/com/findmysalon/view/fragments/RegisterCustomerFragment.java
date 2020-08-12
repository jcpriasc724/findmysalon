package com.findmysalon.view.fragments;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.findmysalon.R;
import com.findmysalon.model.Customer;
import com.findmysalon.model.User;
import com.findmysalon.view.api.UserApi;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.findmysalon.view.adapters.PlaceAutoSuggestionAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

public class RegisterCustomerFragment extends Fragment {

    //Button btnNext;
    private EditText mFirstName;
    private EditText mLastName;
    private EditText mEmail;
    private EditText mAddress;
    private TextView mSearchResult;
    private ListView listView;
    private StringBuilder mResult;
    private EditText mPhone;
    private EditText mPassword;
    private EditText mRePassword;
    private Button mSubmitButton;
    private PlacesClient placesClient;
    private UserApi userApi;
    private String apiKey;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Left", "onCreate()");
        apiKey = getString(R.string.api_key);
        // Initialize the SDK
        if (!Places.isInitialized()) {
            Places.initialize(getActivity().getApplicationContext(), apiKey);
        }
        // Create a new PlacesClient instance
        placesClient = Places.createClient(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register_customer, container, false);

        final AutoCompleteTextView autoCompleteTextView = v.findViewById(R.id.etx_address);
        autoCompleteTextView.setAdapter(new PlaceAutoSuggestionAdapter(getActivity(),android.R.layout.simple_list_item_1));
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Address : ",autoCompleteTextView.getText().toString());
                LatLng latLng = getLatLngFromAddress(autoCompleteTextView.getText().toString());
                //Log.d("Lat lng: ", ""+latLng);
                if(latLng!=null) {
                    Log.d("Lat Lng : ", " " + latLng.latitude + " " + latLng.longitude);
                    Address address=getAddressFromLatLng(latLng);
                    if(address!=null) {
                        Log.d("Address : ", "" + address.toString());
                        Log.d("Address Line : ",""+address.getAddressLine(0));
                        Log.d("Phone : ",""+address.getPhone());
                        Log.d("Pin Code : ",""+address.getPostalCode());
                        Log.d("Feature : ",""+address.getFeatureName());
                        Log.d("More : ",""+address.getLocality());
                    }
                    else {
                        Log.d("Adddress","Address Not Found");
                    }
                }
                else {
                    Log.d("Lat Lng","Lat Lng Not Found");
                }

            }
        });

        mFirstName = (EditText) v.findViewById(R.id.etx_first_name);
        //firstName.setText(mVenue.getName());

        mLastName = (EditText) v.findViewById(R.id.etx_last_name);

        mEmail = (EditText) v.findViewById(R.id.etx_email);

        //mSearchResult = (TextView) v.findViewById(R.id.search_result);

        /*mAddress = (EditText) v.findViewById(R.id.etx_address);
        mAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("Typed text: ",""+s);
                //dataAdapter.getFilter().filter(s.toString());
                //addressAutocomplete(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
*/
        mPhone = (EditText) v.findViewById(R.id.etx_phone);

        mPassword = (EditText) v.findViewById(R.id.etx_password);

        mRePassword = (EditText) v.findViewById(R.id.etx_rePassword);

        mSubmitButton = (Button) v.findViewById(R.id.btn_submit);
        mSubmitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                customerSignUp();
            }
        });

        return v;
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    public void errorMsgDialog(String msg)
    {
        AlertDialog d = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.warning)
                .setMessage(msg)
                .setPositiveButton(R.string.ok, null)
                .create();

        d.show();
    }

    // Method to handle customer signUp
    public void customerSignUp(){
        String firstName = mFirstName.getText().toString();
        String lastName = mLastName.getText().toString();
        String email = mEmail.getText().toString().trim();
        String address = mAddress.getText().toString();
        String phone = mPhone.getText().toString();
        String password = mPassword.getText().toString();
        String rePassword = mRePassword.getText().toString();
        String msg;
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String phonePattern = "^[0-9]{10}$";
        // Validation of empty inputs
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || address.isEmpty() || phone.isEmpty() || password.isEmpty() || rePassword.isEmpty())
        {
            msg = getString(R.string.incomplete);
            errorMsgDialog(msg);
        }
        // Validation of email
        else if(!email.matches(emailPattern)){
            msg = getString(R.string.invalid_email);
            errorMsgDialog(msg);
        }
        // Validation of phone
        else if(!phone.matches(phonePattern)){
            msg = getString(R.string.invalid_phone);
            errorMsgDialog(msg);
        }
        else if(!password.equals(rePassword)){
            msg = getString(R.string.password_no_match);
            errorMsgDialog(msg);
        }
        else{
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8000/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            userApi = retrofit.create(UserApi.class);
            userSignUp(firstName, lastName, email, password, address, phone);
        }

    }

    private void userSignUp(String firstName, String lastName, String email, String password, String address, String phone ){
        Log.d("Msg: ", "Signup initiated");
        //Customer customer = new Customer("Nirpa","Rai","nirpa@gmail.com","test", "0433949553", "Brunswick", 92.123,-23.1212, "C");
        //Log.d("JSON: ", ""+customer);
        Customer customer = new Customer(firstName, lastName, email, password, address, phone,92.123,-23.1212, "C" );
        Call<Customer> call = userApi.customerSignUp(customer);

        call.enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                if(!response.isSuccessful()){
                    Log.d("Error: ",""+response.message());
                }

                Customer resp = response.body();
                Log.d("Response: ", ""+resp);
            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                Log.d("Fail: ", t.getMessage());
            }
        });
    }

    // Method to handle auto complete of address
    public void addressAutocomplete(String address){
        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        RectangularBounds bounds = RectangularBounds.newInstance(
                new LatLng(-33.880490, 151.184363), //dummy lat/lng
                new LatLng(-33.858754, 151.229596));

        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setLocationBias(bounds)
                .setCountry("AU")
                .setTypeFilter(TypeFilter.ADDRESS)
                .setSessionToken(token)
                .setQuery(address)
                .build();

        placesClient.findAutocompletePredictions(request).addOnSuccessListener(response -> {
            mResult = new StringBuilder();
            for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                mResult.append(" ").append(prediction.getFullText(null) + "\n");
                Log.i(TAG, prediction.getPlaceId());
                Log.i(TAG, prediction.getPrimaryText(null).toString());
            }
            mSearchResult.setText(String.valueOf(mResult));
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                Log.e(TAG, "Place not found: " + apiException.getStatusCode());
            }
        });
    }

    private LatLng getLatLngFromAddress(String address){

        Geocoder geocoder=new Geocoder(getActivity());
        List<Address> addressList;

        try {
            addressList = geocoder.getFromLocationName(address, 1);
            if(addressList!=null){
                Address singleaddress=addressList.get(0);
                LatLng latLng=new LatLng(singleaddress.getLatitude(),singleaddress.getLongitude());
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

    }

    private Address getAddressFromLatLng(LatLng latLng){
        Geocoder geocoder=new Geocoder(getActivity());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 5);
            if(addresses!=null){
                Address address=addresses.get(0);
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

    }
}
