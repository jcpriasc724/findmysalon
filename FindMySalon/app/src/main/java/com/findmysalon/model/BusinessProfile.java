package com.findmysalon.model;

import com.google.gson.annotations.SerializedName;

public class BusinessProfile {

    @SerializedName("store_name")
    private String businessName;
    @SerializedName("business_type")
    private String businessType;
    @SerializedName("email")
    private String email;
    @SerializedName("address")
    private String address;
    @SerializedName("latitude")
    private double latitude;
    @SerializedName("longitude")
    private double longitude;
    @SerializedName("phone")
    private String phone;
    @SerializedName("user_type")
    private String userType;
    @SerializedName("profile_photo")
    private String profilePhoto;

    public BusinessProfile(String businessName, String businessType, String email, String address, double latitude, double longitude, String phone, String userType) {
        this.businessName = businessName;
        this.businessType = businessType;
        this.email = email;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.phone = phone;
        this.userType = userType;
    }

    public String getBusinessName() {
        return businessName;
    }

    public String getBusinessType() {
        return businessType;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getPhone() {
        return phone;
    }

    public String getUserType() {
        return userType;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }
}
