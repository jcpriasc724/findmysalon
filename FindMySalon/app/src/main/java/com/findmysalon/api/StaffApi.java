package com.findmysalon.api;

import com.findmysalon.model.Staff;
import retrofit2.Call;
import retrofit2.http.GET;

import java.util.ArrayList;

public interface StaffApi {

    @GET("staff/")
    Call<ArrayList<Staff>> staffList();

}
