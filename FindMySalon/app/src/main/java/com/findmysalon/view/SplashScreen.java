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
import com.findmysalon.model.Token;
import com.findmysalon.utils.Helper;
import com.findmysalon.utils.RetrofitClient;
import com.google.gson.Gson;

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

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_SCREEN = 3000;

    Animation topAnimation;
    ImageView imageView;

    private UserApi userApi;

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

        // data to Json
        Gson gson=new Gson();
        HashMap<String,String> paramsMap= new HashMap<>();
        paramsMap.put("email",  "info@d2mbeauty.com");
        paramsMap.put("password",   "123456");
        String obj=gson.toJson(paramsMap);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),obj);

        // retrofit
        Retrofit retrofit = RetrofitClient.getInstance(SplashScreen.this);
        userApi = retrofit.create(UserApi.class);
        Call<Token> call = userApi.oathToken(body);
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if(response.isSuccessful()){
                    Token resp = response.body();

                    //Log.d("User type", resp.toString());
                    final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SplashScreen.this);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    // Store access and refresh token along with access token expiry information in shared preference
                    editor.putString(ACCESS_TOKEN,resp.getAccess());
                    editor.putString(REFRESH_TOKEN,resp.getRefresh());
                    editor.putLong(TOKEN_EXPIRED, ((System.currentTimeMillis()/1000) + TOKEN_VALID_TIME * 60) );
                    editor.commit();

                    // If user is business then redirect them to business activity
                    if(resp.getUserType().equals("B")){
                        Intent intent = new Intent(SplashScreen.this, BusinessActivity.class);
                        //ide .putExtra("hi", "HI");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                    // If user is customer then redirect them to customer activity
                    else if(resp.getUserType().equals("C")){
                        Intent intent = new Intent(SplashScreen.this, CustomerActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }

                }
                else{
                    Log.d("Error: ",""+response.errorBody());
                    Helper.errorMsgDialog(SplashScreen.this, R.string.invalid_credentials);
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Log.d("Fail: ", t.getMessage());
            }
        });
        // retrofit End

    }
}
