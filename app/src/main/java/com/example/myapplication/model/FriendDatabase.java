package com.example.myapplication.model;

import android.content.Context;

import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Friend.class}, version = 2, exportSchema = false)
public abstract class FriendDatabase extends RoomDatabase {

    private volatile static FriendDatabase instance = null;

    public abstract FriendDao getDao();

    public synchronized static FriendDatabase provide(Context context) {

        if(instance == null)
            instance = FriendDatabase.make(context);

        return instance;
    }

    private static FriendDatabase make(Context context) {
        return Room.databaseBuilder(context, FriendDatabase.class, "my_friends.db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
    }

    @VisibleForTesting
    public static void inject(FriendDatabase db) {
        if(instance != null)
            instance.close();

        instance = db;
    }
}
