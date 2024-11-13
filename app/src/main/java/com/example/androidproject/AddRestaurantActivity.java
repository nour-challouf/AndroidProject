package com.example.androidproject;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import java.util.concurrent.Executors;

public class AddRestaurantActivity extends AppCompatActivity {

    private EditText restaurantNameEditText;
    private Button saveButton;
    private AppDatabase appDatabase;
    private RestaurantDao restaurantDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_restaurant);

        restaurantNameEditText = findViewById(R.id.restaurantNameEditText);
        saveButton = findViewById(R.id.saveButton);

        appDatabase = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase();
        restaurantDao = appDatabase.restaurantDao();

        saveButton.setOnClickListener(v -> {
            String restaurantName = restaurantNameEditText.getText().toString().trim();
            if (!restaurantName.isEmpty()) {
                Restaurant restaurant = new Restaurant();
                restaurant.name = restaurantName;
                Executors.newSingleThreadExecutor().execute(() -> {
                    restaurantDao.insertRestaurant(restaurant);
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Restaurant added", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                });
            }
        });
    }
}