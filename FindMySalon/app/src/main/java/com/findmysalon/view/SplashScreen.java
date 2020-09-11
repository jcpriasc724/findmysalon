package com.findmysalon.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.findmysalon.R;
import com.findmysalon.api.UserApi;
import com.findmysalon.model.Business;
import com.findmysalon.model.BusinessProfile;
import com.findmysalon.model.CustomerProfile;
import com.findmysalon.model.Token;
import com.findmysalon.utils.Helper;
import com.findmysalon.utils.RetrofitClient;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.HashMap;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.findmysalon.utils.abcConstants.ACCESS_TOKEN;
import static com.findmysalon.utils.abcConstants.REFRESH_TOKEN;
import static com.findmysalon.utils.abcConstants.TOKEN_EXPIRED;
import static com.findmysalon.utils.abcConstants.TOKEN_VALID_TIME;
import static com.findmysalon.utils.abcConstants.USER_TYPE;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_SCREEN = 3000;

    Animation topAnimation;
    ImageView imageView;

    private UserApi userApi;
    SharedPreferences sharedPreferences;
    String accessToken, userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        imageView = (ImageView) findViewById(R.id.img_icono_splash);

        imageView.setAnimation(topAnimation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                signIn();

//                Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
//                startActivity(intent);
//                finish();
            }
        }, SPLASH_SCREEN);

//        Intent intent = new Intent(this, CustomerActivity.class);
//        startActivity(intent);
//        finish();

    }

    public void signIn(){

//        Intent intent = new Intent(LoginActivity.this, CustomerActivity.class);
        //ide .putExtra("hi", "HI");
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SplashScreen.this);
        accessToken = sharedPreferences.getString(ACCESS_TOKEN, "");
        userType = sharedPreferences.getString(USER_TYPE, "");

        if(accessToken.equals("") || userType.equals("")){
            Intent intent = new Intent(SplashScreen.this, LoginActivity.class );
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return;
        }

        if(userType.equals("B")){
            // retrofit
            Retrofit retrofit = RetrofitClient.getInstance(SplashScreen.this);
            userApi = retrofit.create(UserApi.class);
            Call<BusinessProfile> call = userApi.getBusinessDetails();
            call.enqueue(new Callback<BusinessProfile>() {
                @Override
                public void onResponse(Call<BusinessProfile> call, Response<BusinessProfile> response) {
                    if(response.isSuccessful()){
                        BusinessProfile resp = response.body();
                        // If user is business then redirect them to business activity
                        Intent intent = new Intent(SplashScreen.this, BusinessActivity.class);
                        //ide .putExtra("hi", "HI");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                    else{
                        Log.d("Error: ",""+response.errorBody());
                        Helper.errorMsgDialog(SplashScreen.this, R.string.invalid_credentials);
                    }
                }

                @Override
                public void onFailure(Call<BusinessProfile> call, Throwable t) {
                    Log.d("Fail: ", t.getMessage());
                }
            });
            // retrofit End
        } else {
            // retrofit
            Retrofit retrofit = RetrofitClient.getInstance(SplashScreen.this);
            userApi = retrofit.create(UserApi.class);
            Call<CustomerProfile> call = userApi.getUserDetails();
            call.enqueue(new Callback<CustomerProfile>() {
                @Override
                public void onResponse(Call<CustomerProfile> call, Response<CustomerProfile> response) {
                    if(response.isSuccessful()){
                        CustomerProfile resp = response.body();
                        // If user is business then redirect them to business activity
                        Intent intent = new Intent(SplashScreen.this, CustomerActivity.class);
                        //ide .putExtra("hi", "HI");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                    else{
                        Log.d("Error: ",""+response.errorBody());
                        Helper.errorMsgDialog(SplashScreen.this, R.string.invalid_credentials);
                    }
                }

                @Override
                public void onFailure(Call<CustomerProfile> call, Throwable t) {
                    Log.d("Fail: ", t.getMessage());
                }
            });
            // retrofit End
        }

    }
}
