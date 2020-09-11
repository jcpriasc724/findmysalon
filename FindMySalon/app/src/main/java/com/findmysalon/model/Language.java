package com.findmysalon.model;

import com.google.gson.annotations.SerializedName;

public class Language {

    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;

    public Language(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
