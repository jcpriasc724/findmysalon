package com.findmysalon.model;

import java.util.Date;

public class Booking {

    private Date dateBooking;
    private String startTime;
    private String endTime;
    private Customer customer;
    private Service service;
    private Staff staff;
    private Business business;

    public Booking() {
    }

    public Booking(Date dateBooking, String startTime, String endTime, Customer customer, Service service, Staff staff, Business business) {
        this.dateBooking = dateBooking;
        this.startTime = startTime;
        this.endTime = endTime;
        this.customer = customer;
        this.service = service;
        this.staff = staff;
        this.business = business;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    public Date getDateBooking() {
        return dateBooking;
    }

    public void setDateBooking(Date dateBooking) {
        this.dateBooking = dateBooking;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }
}
