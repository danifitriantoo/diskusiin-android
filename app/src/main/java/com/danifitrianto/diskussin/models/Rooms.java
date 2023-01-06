package com.danifitrianto.diskussin.models;

import com.google.gson.annotations.SerializedName;

public class Rooms {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("facility")
    private String facility;

    @SerializedName("status")
    private boolean status;

    public Rooms(int id, String name, String facilities, boolean status) {
        this.id = id;
        this.name = name;
        this.facility = facilities;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFacility() {
        return facility;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
