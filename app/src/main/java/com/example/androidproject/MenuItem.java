package com.example.androidproject;

public class MenuItem {
    private String name;
    private double price;
    private String imageUrl;  // URL ou chemin de l'image

    public MenuItem(String name, double price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    // Getter et setter pour le nom, le prix et l'URL de l'image
    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
