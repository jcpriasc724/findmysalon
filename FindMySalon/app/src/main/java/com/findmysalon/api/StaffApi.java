package com.findmysalon.api;

import com.findmysalon.model.Category;
import com.findmysalon.model.Service;
import com.findmysalon.model.Staff;
import com.findmysalon.model.StaffRoster;
import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

import java.util.ArrayList;

public interface StaffApi {

    @GET("staff/")
    Call<ArrayList<Staff>> staffList();

    @Multipart
    @POST("staff/avatar/")
    Call<JsonObject> staffAvatarUpload(@Part MultipartBody.Part image);

    @POST("staff/")
    Call<Staff> staffSubmit(@Body Staff staffObj);

    @GET("staff/{id}/")
    Call<Staff> staffDetail(@Path("id") int id);

    @PUT("staff/{id}/")
    Call<Staff> staffUpdate(
            @Path("id") int id,
            @Body Staff staffObj);

    @DELETE("staff/{id}/")
    Call<ResponseBody> staffDelete(@Path("id") int id);

    // roster
    @GET("staff/{id}/schedule/")
    Call<ArrayList<StaffRoster>> scheduleList(@Path("id") int staffId);

    @Headers({"Content-type:application/json"})
    @POST("staff/{id}/schedule/")
    Call<ResponseBody> submitScheduleList(
            @Path("id") int staffId,
            @Body RequestBody timeList
    );

}
