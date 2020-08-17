package com.findmysalon.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Service {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String nameService;
    @SerializedName("category")
    private Category category;
    @SerializedName("price")
    private Double price;
    @SerializedName("duration")
    private Long duration;
    @SerializedName("display_status")
    private String displayStatus;
    @SerializedName("description")
    private String description;
    @SerializedName("order")
    private int order;
    @SerializedName("created_at")
    private Date createdAt;
    @SerializedName("updated_at")
    private Date updatedAt;

    public Service() {

    }

    public Service(int id, String nameService, Category category, Double price, Long duration, String description,String displayStatus, int order) {
        this.id = id;
        this.nameService = nameService;
        this.category = category;
        this.price = price;
        this.duration = duration;
        this.description = description;
        this.displayStatus = displayStatus;
        this.order = order;
    }

    public Service(String nameService, Double price, Long duration, String description) {
        this.nameService = nameService;
        this.price = price;
        this.duration = duration;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameService() {
        return nameService;
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

    public void setNameService(String nameService) {
        this.nameService = nameService;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplayStatus() {
        return displayStatus;
    }

    public void setDisplayStatus(String displayStatus) {
        this.displayStatus = displayStatus;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
