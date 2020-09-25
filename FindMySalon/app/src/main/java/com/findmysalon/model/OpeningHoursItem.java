package com.findmysalon.model;

public class OpeningHoursItem {

    private String weekDay;
    private String hours;

    public OpeningHoursItem() {
    }

    public OpeningHoursItem(String weekDay, String hours) {
        this.weekDay = weekDay;
        this.hours = hours;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }
}
