package com.findmysalon.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Booking {



    @SerializedName("id")
    private int id;
    @SerializedName("booking_date")
    private Date dateBooking;
    @SerializedName("time")
    private String startTime;
    @SerializedName("end_time")
    private String endTime;
    @SerializedName("customer")
    private Customer customer;
    @SerializedName("service")
    private Service service;
    @SerializedName("staff")
    private Staff staff;
    @SerializedName("business")
    private Business business;
    @SerializedName("status")
    private String status;
    @SerializedName("created_at")
    private Date createAt;
    @SerializedName("updated_at")
    private Date updateAt;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

}
