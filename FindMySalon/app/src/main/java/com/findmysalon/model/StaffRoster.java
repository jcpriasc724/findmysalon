package com.findmysalon.model;

import java.util.ArrayList;
import java.util.Date;

public class StaffRoster {


    private Date dateRoster;
    private ArrayList<String> hoursAvailable;

    public StaffRoster() {
    }

    public StaffRoster(Date dateRoster, ArrayList<String> hoursAvailable) {
        this.dateRoster = dateRoster;
        this.hoursAvailable = hoursAvailable;
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
