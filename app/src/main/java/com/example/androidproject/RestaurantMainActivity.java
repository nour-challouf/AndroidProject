package com.example.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class RestaurantMainActivity extends AppCompatActivity {

    private ListView restaurantListView;
    private Button addRestaurantBtn;

    private RestaurantDao restaurantDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_restaurant);

        restaurantListView = findViewById(R.id.restaurantListView);
        addRestaurantBtn = findViewById(R.id.addRestaurantBtn);

        AppDatabase appDatabase = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase();
        restaurantDao = appDatabase.restaurantDao();

        addRestaurantBtn.setOnClickListener(v -> {
            // Ouvrir une nouvelle activité pour ajouter un restaurant
            startActivity(new Intent(RestaurantMainActivity.this, AddRestaurantActivity.class));
        });

        loadRestaurants();
    }

    private void loadRestaurants() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Restaurant> restaurants = restaurantDao.getAllRestaurants();
            List<String> restaurantNames = new ArrayList<>();
            for (Restaurant restaurant : restaurants) {
                restaurantNames.add(restaurant.name);
            }

            runOnUiThread(() -> {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, restaurantNames);
                restaurantListView.setAdapter(adapter);

                restaurantListView.setOnItemClickListener((parent, view, position, id) -> {
                    // Ouvrir l'activité du menu pour le restaurant sélectionné
                    Intent intent = new Intent(RestaurantMainActivity.this, MenuActivity.class);
                    intent.putExtra("restaurant_id", restaurants.get(position).id);
                    startActivity(intent);
                });
            });
        });
    }
}