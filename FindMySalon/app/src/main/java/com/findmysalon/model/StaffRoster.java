package com.findmysalon.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;

public class StaffRoster {

    @SerializedName("date")
    private Date dateRoster;
    @SerializedName("start_time_hours")
    private String startHour;
    @SerializedName("finish_time_hours")
    private String endHour;
    @SerializedName("start_time_mins")
    private String startMin;
    @SerializedName("finish_time_mins")
    private String endMin;
    @SerializedName("status")
    private int status;

    private ArrayList<String> hoursAvailable;

    public StaffRoster() {
    }

    public StaffRoster(Date dateRoster, ArrayList<String> hoursAvailable) {
        this.dateRoster = dateRoster;
        this.hoursAvailable = hoursAvailable;
    }

    public StaffRoster(Date dateRoster, String startHour, String startMin, String endHour, String endMin, int status) {
        this.dateRoster = dateRoster;
        this.startHour = startHour;
        this.endHour = endHour;
        this.startMin = startMin;
        this.endMin = endMin;
        this.status = status;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public String getStartHour() {
        return startHour;
    }

    public void setStartHour(String startHour) {
        this.startHour = startHour;
    }

    public String getEndHour() {
        return endHour;
    }

    public void setEndHour(String endHour) {
        this.endHour = endHour;
    }

    public String getStartMin() {
        return startMin;
    }

    public void setStartMin(String startMin) {
        this.startMin = startMin;
    }

    public String getEndMin() {
        return endMin;
    }

    public void setEndMin(String endMin) {
        this.endMin = endMin;
    }

    public Date getDateRoster() {
        return dateRoster;
    }

    public void setDateRoster(Date dateRoster) {
        this.dateRoster = dateRoster;
    }

    public ArrayList<String> getHoursAvailable() {
        return hoursAvailable;
    }

    public void setHoursAvailable(ArrayList<String> hoursAvailable) {
        this.hoursAvailable = hoursAvailable;
    }
}
