package com.findmysalon.api;
import android.database.Observable;

import com.findmysalon.model.Business;
import com.findmysalon.model.Customer;
import com.findmysalon.model.Token;
import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UserApi {
    @Headers({"NonAuthentication:True"})
    @Multipart
    @POST("api/user/signup/")
    Call<ResponseBody>  customerSignUp(
            @Part("first_name") RequestBody firstName,
            @Part("last_name") RequestBody lastName,
            @Part("email") RequestBody email,
            @Part("password") RequestBody password,
            @Part("phone") RequestBody phone,
            @Part("address") RequestBody address,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude,
            @Part("user_type") RequestBody userType,
            @Part MultipartBody.Part profilePhoto
    );

    /*@Headers({"NonAuthentication:True"})
    @POST("api/user/signup/")
    Call<Customer> customerSignUp(@Body Customer customer);*/

    @Headers({"NonAuthentication:True"})
    @POST("api/user/signup/")
    Call<Business> businessSignUp(@Body Business business);

    @Headers({"Content-type:application/json;charset=UTF-8","NonAuthentication:True"})
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
