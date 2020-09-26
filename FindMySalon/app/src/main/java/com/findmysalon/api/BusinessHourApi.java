package com.findmysalon.api;

import com.findmysalon.model.BusinessHour;
import com.findmysalon.model.StaffRoster;

import java.util.ArrayList;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface BusinessHourApi {

    // business hour for customers
    @GET("businesshour/{id}/")
    Call<ArrayList<BusinessHour>> businessHourList(@Path("id") int id);

    // business hour for business
    @GET("businesshour/")
    Call<ArrayList<BusinessHour>> businessHourList();

    @Headers({"Content-type:application/json"})
    @POST("businesshour/")
    Call<ResponseBody> submitBusinessHourList(
            @Body RequestBody timeList
    );
}
