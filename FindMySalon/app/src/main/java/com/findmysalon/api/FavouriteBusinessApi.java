package com.findmysalon.api;

import com.findmysalon.model.BusinessProfile;
import com.findmysalon.model.FavouriteBusinessProfile;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface FavouriteBusinessApi {

    @FormUrlEncoded
    @POST("/favourite/")
    Call<ResponseBody> addFavouriteBusiness(
            @Field("business_id") int businessId
    );

    @GET("/favourite/")
    Call<ArrayList<BusinessProfile>> getFavouriteBusiness();

    @GET("/favourite/{id}/details/")
    Call<FavouriteBusinessProfile> isFavouriteBusiness(@Path("id") int id);

    @DELETE("/favourite/{id}/")
    Call<ResponseBody> deleteFavouriteBusiness(@Path("id") int id);
}
