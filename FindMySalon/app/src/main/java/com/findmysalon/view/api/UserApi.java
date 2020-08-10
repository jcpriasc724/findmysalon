package com.findmysalon.view.api;

import com.findmysalon.model.Customer;
import com.findmysalon.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UserApi {
    //@FormUrlEncoded
    @POST("api/user/signup/")
    Call<Customer> customerSignUp(@Body Customer customer);
    /*public void customerSignUp(
            @Field("first_name") String firstName,
            @Field("last_name") String lastName,
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("address") String address,
            @Field("lat") Double lat,
            @Field("lng") Double lng,
            @Field("user_type") String userType
    );*/
}
