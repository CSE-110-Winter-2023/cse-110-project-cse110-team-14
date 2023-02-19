package com.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.myapplication.LocationDatabase;
import com.example.myapplication.LocationItem;
import com.example.myapplication.LocationItemDao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class LocationDBTest {
    private LocationItemDao dao;
    private LocationDatabase db;

    @Before
    public void createDb(){
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, LocationDatabase.class)
                .allowMainThreadQueries()
                .build();
        dao = db.locationItemDao();
    }

    @Test
    public void testInsert(){
        LocationItem item1 = new LocationItem(14, 14, "fourteen");
        LocationItem item2 = new LocationItem(20, 20, "twenty");

        long id1 = dao.insert(item1);
        long id2 = dao.insert(item2);

        assertNotEquals(id1,id2);
    }

    @Test
    public void testGet(){
        LocationItem insertedItem = new LocationItem(14, 14, "fourteen");
        long id = dao.insert(insertedItem);

        LocationItem item = dao.get(id);
        assertEquals(id, item.id);
        assertEquals(insertedItem.longitude, item.longitude,0);
        assertEquals(insertedItem.latitude, item.latitude,0);
        assertEquals(insertedItem.label, item.label);
    }

    @Test
    public void testUpdate(){
        LocationItem item = new LocationItem(14, 14, "fourteen");
        long id = dao.insert(item);

        item = dao.get(id);
        item.longitude = 15;
        int itemsUpdated = dao.update(item);
        assertEquals(1, itemsUpdated,0);

        item = dao.get(id);
        assertNotNull(item);
        assertEquals(15, item.longitude,0);
    }

    @Test
    public void testDelete(){
        LocationItem item = new LocationItem(14, 14, "fourteen");
        long id = dao.insert(item);

        item = dao.get(id);
        int itemsDeleted = dao.delete(item);
        assertEquals(1, itemsDeleted);
        assertNull(dao.get(id));
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }
}
