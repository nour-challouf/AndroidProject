package com.example.androidproject;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MenuItemDao {

    @Insert
    void insertMenuItem(MenuItem menuItem);
    @Query("SELECT * FROM menu WHERE id = :id")
    MenuItem getMenuItemById(int id);

    @Delete
    void deleteMenuItem(MenuItem menuItem);

    @Query("SELECT * FROM menu WHERE restaurantId = :restaurantId")
    List<MenuItem> getMenuItemsForRestaurant(int restaurantId);

    @Query("DELETE FROM menu WHERE id = :id")
    void deleteMenuItemById(int id);
}