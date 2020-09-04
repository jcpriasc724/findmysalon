package com.findmysalon.model;

import com.google.gson.annotations.SerializedName;

public class Token {

    @SerializedName("refresh")
    private String refresh;
    @SerializedName("access")
    private String access;
    @SerializedName("user_id")
    private int userId;
    @SerializedName("user_type")
    private String userType;
    @SerializedName("profile_photo")
    private String profilePhoto;

    public Token() {

    }

    public Token(String refresh, String access, int userId, String profilePhoto, String userType) {
        this.refresh = refresh;
        this.access = access;
        this.userId = userId;
        this.userType = userType;
        this.profilePhoto = profilePhoto;
    }

    public Token(String access) {
        this.access = access;
    }

    public String getRefresh() {
        return refresh;
    }
    public String getAccess() {return access; }
    public String getUserType() {
        return userType;
    }

    public int getUserId() {
        return userId;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public void setRefresh(String  refresh) {
        this.refresh = refresh;
    }

    public void setAccess(String  access) {
        this.access = access;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @Override
    public String toString() {
        return "Token{" +
                "refresh='" + refresh + '\'' +
                ", access='" + access + '\'' +
                ", userType='" + userType + '\'' +
                '}';
    }
}

