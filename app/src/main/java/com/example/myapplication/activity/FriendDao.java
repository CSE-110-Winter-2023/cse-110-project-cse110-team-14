package com.example.myapplication.activity;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Query;
import androidx.room.Upsert;

import com.example.myapplication.Friend;

import java.util.List;

@Dao
public abstract class FriendDao {

    @Upsert
    public abstract long upsert(Friend friend);

    @Query("SELECT EXISTS(SELECT 1 FROM friends WHERE public_code = :publicCode)")
    public abstract boolean exists(String publicCode);

    @Query("SELECT * FROM friends WHERE public_code = :publicCode")
    public abstract LiveData<Friend> get(String publicCode);

    @Query("SELECT * FROM friends ORDER BY label")
    public abstract LiveData<List<Friend>> getAll();

    @Delete
    public abstract int delete(Friend friend);
}
