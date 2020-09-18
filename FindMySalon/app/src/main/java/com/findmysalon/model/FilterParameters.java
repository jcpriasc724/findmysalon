package com.findmysalon.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class FilterParameters implements Serializable {

    private String businessTypeSelected;
    private double lat;
    private double lng;
    private int distanceSelected;
    private int budgetSelected;
    private ArrayList<String> languageSelected;
    private int categoryIdSelected;
    private String keywordTyped;

    public FilterParameters(String businessTypeSelected, double lat, double lng, int distanceSelected, int budgetSelected, ArrayList<String> languageSelected, int categoryIdSelected, String keywordTyped) {
        this.businessTypeSelected = businessTypeSelected;
        this.lat = lat;
        this.lng = lng;
        this.distanceSelected = distanceSelected;
        this.budgetSelected = budgetSelected;
        this.languageSelected = languageSelected;
        this.categoryIdSelected = categoryIdSelected;
        this.keywordTyped = keywordTyped;
    }

    public String getBusinessTypeSelected() {
        return businessTypeSelected;
    }

    public void setBusinessTypeSelected(String businessTypeSelected) {
        this.businessTypeSelected = businessTypeSelected;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public int getDistanceSelected() {
        return distanceSelected;
    }

    public void setDistanceSelected(int distanceSelected) {
        this.distanceSelected = distanceSelected;
    }

    public int getBudgetSelected() {
        return budgetSelected;
    }

    public void setBudgetSelected(int budgetSelected) {
        this.budgetSelected = budgetSelected;
    }

    public ArrayList<String> getLanguageSelected() {
        return languageSelected;
    }

    public void setLanguageSelected(ArrayList<String> languageSelected) {
        this.languageSelected = languageSelected;
    }

    public int getCategoryIdSelected() {
        return categoryIdSelected;
    }

    public void setCategoryIdSelected(int categoryIdSelected) {
        this.categoryIdSelected = categoryIdSelected;
    }

    public String getKeywordTyped() {
        return keywordTyped;
    }

    public void setKeywordTyped(String keywordTyped) {
        this.keywordTyped = keywordTyped;
    }

}
