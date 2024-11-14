package com.example.androidproject;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "locations")

public class Location {

    @PrimaryKey(autoGenerate = true)
    private int id;


}
