package com.findmysalon.api;

import com.findmysalon.model.Category;
import com.findmysalon.model.Service;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface ServiceApi {

    @GET("service/")
    Call<ArrayList<Service>> serviceList();

    @GET("service/category/")
    Call<ArrayList<Category>> serviceCategoryList();

    @POST("service/")
    Call<Service> serviceSubmit(@Body Service service);

    @GET("service/{id}/")
    Call<Service> serviceDetail(@Path("id") int id);

    @PUT("service/{id}/")
    Call<Service> serviceUpdate(
            @Path("id") int id,
            @Body Service serviceObj);

    @DELETE("service/{id}/")
    Call<ResponseBody> serviceDelete(@Path("id") int id);
}
