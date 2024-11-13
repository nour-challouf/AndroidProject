package com.example.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;

public class MenuActivity extends AppCompatActivity {

    private ListView menuListView;
    private long restaurantId;
    private Button addMenuItemBtn;
    private MenuItemDao menuItemDao;
    private RestaurantDao restaurantDao;
    private List<MenuItem> cartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        menuListView = findViewById(R.id.menuListView);
        addMenuItemBtn = findViewById(R.id.addMenuItemBtn);

        AppDatabase appDatabase = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase();
        menuItemDao = appDatabase.menuItemDao();
        restaurantDao = appDatabase.restaurantDao();

        restaurantId = getIntent().getIntExtra("restaurant_id", -1);
        cartItems = CartManager.getInstance().getCartItems();

        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }

        // Check if the restaurantId is valid
        Executors.newSingleThreadExecutor().execute(() -> {
            Restaurant restaurant = restaurantDao.getRestaurantById((int) restaurantId);
            if (restaurant == null) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Invalid restaurant ID", Toast.LENGTH_SHORT).show();
                    finish();
                });
            } else {
                loadMenu();
            }
        });

        addMenuItemBtn.setOnClickListener(v -> showAddMenuItemDialog());
    }

    private void loadMenu() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<MenuItem> menuItems = menuItemDao.getMenuItemsForRestaurant((int) restaurantId);
            ArrayList<HashMap<String, String>> menuItemsMap = new ArrayList<>();

            for (MenuItem menuItem : menuItems) {
                HashMap<String, String> map = new HashMap<>();
                map.put("name", menuItem.menuItem);
                map.put("price", String.format("%.2f D", menuItem.price));
                map.put("id", String.valueOf(menuItem.id));
                menuItemsMap.add(map);
            }

            runOnUiThread(() -> {
                SimpleAdapter adapter = new SimpleAdapter(
                        this,
                        menuItemsMap,
                        R.layout.menu_item,
                        new String[]{"name", "price"},
                        new int[]{R.id.menuItemName, R.id.menuItemPrice}
                );

                menuListView.setAdapter(adapter);

                menuListView.setAdapter(new SimpleAdapter(this, menuItemsMap, R.layout.menu_item,
                        new String[]{"name", "price"}, new int[]{R.id.menuItemName, R.id.menuItemPrice}) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);

                        HashMap<String, String> selectedItem = menuItemsMap.get(position);
                        String itemId = selectedItem.get("id");

                        Button deleteBtn = view.findViewById(R.id.deleteMenuItemBtn);
                        deleteBtn.setOnClickListener(v -> {
                            deleteMenuItem(Long.parseLong(itemId));
                            loadMenu();
                        });

                        Button addToCartBtn = view.findViewById(R.id.addToCartBtn);
                        addToCartBtn.setOnClickListener(v -> {
                            addToCart(Long.parseLong(itemId));
                        });

                        return view;
                    }
                });
            });
        });
    }

    private void showAddMenuItemDialog() {
        final EditText nameEditText = new EditText(this);
        nameEditText.setHint("Nom du plat");

        final EditText priceEditText = new EditText(this);
        priceEditText.setHint("Prix");

        android.widget.LinearLayout layout = new android.widget.LinearLayout(this);
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.addView(nameEditText);
        layout.addView(priceEditText);

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
        Executors.newSingleThreadExecutor().execute(() -> {
            MenuItem menuItem = new MenuItem(name, price, (int) restaurantId);
            menuItemDao.insertMenuItem(menuItem);
            runOnUiThread(() -> {
                Toast.makeText(this, "Plat ajouté avec succès", Toast.LENGTH_SHORT).show();
                loadMenu();
            });
        });
    }

    private void deleteMenuItem(long itemId) {
        Executors.newSingleThreadExecutor().execute(() -> {
            menuItemDao.deleteMenuItemById((int) itemId);
            runOnUiThread(() -> {
                Toast.makeText(this, "Plat supprimé avec succès", Toast.LENGTH_SHORT).show();
                loadMenu();
            });
        });
    }

    private void addToCart(long itemId) {
        Executors.newSingleThreadExecutor().execute(() -> {
            MenuItem menuItem = menuItemDao.getMenuItemById((int) itemId);
            if (menuItem != null) {
                cartItems.add(menuItem);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Item added to cart", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        CartManager cartManager = CartManager.getInstance();
        for(MenuItem item : cartItems) {
            cartManager.addToCart(item);
        }

        super.onBackPressed();
    }
}