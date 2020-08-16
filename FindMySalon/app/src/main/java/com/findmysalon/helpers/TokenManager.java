/*
package com.findmysalon.helpers;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.util.Log;

import com.findmysalon.R;
import com.findmysalon.api.UserApi;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TokenManager extends ContextWrapper {
    private final SharedPreferences sharedPrefs;
    private String accessToken;
    private String refreshToken;
    private Long timeStamp;
    private Retrofit retrofit;
    private UserApi userApi;

    public TokenManager(Context base){
        super(base);
        sharedPrefs = getSharedPreferences("FindMeSalon", 0);
        refreshToken = sharedPrefs.getString("refresh_token", null);
        accessToken = sharedPrefs.getString("access_token", null);
        timeStamp = sharedPrefs.getLong("timestamp", 0);
        retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.server_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userApi = retrofit.create(UserApi.class);
    }

    public String getAccessToken(){
        Log.d("Token manager", accessToken);
        Log.d("Refresh token", refreshToken);
        final String[] recentAccessToken = new String[1];
        Long currentTimeStamp = System.currentTimeMillis()/1000;
        Long diff = currentTimeStamp - timeStamp;
        // If 3 minutes elapsed since last access token was fetched, then request new access token
        // by passing refresh token
        if (diff >= 0) {
            Log.d("Server", "" + getString(R.string.server_url));
            Log.d("Timestamp", "" + timeStamp);
            Log.d("Current timestamp", "" + currentTimeStamp);
            Log.d("Diff timestamp", "" + diff);
            Call<JsonObject> call = userApi.getNewAccessToken(refreshToken);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    // If user is not authenticated, display error message
                    if (!response.isSuccessful()) {
                        Log.d("Error code: ", response.code()+" Could not fetch new access token");
                    }
                    // Proceed if success
                    else{
                        JsonObject resp = response.body();

                        // Store new access token fetched from API into SharedPreference
                        String newAccessToken = resp.get("access").toString();
                        SharedPreferences.Editor editor = getSharedPreferences("FindMeSalon", MODE_PRIVATE).edit();
                        editor.putString("access_token", newAccessToken);
                        editor.apply();
                        recentAccessToken[0] = newAccessToken;
                        Log.d("Customer access token: ", newAccessToken);
                    }

                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.d("Fail: ", t.getMessage());
                }
            });
        }
        else{
            recentAccessToken[0] = accessToken;
        }
        return recentAccessToken[0];
    }

    public String getRefreshToken(){
        return refreshToken;
    }
}
*/
