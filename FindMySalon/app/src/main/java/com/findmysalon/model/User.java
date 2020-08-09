package com.findmysalon.model;

public class User {

    private int userId;
    private String email;
    private String phone;
    private String address;
    private double lat;
    private double lng;
    private String userType;
    //private String profilePhoto;

    public User(String email, String phone, String address, double lat, double lng, String userType){
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.userType = userType;
        //this.profilePhoto = profilePhoto;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    /*public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }*/
}
