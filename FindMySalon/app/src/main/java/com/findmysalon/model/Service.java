package com.findmysalon.model;

import java.util.Date;

public class Service {

    private String nameService;
    private Category category;
    private Double price;
    private Long duration;
    private String description;
    private Date createdAt;
    private Date updatedAt;

    public Service() {

    }

    public Service(String nameService, Category category, Double price, Long duration, String description) {
        this.nameService = nameService;
        this.category = category;
        this.price = price;
        this.duration = duration;
        this.description = description;
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
}
