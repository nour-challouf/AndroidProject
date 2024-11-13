package com.example.androidproject;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddRestaurantActivity extends AppCompatActivity {

    private EditText restaurantNameEditText;
    private Button saveButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_restaurant);

        dbHelper = new DatabaseHelper(this);
        restaurantNameEditText = findViewById(R.id.restaurantNameEditText);
        saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(v -> {
            String restaurantName = restaurantNameEditText.getText().toString().trim();
            if (!restaurantName.isEmpty()) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(DatabaseHelper.COLUMN_NAME, restaurantName);
                db.insert(DatabaseHelper.TABLE_RESTAURANTS, null, values);
                Toast.makeText(this, "Restaurant ajouté", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
