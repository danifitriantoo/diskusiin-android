package com.danifitrianto.diskussin.models.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.danifitrianto.diskussin.models.Rooms;

import java.util.List;

@Entity
public class RoomsEntity {

    @PrimaryKey()
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "facility")
    private String facility;

    @ColumnInfo(name = "status")
    private boolean status;

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
