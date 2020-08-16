package com.findmysalon.api;
import com.findmysalon.model.Business;
import com.findmysalon.model.Customer;
import com.findmysalon.model.Token;
import com.google.gson.JsonObject;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface UserApi {
    @POST("api/user/signup/")
    Call<Customer> customerSignUp(@Body Customer customer);

    @POST("api/user/signup/")
    Call<Business> businessSignUp(@Body Business business);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("api/user/signin/")
    Call<Token> oathToken(@Body RequestBody login);

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
