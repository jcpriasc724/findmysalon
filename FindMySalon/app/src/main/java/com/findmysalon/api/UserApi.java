package com.findmysalon.api;
import android.database.Observable;

import com.findmysalon.model.Booking;
import com.findmysalon.model.Business;
import com.findmysalon.model.BusinessProfile;
import com.findmysalon.model.Customer;
import com.findmysalon.model.CustomerProfile;
import com.findmysalon.model.Service;
import com.findmysalon.model.Staff;
import com.findmysalon.model.Token;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

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

    @Headers({"NonAuthentication:True"})
    @Multipart
    @POST("api/user/signup/")
    Call<ResponseBody>  businessSignUp(
            @Part("store_name") RequestBody storeName,
            @Part("business_type") RequestBody businessType,
            @Part("email") RequestBody email,
            @Part("password") RequestBody password,
            @Part("phone") RequestBody phone,
            @Part("address") RequestBody address,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude,
            @Part("user_type") RequestBody userType,
            @Part MultipartBody.Part profilePhoto
    );

    @GET("api/user/signup/")
    Call<Staff> staffDetail(@Path("id") int id);

    /*@Headers({"NonAuthentication:True"})
    @POST("api/user/signup/")
    Call<Customer> customerSignUp(@Body Customer customer);*/

    /*@Headers({"NonAuthentication:True"})
    @POST("api/user/signup/")
    Call<Business> businessSignUp(@Body Business business);*/

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

    @GET("api/user/details/")
    Call<CustomerProfile> getUserDetails();

    @GET("api/user/details/")
    Call<BusinessProfile> getBusinessDetails();

    /*@PATCH("api/user/update/")
    Call<CustomerProfile> customerUpdate(@Body CustomerProfile customerProfile);*/

    @Multipart
    @PATCH("api/user/update/")
    Call<ResponseBody>  customerUpdate(
            @Part("first_name") RequestBody firstName,
            @Part("last_name") RequestBody lastName,
            @Part("email") RequestBody email,
            @Part("phone") RequestBody phone,
            @Part("address") RequestBody address,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude,
            @Part("user_type") RequestBody userType,
            @Part MultipartBody.Part profilePhoto
    );

    /*@PATCH("api/user/update/")
    Call<BusinessProfile> businessUpdate(@Body BusinessProfile businessProfile);*/

    @Multipart
    @PATCH("api/user/update/")
    Call<ResponseBody> businessUpdate(
            @Part("store_name") RequestBody storeName,
            @Part("business_type") RequestBody businessType,
            @Part("email") RequestBody email,
            @Part("phone") RequestBody phone,
            @Part("address") RequestBody address,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude,
            @Part("user_type") RequestBody userType,
            @Part MultipartBody.Part profilePhoto
    );

    @FormUrlEncoded
    @PATCH("api/user/change_password/")
    Call<JsonObject> changePassword(
            @Field("current_password") String currentPassword,
            @Field("new_password") String newPassword
            );

    @Multipart
    @POST("api/user/update_profile_photo")
    Call<JsonObject> updateProfilePhoto(@Part MultipartBody.Part image);

}
