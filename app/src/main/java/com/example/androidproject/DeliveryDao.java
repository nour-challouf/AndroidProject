package com.example.androidproject;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DeliveryDao {

    @Insert
    void insert(Delivery delivery);
    @Update
    void updateUser(Delivery delivery);


    @Query("SELECT * FROM deliveries WHERE username = :username")
    List<Delivery> login(String username);

    @Query("SELECT * FROM deliveries")  // Use lowercase table name
    LiveData<List<Delivery>> getAllDeliveries();


}
