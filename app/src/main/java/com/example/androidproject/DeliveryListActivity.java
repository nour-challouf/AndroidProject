package com.example.androidproject;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.room.Room;
import com.example.androidproject.AppDatabase;
import com.example.androidproject.Delivery;
import com.example.androidproject.DeliveryAdapter;

import java.util.List;

public class DeliveryListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DeliveryAdapter deliveryAdapter;
    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_list); // Set the layout

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerViewDeliveries);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Set layout manager

        // Initialize AppDatabase instance directly using Room.databaseBuilder
        appDatabase = Room.databaseBuilder(this, AppDatabase.class, "app_database")
                .fallbackToDestructiveMigration() // Handles migration if schema changes
                .build();

        // Observe the deliveries from the database
        appDatabase.deliveryDao().getAllDeliveries().observe(this, new Observer<List<Delivery>>() {
            @Override
            public void onChanged(List<Delivery> deliveries) {
                // Create and set the adapter with the new list
                if (deliveryAdapter == null) {
                    deliveryAdapter = new DeliveryAdapter(DeliveryListActivity.this, deliveries);
                    recyclerView.setAdapter(deliveryAdapter);  // Set the adapter only once
                } else {
                    deliveryAdapter.updateDeliveryList(deliveries);  // Update the list if adapter already set
                }
            }
        });
    }
}
