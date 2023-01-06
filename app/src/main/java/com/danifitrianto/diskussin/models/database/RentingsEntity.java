package com.danifitrianto.diskussin.models.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class RentingsEntity implements Serializable {
    @PrimaryKey()
    private int id;

    @ColumnInfo(name = "room_id")
    private int roomId;

    @ColumnInfo(name = "user_id")
    private int userId;

    @ColumnInfo(name = "keperluan")
    private String keperluan;

    @ColumnInfo(name = "tanggal")
    private String tanggal;

    @ColumnInfo(name = "mulai")
    private String mulai;

    @ColumnInfo(name = "durasi")
    private int durasi;

    @ColumnInfo(name = "jumlah_orang")
    private int jumlahOrang;

    @ColumnInfo(name = "status")
    private int status;

    public RentingsEntity(int id, int roomId, int userId, String keperluan, String tanggal, String mulai, int durasi, int jumlahOrang,int status) {
        this.id = id;
        this.roomId = roomId;
        this.userId = userId;
        this.keperluan = keperluan;
        this.tanggal = tanggal;
        this.mulai = mulai;
        this.durasi = durasi;
        this.jumlahOrang = jumlahOrang;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
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

    public String getMulai() {
        return mulai;
    }

    public void setMulai(String mulai) {
        this.mulai = mulai;
    }

    public int getDurasi() {
        return durasi;
    }

    public void setDurasi(int durasi) {
        this.durasi = durasi;
    }

    public int getJumlahOrang() {
        return jumlahOrang;
    }

    public void setJumlahOrang(int jumlahOrang) {
        this.jumlahOrang = jumlahOrang;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
