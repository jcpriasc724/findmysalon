package com.findmysalon.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;

public class Staff {

    @SerializedName("id")
    private int id;
    private Business business;
    @SerializedName("name")
    private String name;
    @SerializedName("service_category_id")
    private int category_id;
    @SerializedName("service_category")
    private Category category;
    @SerializedName("phone")
    private String phoneNumber;
    private String email;
    @SerializedName("image")
    private String image;
    private float rating;
    @SerializedName("created_at")
    private Date createdAt;
    @SerializedName("updated_at")
    private Date updatedAt;
    private ArrayList<StaffRoster> staffRosters;

    public Staff() {
    }

    public Staff(String name, String phoneNumber, String email) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public Staff(String name, float rating, ArrayList<StaffRoster> staffRosters) {
        this.name = name;
        this.rating = rating;
        this.staffRosters = staffRosters;
    }

    public Staff(Business business, String name, Category category, String phoneNumber, String email) {
        this.business = business;
        this.name = name;
        this.category = category;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public Staff(int id, String name, Category category, String phoneNumber, String image, Date createdAt, Date updatedAt) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.phoneNumber = phoneNumber;
        this.image = image;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Create Object
     */
    public Staff(int category_id, String name,  String phoneNumber) {
        this.category_id = category_id;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public ArrayList<StaffRoster> getStaffRosters() {
        return staffRosters;
    }

    public void setStaffRosters(ArrayList<StaffRoster> staffRosters) {
        this.staffRosters = staffRosters;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id;}

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
