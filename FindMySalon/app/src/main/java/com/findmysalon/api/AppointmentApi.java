package com.findmysalon.api;

import com.findmysalon.model.Booking;
import com.findmysalon.model.Staff;
import com.findmysalon.model.StaffAvailable;
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

    @GET("/appointment/timetable/")
    Call<ArrayList<StaffAvailable>> availableTimeTable(@Query("service_id") int serviceId,
                                              @Query("business_id") int businessId);

    @FormUrlEncoded
    @POST("/appointment/")
    Call<ResponseBody> submitAppointment(
            @Field("service_id") int serviceId,
            @Field("business_id") int businessId,
            @Field("staff_id") int staffId,
            @Field("date") String date,
            @Field("time") String time
    );
}
