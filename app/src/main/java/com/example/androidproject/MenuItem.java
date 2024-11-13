package com.example.androidproject;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "menu",
        foreignKeys = @ForeignKey(entity = Restaurant.class,
                parentColumns = "id",
                childColumns = "restaurantId",
                onDelete = ForeignKey.CASCADE))
public class MenuItem  {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String menuItem;
    public double price;
    public int restaurantId;
    public String imageUrl;

    public MenuItem(String menuItem, double price, int restaurantId) {
        this.menuItem = menuItem;
        this.price = price;
        this.restaurantId = restaurantId;
    }

    protected MenuItem(Parcel in) {
        id = in.readInt();
        menuItem = in.readString();
        price = in.readDouble();
        restaurantId = in.readInt();
        imageUrl = in.readString();
    }

}