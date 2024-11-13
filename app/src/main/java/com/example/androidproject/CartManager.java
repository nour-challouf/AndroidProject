package com.example.androidproject;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private List<MenuItem> cartItems;

    private CartManager() {
        cartItems = new ArrayList<>();
    }

    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public List<MenuItem> getCartItems() {
        return new ArrayList<>(cartItems);
    }

    public void addToCart(MenuItem item) {
        cartItems.add(item);
    }

    public void clearCart() {
        cartItems.clear();
    }
}