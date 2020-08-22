package com.findmysalon.api;

import com.findmysalon.model.Booking;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface AppointmentApi {

    @GET("appointment/")
    Call<ArrayList<Booking>> appointmentList();

    @FormUrlEncoded
    @PUT("/appointment/{id}/status/")
    Call<ResponseBody> appointmentStatusChange(@Path("id") int id,
                                             @Field("status") String status);
}
