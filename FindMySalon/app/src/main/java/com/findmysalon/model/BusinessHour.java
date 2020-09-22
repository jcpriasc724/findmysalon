package com.findmysalon.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class BusinessHour {

    @SerializedName("day")
    private String day;
    @SerializedName("opening_time_hour")
    private String openingHour;
    @SerializedName("closing_time_hour")
    private String closingHour;
    @SerializedName("opening_time_min")
    private String openingMin;
    @SerializedName("closing_time_min")
    private String closingMin;
    @SerializedName("status")
    private int status;

    public BusinessHour(String day, String openingHour, String closingHour, String openingMin, String closingMin, int status) {
        this.day = day;
        this.openingHour = openingHour;
        this.closingHour = closingHour;
        this.openingMin = openingMin;
        this.closingMin = closingMin;
        this.status = status;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getOpeningHour() {
        return openingHour;
    }

    public void setOpeningHour(String openingHour) {
        this.openingHour = openingHour;
    }

    public String getClosingHour() {
        return closingHour;
    }

    public void setClosingHour(String closingHour) {
        this.closingHour = closingHour;
    }

    public String getOpeningMin() {
        return openingMin;
    }

    public void setOpeningMin(String openingMin) {
        this.openingMin = openingMin;
    }

    public String getClosingMin() {
        return closingMin;
    }

    public void setClosingMin(String closingMin) {
        this.closingMin = closingMin;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
