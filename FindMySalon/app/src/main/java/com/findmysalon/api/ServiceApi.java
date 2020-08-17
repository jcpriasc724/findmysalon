package com.findmysalon.api;

import com.findmysalon.model.Service;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ServiceApi {

    @GET("service/")
    Call<ArrayList<Service>> serviceList();
}
