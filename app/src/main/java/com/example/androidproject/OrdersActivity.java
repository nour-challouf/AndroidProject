package com.example.androidproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;

public class OrdersActivity extends AppCompatActivity {

    private ListView ordersListView;
    private List<Order> orders;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        ordersListView = findViewById(R.id.ordersListView);
        userId = getIntent().getIntExtra("userId", -1);

        loadOrders();
    }

    private void loadOrders() {
        AppDatabase appDatabase = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase();
        OrderDao orderDao = appDatabase.orderDao();

        Executors.newSingleThreadExecutor().execute(() -> {
            orders = orderDao.getOrdersByUserId(userId);
            runOnUiThread(() -> {
                ArrayList<HashMap<String, String>> ordersMap = new ArrayList<>();
                for (Order order : orders) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("orderDetails", order.getOrderDetails());
                    ordersMap.add(map);
                }

                SimpleAdapter adapter = new SimpleAdapter(
                        this,
                        ordersMap,
                        R.layout.order_item,
                        new String[]{"orderDetails"},
                        new int[]{R.id.orderDetails}
                ) {
                    @Override
                    public View getView(int position, View convertView, android.view.ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        Button deleteButton = view.findViewById(R.id.deleteButton);
                        deleteButton.setOnClickListener(v -> {
                            Order order = orders.get(position);
                            Executors.newSingleThreadExecutor().execute(() -> {
                                orderDao.deleteOrder(order);
                                runOnUiThread(() -> {
                                    orders.remove(position);
                                    loadOrders();
                                    Toast.makeText(OrdersActivity.this, "Order removed", Toast.LENGTH_SHORT).show();
                                });
                            });
                        });
                        return view;
                    }
                };

                ordersListView.setAdapter(adapter);
            });
        });
    }
}