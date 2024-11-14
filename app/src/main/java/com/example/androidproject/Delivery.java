package com.example.androidproject;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "deliveries")

public class Delivery {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private double distance ;

    private double price;

    public Delivery(double distance, double price) {
        this.distance = distance;
        this.price = price;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    private String username;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
