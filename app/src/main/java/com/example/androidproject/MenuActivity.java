package com.example.androidproject;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class MenuActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ListView menuListView;
    private long restaurantId;  // Remplacer par l'ID réel du restaurant
    private Button addMenuItemBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Initialiser les vues
        dbHelper = new DatabaseHelper(this);
        menuListView = findViewById(R.id.menuListView);
        addMenuItemBtn = findViewById(R.id.addMenuItemBtn);

        // Charger le menu
        loadMenu();

        // Ajouter l'action pour le bouton "Ajouter un élément"
        addMenuItemBtn.setOnClickListener(v -> showAddMenuItemDialog());
    }

    private void loadMenu() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        ArrayList<HashMap<String, String>> menuItems = new ArrayList<>();

        try {
            cursor = db.query(DatabaseHelper.TABLE_MENU,
                    new String[]{DatabaseHelper.COLUMN_MENU_ITEM, DatabaseHelper.COLUMN_PRICE, DatabaseHelper.COLUMN_RESTAURANT_ID, DatabaseHelper.COLUMN_ID},
                    DatabaseHelper.COLUMN_RESTAURANT_ID + " = ?",
                    new String[]{String.valueOf(restaurantId)}, null, null, null);

            // Vérifier si des éléments sont retournés
            if (cursor != null && cursor.moveToFirst()) {
                // Vérifier les index des colonnes
                int menuItemIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_MENU_ITEM);
                int priceIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_PRICE);
                int idIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID);

                if (menuItemIndex >= 0 && priceIndex >= 0 && idIndex >= 0) {
                    do {
                        String item = cursor.getString(menuItemIndex);
                        double price = cursor.getDouble(priceIndex);
                        long id = cursor.getLong(idIndex);

                        // Créer un HashMap pour chaque élément du menu
                        HashMap<String, String> menuItem = new HashMap<>();
                        menuItem.put("name", item);
                        menuItem.put("price", String.format("%.2f D", price));  // Format du prix avec le symbole €
                        menuItem.put("id", String.valueOf(id));

                        menuItems.add(menuItem);
                    } while (cursor.moveToNext());
                }
            }

        } catch (Exception e) {
            Toast.makeText(this, "Erreur de chargement du menu", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();  // Assurez-vous que le curseur est fermé même en cas d'erreur
            }
            db.close();  // Fermer la base de données après l'opération
        }

        // Adapter personnalisé pour afficher les éléments du menu dans une ListView
        SimpleAdapter adapter = new SimpleAdapter(
                this,
                menuItems,
                R.layout.menu_item,  // Layout personnalisé pour chaque item du menu
                new String[]{"name", "price"}, // Les clés à récupérer dans le HashMap
                new int[]{R.id.menuItemName, R.id.menuItemPrice} // Les vues correspondantes
        );

        menuListView.setAdapter(adapter);

        // Ajouter un listener pour le bouton de suppression (X)
        menuListView.setAdapter(new SimpleAdapter(this, menuItems, R.layout.menu_item,
                new String[] { "name", "price" }, new int[] { R.id.menuItemName, R.id.menuItemPrice }) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                // Récupérer l'élément du menu
                HashMap<String, String> selectedItem = menuItems.get(position);
                String itemId = selectedItem.get("id");

                // Trouver le bouton "X" et lui ajouter un listener
                Button deleteBtn = view.findViewById(R.id.deleteMenuItemBtn);
                deleteBtn.setOnClickListener(v -> {
                    // Supprimer le plat de la base de données
                    deleteMenuItem(Long.parseLong(itemId));
                    // Recharger le menu après suppression
                    loadMenu();
                });

                return view;
            }
        });
    }

    private void showAddMenuItemDialog() {
        // Créer un EditText pour saisir le nom et le prix
        final EditText nameEditText = new EditText(this);
        nameEditText.setHint("Nom du plat");

        final EditText priceEditText = new EditText(this);
        priceEditText.setHint("Prix");

        // Créer un layout pour contenir les EditTexts
        android.widget.LinearLayout layout = new android.widget.LinearLayout(this);
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.addView(nameEditText);
        layout.addView(priceEditText);

        // Afficher une boîte de dialogue pour ajouter un plat
        new AlertDialog.Builder(this)
                .setTitle("Ajouter un plat")
                .setView(layout)
                .setPositiveButton("Ajouter", (dialog, which) -> {
                    String name = nameEditText.getText().toString();
                    String priceString = priceEditText.getText().toString();

                    if (!name.isEmpty() && !priceString.isEmpty()) {
                        double price = Double.parseDouble(priceString);
                        addMenuItem(name, price);
                    } else {
                        Toast.makeText(MenuActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Annuler", null)
                .show();
    }

    private void addMenuItem(String name, double price) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            // Insérer le nouvel élément dans la base de données
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_MENU_ITEM, name);
            values.put(DatabaseHelper.COLUMN_PRICE, price);
            values.put(DatabaseHelper.COLUMN_RESTAURANT_ID, restaurantId);

            long newRowId = db.insert(DatabaseHelper.TABLE_MENU, null, values);
            if (newRowId != -1) {
                Toast.makeText(this, "Plat ajouté avec succès", Toast.LENGTH_SHORT).show();
                loadMenu();  // Recharger le menu
            } else {
                Toast.makeText(this, "Erreur lors de l'ajout du plat", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur d'ajout", Toast.LENGTH_SHORT).show();
        } finally {
            db.close();
        }
    }

    private void deleteMenuItem(long itemId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            int rowsDeleted = db.delete(DatabaseHelper.TABLE_MENU,
                    DatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(itemId)});
            if (rowsDeleted > 0) {
                Toast.makeText(this, "Plat supprimé avec succès", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Erreur lors de la suppression du plat", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur de suppression", Toast.LENGTH_SHORT).show();
        } finally {
            db.close();
        }
    }
}
