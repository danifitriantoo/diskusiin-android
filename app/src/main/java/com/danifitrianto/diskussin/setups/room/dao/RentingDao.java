package com.danifitrianto.diskussin.setups.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.danifitrianto.diskussin.models.database.RentingsEntity;

import java.util.List;

@Dao
public interface RentingDao {
    @Query("SELECT * FROM rentingsentity LIMIT 1")
    List<RentingsEntity> getAll();

    @Insert
    void insert(RentingsEntity rentings);
}
