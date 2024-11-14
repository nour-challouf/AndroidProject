package com.example.androidproject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class RestaurantMainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ListView restaurantListView;
    private Button addRestaurantBtn,addlocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_restaurant);

        dbHelper = new DatabaseHelper(this);
        restaurantListView = findViewById(R.id.restaurantListView);
        addRestaurantBtn = findViewById(R.id.addRestaurantBtn);
        addlocation = findViewById(R.id.addlocation);


        addRestaurantBtn.setOnClickListener(v -> {
            // Ouvrir une nouvelle activité pour ajouter un restaurant
            startActivity(new Intent(RestaurantMainActivity.this, AddRestaurantActivity.class));
        });
        addlocation.setOnClickListener(v -> {
            // Ouvrir une nouvelle activité pour ajouter un restaurant
            startActivity(new Intent(RestaurantMainActivity.this, RestaurantLocationActivity.class));
        });

        loadRestaurants();
    }

    private void loadRestaurants() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_RESTAURANTS,
                new String[]{DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_NAME},
                null, null, null, null, null);

        List<String> restaurantNames = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                restaurantNames.add(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME)));
            } while (cursor.moveToNext());
        }
        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, restaurantNames);
        restaurantListView.setAdapter(adapter);

        restaurantListView.setOnItemClickListener((parent, view, position, id) -> {
            // Ouvrir l'activité du menu pour le restaurant sélectionné
            Intent intent = new Intent(RestaurantMainActivity.this, MenuActivity.class);
            intent.putExtra("restaurant_id", id);
            startActivity(intent);
        });
    }
}
