package com.findmysalon.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;

public class StaffAvailable {

    @SerializedName("staff")
    private Staff staff;

    @SerializedName("datesList")
    private ArrayList<StaffRoster> staffRosters;

    public StaffAvailable(Staff staff, ArrayList<StaffRoster> staffRosters) {
        this.staff = staff;
        this.staffRosters = staffRosters;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }


    public ArrayList<StaffRoster> getStaffRosters() {
        return staffRosters;
    }

    public void setStaffRosters(ArrayList<StaffRoster> staffRosters) {
        this.staffRosters = staffRosters;
    }

}
