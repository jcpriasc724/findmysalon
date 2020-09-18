package com.findmysalon.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class FilterParameters implements Parcelable {

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

    protected FilterParameters(Parcel in) {
        businessTypeSelected = in.readString();
        lat = in.readDouble();
        lng = in.readDouble();
        distanceSelected = in.readInt();
        budgetSelected = in.readInt();
        languageSelected = in.createStringArrayList();
        categoryIdSelected = in.readInt();
        keywordTyped = in.readString();
    }

    public static final Creator<FilterParameters> CREATOR = new Creator<FilterParameters>() {
        @Override
        public FilterParameters createFromParcel(Parcel in) {
            return new FilterParameters(in);
        }

        @Override
        public FilterParameters[] newArray(int size) {
            return new FilterParameters[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(businessTypeSelected);
        dest.writeDouble(lat);
        dest.writeDouble(lng);
        dest.writeInt(distanceSelected);
        dest.writeInt(budgetSelected);
        dest.writeStringList(languageSelected);
        dest.writeInt(categoryIdSelected);
        dest.writeString(keywordTyped);
    }
}
