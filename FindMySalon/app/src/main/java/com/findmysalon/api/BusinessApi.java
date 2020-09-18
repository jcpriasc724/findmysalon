package com.findmysalon.api;

import com.findmysalon.model.Business;
import com.findmysalon.model.BusinessProfile;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface BusinessApi {

    @FormUrlEncoded
    @POST("api/user/business_listing/")
    Call<ArrayList<BusinessProfile>> businessList(
            @Field("business_type") String businessType,
            @Field("latitude") double latitude,
            @Field("longitude") double longitude,
            @Field("distance") int radius,
            @Field("budget") int price,
            @Field("language") int languageId,
            @Field("category_id") int categoryId,
            @Field("keyword") String keyword
            );
}
