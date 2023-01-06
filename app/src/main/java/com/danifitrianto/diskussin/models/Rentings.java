package com.danifitrianto.diskussin.models;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;


public class Rentings {
    @SerializedName("id")
    private int id;

    @SerializedName("room_Id")
    private int room_id;

    @SerializedName("user_Id")
    private int userId;

    @SerializedName("keperluan")
    private String keperluan;

    @SerializedName("tanggal")
    private String tanggal;

    @SerializedName("mulai")
    private String waktu;

    @SerializedName("durasi")
    private int durasi;

    @SerializedName("jumlah_Orang")
    private int jumlah_orang;

    @SerializedName("status")
    private int status;

    /* to json */
    public Rentings(@Nullable int id,
                    int room_id,
                    int userId,
                    String keperluan,
                    String tanggal,
                    String waktu,
                    int durasi,
                    int jumlah_orang,
                    int status) {
        this.id = id;
        this.room_id = room_id;
        this.userId = userId;
        this.keperluan = keperluan;
        this.tanggal = tanggal;
        this.waktu = waktu;
        this.durasi = durasi;
        this.jumlah_orang = jumlah_orang;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getKeperluan() {
        return keperluan;
    }

    public void setKeperluan(String keperluan) {
        this.keperluan = keperluan;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public int getDurasi() {
        return durasi;
    }

    public void setDurasi(int durasi) {
        this.durasi = durasi;
    }

    public int getJumlah_orang() {
        return jumlah_orang;
    }

    public void setJumlah_orang(int jumlah_orang) {
        this.jumlah_orang = jumlah_orang;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
