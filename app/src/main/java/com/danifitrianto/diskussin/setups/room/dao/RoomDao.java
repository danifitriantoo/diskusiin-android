package com.danifitrianto.diskussin.setups.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.danifitrianto.diskussin.models.Rooms;
import com.danifitrianto.diskussin.models.database.RoomsEntity;

import java.util.List;

@Dao
public interface RoomDao {
    @Query("SELECT * FROM RoomsEntity")
    List<RoomsEntity> getAll();

    @Insert
    void insert(List<RoomsEntity> model);

    @Delete
    void delete(RoomsEntity model);

    @Update
    void update(RoomsEntity model);
}
