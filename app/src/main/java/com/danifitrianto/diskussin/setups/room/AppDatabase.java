package com.danifitrianto.diskussin.setups.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.danifitrianto.diskussin.models.Rooms;
import com.danifitrianto.diskussin.models.database.RentingsEntity;
import com.danifitrianto.diskussin.models.database.RoomsEntity;
import com.danifitrianto.diskussin.setups.room.dao.RentingDao;
import com.danifitrianto.diskussin.setups.room.dao.RoomDao;

@Database(entities = {
        RoomsEntity.class,
        RentingsEntity.class
}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract RoomDao roomDao();
    public abstract RentingDao rentingDao();
}
