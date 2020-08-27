package com.findmysalon.model;

import com.google.gson.annotations.SerializedName;

public class Business extends User{

    @SerializedName("id")
    private int id;
    @SerializedName("store_name")
    private String storeName;
    @SerializedName("business_type")
    private String businessType;
    private Float rating = 4.5f;

    public Business(String storeName, String businessType, String email, String password, String phone, String address, double lat, double lng, String userType) {
        super(email, password, phone, address, lat, lng, userType);
        this.storeName = storeName;
        this.businessType = businessType;
    }

    public Business(String address, String phone, String storeName) {
        super(address, phone);
        this.storeName = storeName;
    }

    /*public Business(String address, String phone, String storeName, Float rating) {
        super(address, phone);
        this.storeName = storeName;
        this.rating = rating;
    }*/


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    @Override
    public String toString() {
        return "Business{" +
                "storeName='" + storeName + '\'' +
                ", businessType='" + businessType + '\'' +
                ", rating=" + rating +
                '}' + super.toString();
    }
}
