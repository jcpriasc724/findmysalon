package com.findmysalon.model;

import com.google.gson.annotations.SerializedName;

public class Token {

    @SerializedName("refresh")
    private String refresh;
    @SerializedName("access")
    private String access;
    @SerializedName("user_type")
    private String userType;

    public Token() {

    }

    public Token(String refresh, String access, String userType) {
        this.refresh = refresh;
        this.access = access;
        this.userType = userType;
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

