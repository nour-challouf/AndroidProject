package com.example.androidproject;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RestaurantDao {
    @Insert
    void insertRestaurant(Restaurant restaurant);
    @Query("SELECT * FROM restaurants WHERE id = :restaurantId")
    Restaurant getRestaurantById(int restaurantId);

    @Insert
    void insertMenuItem(MenuItem menuItem);

    @Query("SELECT * FROM menu WHERE restaurantId = :restaurantId")
    List<MenuItem> getMenuItemsForRestaurant(int restaurantId);

    @Query("SELECT * FROM restaurants")
    List<Restaurant> getAllRestaurants();
}
