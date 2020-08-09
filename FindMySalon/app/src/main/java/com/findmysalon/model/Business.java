package com.findmysalon.model;

public class Business extends User{

    private String storeName;
    private String businessType;
    private String customerCategory;

    public Business(String email, String phone, String address, double lat, double lng, String userType, String storeName, String businessType, String customerCategory) {
        super(email, phone, address, lat, lng, userType);
        this.storeName = storeName;
        this.businessType = businessType;
        this.customerCategory = customerCategory;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getCustomerCategory() {
        return customerCategory;
    }

    public void setCustomerCategory(String customerCategory) {
        this.customerCategory = customerCategory;
    }
}
