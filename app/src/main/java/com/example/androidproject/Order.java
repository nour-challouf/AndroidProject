package com.example.androidproject;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;
import androidx.room.TypeConverters;

import java.util.List;

@Entity(tableName = "orders",
        foreignKeys = @ForeignKey(entity = User.class,
                parentColumns = "id",
                childColumns = "userId",
                onDelete = ForeignKey.CASCADE))
@TypeConverters(MenuItemConverter.class)
public class Order {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int userId;
    public List<MenuItem> items;

    public Order(int userId, List<MenuItem> items) {
        this.userId = userId;
        this.items = items;
    }

    public String getOrderDetails() {
        StringBuilder details = new StringBuilder();
        double total = 0.0;
        for (MenuItem item : items) {
            details.append(item.menuItem).append(" - ").append(String.format("%.2f D", item.price)).append("\n");
            total += item.price;
        }
        details.append("Total: ").append(String.format("%.2f D", total));
        return details.toString();
    }
}