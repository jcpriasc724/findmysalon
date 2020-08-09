package com.findmysalon.view.api;

import com.findmysalon.model.Customer;
import com.findmysalon.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserApi {

    @POST("api/user/signup/")
    Call<Customer> customerSignUp(@Body Customer customer);
}
