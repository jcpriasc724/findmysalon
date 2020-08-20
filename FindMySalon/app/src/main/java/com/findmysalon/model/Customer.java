package com.findmysalon.model;

import com.google.gson.annotations.SerializedName;

public class Customer extends User {

    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;

    public Customer(String firstName, String lastName, String email, String password, String phone, String address, double lat, double lng, String userType) {
        super(email, password, phone, address, lat, lng, userType);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Customer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
