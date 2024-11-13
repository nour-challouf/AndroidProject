package com.example.androidproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;

public class CartActivity extends AppCompatActivity {

    private ListView cartListView;
    private Button placeOrderButton;
    private TextView totalAmountTextView;
    private List<MenuItem> cartItems;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartListView = findViewById(R.id.cartListView);
        placeOrderButton = findViewById(R.id.placeOrderButton);
        totalAmountTextView = findViewById(R.id.totalAmountTextView);

        // Retrieve cart items from CartManager
        cartItems = CartManager.getInstance().getCartItems();
        userId = getIntent().getIntExtra("userId", -1);

        loadCartItems();

        placeOrderButton.setOnClickListener(v -> placeOrder());
    }

    private void loadCartItems() {
        ArrayList<HashMap<String, String>> cartItemsMap = new ArrayList<>();
        double totalAmount = 0.0;

        if (cartItems != null) {
            for (MenuItem menuItem : cartItems) {
                HashMap<String, String> map = new HashMap<>();
                map.put("name", menuItem.menuItem);
                map.put("price", String.format("%.2f D", menuItem.price));
                cartItemsMap.add(map);
                totalAmount += menuItem.price;
            }
        }

        totalAmountTextView.setText(String.format("Total: %.2f D", totalAmount));

        SimpleAdapter adapter = new SimpleAdapter(
                this,
                cartItemsMap,
                R.layout.cart_item,
                new String[]{"name", "price"},
                new int[]{R.id.cartItemName, R.id.cartItemPrice}
        ) {
            @Override
            public View getView(int position, View convertView, android.view.ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                Button deleteButton = view.findViewById(R.id.deleteButton);
                deleteButton.setOnClickListener(v -> {
                    cartItems.remove(position);
                    loadCartItems();
                    Toast.makeText(CartActivity.this, "Item removed from cart", Toast.LENGTH_SHORT).show();
                });
                return view;
            }
        };

        cartListView.setAdapter(adapter);
    }

    private void placeOrder() {
        // Save the order in the database
        AppDatabase appDatabase = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase();
        OrderDao orderDao = appDatabase.orderDao();
        Executors.newSingleThreadExecutor().execute(() -> {
            if (cartItems != null && !cartItems.isEmpty() && userId != -1) {
                Order order = new Order(userId, cartItems);
                orderDao.insertOrder(order);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Order placed successfully", Toast.LENGTH_SHORT).show();
                    finish();
                });
            } else {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}