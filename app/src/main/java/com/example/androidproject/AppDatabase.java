package com.example.androidproject;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {User.class,Delivery.class}, version = 1)
@Database(entities = {User.class,Restaurant.class, MenuItem.class,Order.class,Delivery.class}, version = 3)
@TypeConverters({MenuItemConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract RestaurantDao restaurantDao();
    public abstract MenuItemDao menuItemDao();
    public abstract OrderDao orderDao();
        public abstract DeliveryDao deliveryDao();
}