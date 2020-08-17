package com.findmysalon.model;

import com.google.gson.annotations.SerializedName;

public class Token {

    @SerializedName("refresh")
    private String refresh;
    @SerializedName("access")
    private String access;

    public Token() {

    }

    public Token(String refresh, String access) {
        this.refresh = refresh;
        this.access = access;
    }

    public Token(String access) {
        this.access = access;
    }

    public String getRefresh() {
        return refresh;
    }
    public String getAccess() {return access; }

    public void setRefresh(String  refresh) {
        this.refresh = refresh;
    }

    public void setAccess(String  access) {
        this.access = access;
    }

}

