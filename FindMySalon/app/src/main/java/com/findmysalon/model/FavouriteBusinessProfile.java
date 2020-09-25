package com.findmysalon.model;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FavouriteBusinessProfile extends BusinessProfile implements Serializable {
    @SerializedName("is_favourite")
    private boolean isFavourite;

    public FavouriteBusinessProfile(int businessId, String businessName, String address, String phone, float rating, String profilePhoto, boolean isFavourite) {
        super(businessId, businessName, address, phone, rating, profilePhoto);
        this.isFavourite = isFavourite;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }
}
