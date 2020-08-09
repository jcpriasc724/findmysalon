package com.findmysalon.model;

public class Customer extends User {

    private String firstName;
    private String lastName;

    public Customer(String firstName, String lastName, String email, String phone, String address, double lat, double lng, String userType) {
        super(email, phone, address, lat, lng, userType);
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
