package com.findmysalon.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;

public class Category {


    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String nameCategory;
    @SerializedName("order")
    private String order;
    private String description;
    @SerializedName("created_at")
    private Date createdAt;
    @SerializedName("updated_at")
    private Date updatedAt;
    private ArrayList<Service> listServices;

    public Category(int id, String nameCategory) {
        this.id = id;
        this.nameCategory = nameCategory;
    }

    public Category(String nameCategory, String order, String description, ArrayList<Service> listServices) {
        this.nameCategory = nameCategory;
        this.order = order;
        this.description = description;
        this.listServices = listServices;
    }

    public Category(int id, String nameCategory, String order, String description, ArrayList<Service> listServices) {
        this.id = id;
        this.nameCategory = nameCategory;
        this.order = order;
        this.description = description;
        this.listServices = listServices;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Service> getListServices() {
        return listServices;
    }

    public void setListServices(ArrayList<Service> listServices) {
        this.listServices = listServices;
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

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    //to display object as a string in spinner
    @Override
    public String toString() {
        return nameCategory;
    }
}
