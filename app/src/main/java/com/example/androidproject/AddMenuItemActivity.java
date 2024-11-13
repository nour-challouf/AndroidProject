package com.example.androidproject;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddMenuItemActivity extends AppCompatActivity {

    private EditText menuItemName, menuItemPrice;
    private Button saveMenuItemBtn;
    private DatabaseHelper dbHelper;
    private long restaurantId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu_item);

        dbHelper = new DatabaseHelper(this);
        menuItemName = findViewById(R.id.menuItemName);
        menuItemPrice = findViewById(R.id.menuItemPrice);
        saveMenuItemBtn = findViewById(R.id.saveMenuItemBtn);

        // Récupérer l'ID du restaurant à partir de l'intent
        restaurantId = getIntent().getLongExtra("restaurant_id", -1);

        saveMenuItemBtn.setOnClickListener(v -> {
            String name = menuItemName.getText().toString().trim();
            String priceStr = menuItemPrice.getText().toString().trim();

            if (name.isEmpty() || priceStr.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double price = Double.parseDouble(priceStr);
                addMenuItem(name, price);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Le prix doit être un nombre valide", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addMenuItem(String name, double price) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_MENU_ITEM, name);
        values.put(DatabaseHelper.COLUMN_PRICE, price);
        values.put(DatabaseHelper.COLUMN_RESTAURANT_ID, restaurantId);

        long result = db.insert(DatabaseHelper.TABLE_MENU, null, values);
        db.close();

        if (result != -1) {
            Toast.makeText(this, "Plat ajouté avec succès", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Erreur lors de l'ajout du plat", Toast.LENGTH_SHORT).show();
        }
    }
}
