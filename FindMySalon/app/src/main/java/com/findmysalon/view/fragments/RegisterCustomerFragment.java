package com.findmysalon.view.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.findmysalon.R;
import com.findmysalon.model.Customer;
import com.findmysalon.model.User;
import com.findmysalon.view.api.UserApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterCustomerFragment extends Fragment {

    //Button btnNext;
    private EditText mFirstName;
    private EditText mLastName;
    private EditText mEmail;
    private EditText mAddress;
    private EditText mPhone;
    private EditText mPassword;
    private EditText mRePassword;
    private Button mSubmitButton;

    private UserApi userApi;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register_customer, container, false);
        //((AppCompatActivity)getActivity()).getSupportActionBar().hide();

       /* btnNext = view.findViewById(R.id.btn_next);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.nav_reg_business_services);
            }
        });*/

        mFirstName = (EditText) v.findViewById(R.id.etx_first_name);
        //firstName.setText(mVenue.getName());

        mLastName = (EditText) v.findViewById(R.id.etx_last_name);

        mEmail = (EditText) v.findViewById(R.id.etx_email);

        mAddress = (EditText) v.findViewById(R.id.etx_address);

        mPhone = (EditText) v.findViewById(R.id.etx_phone);

        mPassword = (EditText) v.findViewById(R.id.etx_password);

        mRePassword = (EditText) v.findViewById(R.id.etx_rePassword);

        mSubmitButton = (Button) v.findViewById(R.id.btn_submit);
        mSubmitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                customerSignUp();
                /*Retrofit retrofit = new Retrofit.Builder()
                        //.baseUrl("https://api.dogdog.info/")
                        .baseUrl("http://10.0.2.2:8000/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                userApi = retrofit.create(UserApi.class);
                userSignUp();*/
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

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String phonePattern = "^[0-9]{10}$";
        // Validation of empty inputs
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || address.isEmpty() || phone.isEmpty() || password.isEmpty() || rePassword.isEmpty())
        {
            String msg = getString(R.string.incomplete);
            errorMsgDialog(msg);
        }
        // Validation of email
        else if(!email.matches(emailPattern)){
            String msg = getString(R.string.invalid_email);
            errorMsgDialog(msg);
        }
        // Validation of phone
        else if(!phone.matches(phonePattern)){
            String msg = getString(R.string.invalid_phone);
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
}
