package com.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.myapplication.Friend;
import com.example.myapplication.activity.FriendDatabase;
import com.example.myapplication.activity.FriendDao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;


@RunWith(AndroidJUnit4.class)
public class FriendDBTest {
    private FriendDao dao;
    private FriendDatabase db;

    @Before
    public void createDb(){
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, FriendDatabase.class)
                .allowMainThreadQueries()
                .build();
        dao = db.getDao();
    }

    @Test
    public void testInsert(){
        Friend item1 = new Friend("d95a217f-8ab3-4d8e-b254-4f759e5fdb26", "John", 14,14,14);
        dao.upsert(item1);
        assertTrue(dao.exists(item1.public_code));
    }

    @Test
    public void testGet(){
        Friend friend = new Friend("d95a217f-8ab3-4d8e-b254-4f759e5fdb26", "John", 14,14,14);
        dao.upsert(friend);
        Friend insertedFriend = dao.get(friend.public_code);
        assertEquals(friend.public_code, insertedFriend.public_code);
    }

    @Test
    public void testUpdateName(){
        Friend friend = new Friend("d95a217f-8ab3-4d8e-b254-4f759e5fdb26", "John", 14,14,14);
        dao.upsert(friend);
        friend.setLabel("Joe");
        assertTrue(dao.exists(friend.public_code));
        assertEquals("d95a217f-8ab3-4d8e-b254-4f759e5fdb26", friend.public_code);
        assertEquals("Joe", friend.label);
        assertEquals(14, friend.latitude, 0);
        assertEquals(14, friend.longitude, 0);
    }

    @Test
    public void testUpdateLocation(){
        Friend friend = new Friend("d95a217f-8ab3-4d8e-b254-4f759e5fdb26", "John", 14,14,14);
        dao.upsert(friend);
        friend.setLongitude(20);
        friend.setLatitude(25);
        friend.setVersion(30);
        assertTrue(dao.exists(friend.public_code));
        assertEquals("d95a217f-8ab3-4d8e-b254-4f759e5fdb26", friend.public_code);
        assertEquals("John", friend.label);
        assertEquals(20, friend.longitude, 0);
        assertEquals(25, friend.latitude, 0);
        assertEquals(30, friend.version, 0);
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }
}

