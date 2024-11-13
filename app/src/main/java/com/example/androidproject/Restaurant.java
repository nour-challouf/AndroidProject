package com.example.androidproject;

public class Restaurant {
    private int id;
    private String name;

    // Constructeur
    public Restaurant(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}