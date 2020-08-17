package com.findmysalon.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.findmysalon.model.Token;
import com.findmysalon.view.LoginActivity;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;

import static com.findmysalon.utils.abcConstants.ACCESS_TOKEN;
import static com.findmysalon.utils.abcConstants.BASE_URL;
import static com.findmysalon.utils.abcConstants.JWT_TOKEN_PREFIX;
import static com.findmysalon.utils.abcConstants.REFRESH_TOKEN;
import static com.findmysalon.utils.abcConstants.TOKEN_EXPIRED;
import static com.findmysalon.utils.abcConstants.TOKEN_VALID_TIME;

public class TokenInterceptor implements Interceptor {
    private static Retrofit retrofit = null;
    private static String accessToken;
    private static String refreshToken;
    private static Long tokenExpired;
    private static TokenManager tokenManager;
    private static Context mContext;

    public TokenInterceptor(Context context) {
        this.mContext = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request modifiedRequest = null;

        tokenManager = new TokenManager() {
            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);

            @Override
            public String getToken() {
                tokenExpired = sharedPreferences.getLong(TOKEN_EXPIRED, 0);
                Long currentTimeStamp = System.currentTimeMillis()/1000;
                Long diff = currentTimeStamp - tokenExpired;
                if (diff >= 0) {
                    return "";
                }
                accessToken = sharedPreferences.getString(ACCESS_TOKEN, "");
                return accessToken;
            }

            @Override
            public boolean hasToken() {
                accessToken = sharedPreferences.getString(ACCESS_TOKEN, "");
                if (accessToken != null && !accessToken.equals("")) {
                    return true;
                }
                return false;
            }

            @Override
            public void clearToken() {
                sharedPreferences.edit().putString(ACCESS_TOKEN, "").apply();
            }

            @Override
            public String refreshToken() {
                final String accessToken = null;

//                RequestBody reqbody = RequestBody.create(null, new byte[0]);
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("refresh", refreshToken)
                        .build();
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(BASE_URL + "api/user/token/refresh/")
                        .method("POST", requestBody)
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    if ((response.code()) == 200) {
                        // Get response
                        String jsonData = response.body().string();
//                        Log.i(null,response.code() + " "+ jsonData);
                        Gson gson = new Gson();
                        Token refreshTokenResponseModel = gson.fromJson(jsonData, Token.class);
                        sharedPreferences.edit().putString(ACCESS_TOKEN, refreshTokenResponseModel.getAccess()).apply();
                        sharedPreferences.edit().putLong(TOKEN_EXPIRED, ((System.currentTimeMillis()/1000) + TOKEN_VALID_TIME * 60) ).apply();
                        return accessToken;
                    } else {
                        // if refresh token fails, redirect to login activity
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mContext.startActivity(intent);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return accessToken;
            }
        };

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        refreshToken = sharedPreferences.getString(REFRESH_TOKEN, "");
//        Log.i(null, String.valueOf( request.header("NonAuthentication")));
        if(request.header("NonAuthentication") == null) {
            // validate the token is expired or not
            if (tokenManager.getToken() == "") {
                tokenManager.clearToken();
                tokenManager.refreshToken();
            }
            request = request.newBuilder()
                    .addHeader("Authorization", JWT_TOKEN_PREFIX + " " + tokenManager.getToken())
                    .build();
        }
        Response response = chain.proceed(request);



        boolean unauthorized =false;
        if(response.code() == 401 || response.code() == 422){
            unauthorized=true;
        }

        if (unauthorized) {
//            tokenManager.clearToken();
//            tokenManager.refreshToken();
//            accessToken = sharedPreferences.getString(ACCESS_TOKEN, "");
//            if(accessToken!=null){
//                response.close();
////                Log.i(null, "1: " + JWT_TOKEN_PREFIX + " "+ tokenManager.getToken());
//                modifiedRequest = request.newBuilder()
//                        .addHeader("Authorization", JWT_TOKEN_PREFIX + " "+ tokenManager.getToken())
//                        .build();
//                Log.i(null, "2:" + JWT_TOKEN_PREFIX + " "+ tokenManager.getToken());
//                Log.i(null, modifiedRequest.header("Authorization"));
//
//                response = chain.proceed(modifiedRequest);
//                return response;
//            }
            response.close();
            Intent intent = new Intent(mContext, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mContext.startActivity(intent);
        }
        return response;
    }
}
