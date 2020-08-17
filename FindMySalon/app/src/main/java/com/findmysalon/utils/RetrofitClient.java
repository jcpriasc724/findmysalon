package com.findmysalon.utils;

import android.content.Context;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.findmysalon.utils.abcConstants.BASE_URL;

public class RetrofitClient {

    private static Retrofit retrofit = null;

    private RetrofitClient() {
        // private constructor to prevent access
        // only way to access: Retrofit client = RetrofitClient.getInstance();
    }

    public static Retrofit getInstance(Context context) {
        if (retrofit == null) {

            // !! This interceptor is not required for everyone !!
            // Main purpose is this interceptor is reducing server calls

            // Our token needs to be refreshed after 10 hours
            // We came application after 50 hours and try to make request.
            // Of course token is expired and it will return 401
            // So this interceptor checks time and refreshes token before any 401
            // If this fails and I get 401 then my TokenAuthenticator do his job.
            // if my TokenAuthenticator fails too, basically I just logout user

//            TokenInterceptor tokenInterceptor = new TokenInterceptor();
//            TokenAuthenticator tokenAuthenticator = new TokenAuthenticator();
            OkHttpClient okClient = new OkHttpClient.Builder()
                    .addInterceptor(new TokenInterceptor(context))
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okClient)
                    .build();

        }
        return retrofit;
    }

}