package com.danifitrianto.diskussin.setups.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.danifitrianto.diskussin.models.database.RentingsEntity;

import java.util.List;

@Dao
public interface RentingDao {
    @Query("SELECT * FROM rentingsentity ORDER BY id ASC")
    List<RentingsEntity> getAll();

    @Insert
    void insert(RentingsEntity rentings);

    @Query("UPDATE rentingsentity " +
            "SET status=:status, keperluan=:keperluan, durasi=:durasi, jumlah_orang=:jumlahOrang " +
            "WHERE id=:id")
    void update(int id,int status,String keperluan, int durasi, int jumlahOrang);

    @Query("DELETE FROM rentingsentity WHERE id=:id")
    void deleteRent(int id);
}
