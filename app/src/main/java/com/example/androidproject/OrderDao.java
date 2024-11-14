package com.example.androidproject;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface OrderDao {
    @Insert
    void insertOrder(Order order);
    @Delete
    void deleteOrder(Order order);
    @Query("SELECT * FROM orders WHERE userId = :userId")
    List<Order> getOrdersByUserId(int userId);
}