package com.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.myapplication.model.Friend;
import com.example.myapplication.model.FriendDao;
import com.example.myapplication.model.FriendDatabase;

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
        Friend f1 = new Friend("12345678", "name", 15, 15, 1);
        Friend f2 = new Friend("87654321", "name2", 17, -15, 1);

        long id1 = dao.upsert(f1);
        long id2 = dao.upsert(f2);

        assertNotEquals(id1,id2);
    }

    @Test
    public void testGet(){
        Friend f1 = new Friend("12345678", "name", 15, 15, 1);
        long id = dao.upsert(f1);

        Friend f = dao.get("12345678");
        assertEquals(f.longitude, f1.longitude,0);
        assertEquals(f.latitude, f1.latitude,0);
        assertEquals(f.label, f1.label);
    }

    @Test
    public void testDelete(){
        Friend f1 = new Friend("12345678", "name", 15, 15, 1);
        long id = dao.upsert(f1);

        Friend f = dao.get("12345678");
        int itemsDeleted = dao.delete(f);
        assertEquals(1, itemsDeleted);
        assertNull(dao.get("12345678"));
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }
}
