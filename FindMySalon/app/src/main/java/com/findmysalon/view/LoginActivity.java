package com.findmysalon.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.findmysalon.R;
import com.findmysalon.api.UserApi;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private Button btnSignin;
    private Button btnSignup;
    private EditText mEmail;
    private EditText mPassword;
    private UserApi userApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onStart() {
        super.onStart();

        btnSignin = (Button) findViewById(R.id.btn_signIn);
        btnSignup = (Button) findViewById(R.id.btn_signUp);
        mEmail = findViewById(R.id.etx_email);
        mPassword = findViewById(R.id.etx_password);

        btnSignin.setOnClickListener(v -> {
            signIn();
        });

        btnSignup.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
            //ide .putExtra("hi", "HI");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

    }

    public void errorMsgDialog(String msg)
    {
        AlertDialog d = new AlertDialog.Builder(this)
                .setTitle(R.string.warning)
                .setMessage(msg)
                .setPositiveButton(R.string.ok, null)
                .create();

        d.show();
    }

    public void signIn(){
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
        String msg;
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        // Validation of empty inputs
        if (email.isEmpty() || password.isEmpty())
        {
            msg = getString(R.string.incomplete);
            errorMsgDialog(msg);
        }
        // Validation of email
        else if(!email.matches(emailPattern)){
            msg = getString(R.string.invalid_email);
            errorMsgDialog(msg);
        }
        else{
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(getString(R.string.server_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            userApi = retrofit.create(UserApi.class);
            Call<JsonObject> call = userApi.userSignIn(email, password);

            call.enqueue(new Callback<JsonObject>(){

                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    // If user is not authenticated, display error message
                    if(response.code() == 200){
                        JsonObject resp = response.body();

                        // Store access and refresh token fetched from API into SharedPreference
                        String accessToken = resp.get("access").toString();
                        Log.d("Login access token: ", accessToken);
                        String refreshToken = resp.get("refresh").toString();
                        Long timeStamp = System.currentTimeMillis()/1000;
                        SharedPreferences.Editor editor = getSharedPreferences("FindMeSalon", MODE_PRIVATE).edit();
                        editor.putString("access_token", accessToken);
                        editor.putString("refresh_token", refreshToken);
                        editor.putLong("timestamp", timeStamp);
                        editor.apply();

                        //Navigation.findNavController(v).navigate(R.id.nav_type_user);
                        Intent intent = new Intent(LoginActivity.this, CustomerActivity.class);
                        //ide .putExtra("hi", "HI");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                    else if(response.code() == 401){
                        Log.d("Error: ",""+response.message());
                        String msg = getString(R.string.invalid_credentials);
                        errorMsgDialog(msg);
                    }
                    else{
                        Log.d("Error: ",""+response.message());
                        String msg = getString(R.string.network_error);
                        errorMsgDialog(msg);
                    }

                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.d("Fail: ", t.getMessage());
                    String msg = getString(R.string.network_error);
                    errorMsgDialog(msg);
                }
            });
        }


    }
}