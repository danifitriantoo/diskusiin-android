package com.danifitrianto.diskussin.models;

import com.google.gson.annotations.SerializedName;

public class Users {
    private String nim,password;

    public Users(String nim, String password) {
        this.nim = nim;
        this.password = password;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
