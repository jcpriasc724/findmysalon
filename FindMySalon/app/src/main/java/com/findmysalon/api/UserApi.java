package com.findmysalon.api;
import com.findmysalon.model.Customer;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface UserApi {
    @POST("api/user/signup/")
    Call<Customer> customerSignUp(@Body Customer customer);

    @FormUrlEncoded
    @POST("api/user/signin/")
    Call<JsonObject> userSignIn(
            @Field("email") String email,
            @Field("password") String address);

    @FormUrlEncoded
    @POST("api/user/token/verify/")
    Call<JsonObject> verifyAccessToken(
            @Field("access") String accessToken);

    //@Headers("Content-Type: application/json")
    @FormUrlEncoded
    @POST("api/user/token/refresh/")
    Call<JsonObject> getNewAccessToken(
            @Field("refresh") String refreshToken);

}
