package com.findmysalon.api;


import okhttp3.ResponseBody;
import retrofit2.Call;


import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RatingApi {

    @FormUrlEncoded
    @POST("rating/{id}/")
    Call<ResponseBody> submitRating(@Path("id") int id,
                                    @Field("rating") Float rating);


}
