package com.findmysalon.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.findmysalon.R;
import com.findmysalon.api.UserApi;
import com.findmysalon.model.Token;
import com.findmysalon.utils.RetrofitClient;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.HashMap;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.findmysalon.utils.Helper;

import static com.findmysalon.utils.abcConstants.ACCESS_TOKEN;
import static com.findmysalon.utils.abcConstants.REFRESH_TOKEN;

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
        // data to Json
        Gson gson=new Gson();
        HashMap<String,String> paramsMap= new HashMap<>();
        paramsMap.put("email",  mEmail.getText().toString());
        paramsMap.put("password",   mPassword.getText().toString());
        String obj=gson.toJson(paramsMap);
        RequestBody body= RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),obj);

        // retrofit
        Retrofit retrofit = RetrofitClient.getInstance(LoginActivity.this);
        userApi = retrofit.create(UserApi.class);
        Call<Token> call = userApi.oathToken(body);
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if(response.code() != 200){
                    Log.d("Error: ",""+response.errorBody());
                    Helper.errorMsgDialog(LoginActivity.this, R.string.invalid_credentials);
                    return;
                }
//                      response.code()
                Token resp = response.body();
                final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(ACCESS_TOKEN,resp.getAccess());
                editor.putString(REFRESH_TOKEN,resp.getRefresh());
                editor.commit();

                Intent intent = new Intent(LoginActivity.this, CustomerActivity.class);
                //ide .putExtra("hi", "HI");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Log.d("Fail: ", t.getMessage());
            }
        });
        // retrofit End
    }
}