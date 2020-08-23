package com.findmysalon.model;

import java.util.ArrayList;
import java.util.Date;

public class StaffRoster {

    private Date dateRoster;
    private int startHour;
    private int endHour;
    private int startMin;
    private int endMin;

    private ArrayList<String> hoursAvailable;

    public StaffRoster() {
    }

    public StaffRoster(Date dateRoster, ArrayList<String> hoursAvailable) {
        this.dateRoster = dateRoster;
        this.hoursAvailable = hoursAvailable;
    }

    public StaffRoster(Date dateRoster) {
        this.dateRoster = dateRoster;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public int getStartMin() {
        return startMin;
    }

    public void setStartMin(int startMin) {
        this.startMin = startMin;
    }

    public int getEndMin() {
        return endMin;
    }

    public void setEndMin(int endMin) {
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
