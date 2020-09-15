package com.findmysalon.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface FavouriteApi {

    @FormUrlEncoded
    @POST("/favourite/")
    Call<ResponseBody> addFavouriteBusiness(
            @Field("business_id") int businessId
    );
}
