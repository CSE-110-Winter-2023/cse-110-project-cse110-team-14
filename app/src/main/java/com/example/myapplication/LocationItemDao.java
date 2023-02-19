package com.example.myapplication;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface LocationItemDao {
    @Insert
    long insert(LocationItem locItem);

    @Insert
    List<Long> insertAll(List<LocationItem> locListItem);

    @Query("SELECT*FROM `location_items` WHERE `id`=:id")
    LocationItem get(long id);

    @Query("SELECT*FROM `location_items` ORDER BY `label`")
    List<LocationItem> getAll();

    @Update
    int update(LocationItem locItem);

    @Delete
    int delete(LocationItem locItem);

    @Query("DELETE FROM location_items")
    public void nukeTable();
}
