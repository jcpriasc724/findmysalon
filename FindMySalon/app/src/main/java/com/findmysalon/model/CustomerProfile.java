package com.findmysalon.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CustomerProfile implements Serializable {

    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
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

    public CustomerProfile(String firstName, String lastName, String email, String address, double latitude, double longitude, String phone, String userType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.phone = phone;
        this.userType = userType;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
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
