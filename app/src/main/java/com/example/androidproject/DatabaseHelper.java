package com.example.androidproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "restaurant.db";
    private static final int DATABASE_VERSION = 2;  // Augmenter la version de la base de données

    // Table and columns for restaurants and menu
    public static final String TABLE_RESTAURANTS = "restaurants";
    public static final String TABLE_MENU = "menu";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_MENU_ITEM = "menu_item";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_RESTAURANT_ID = "restaurant_id";
    public static final String COLUMN_IMAGE_URL = "image_url";  // Nouvelle colonne pour l'URL de l'image

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Créer la table des restaurants
        String CREATE_RESTAURANT_TABLE = "CREATE TABLE " + TABLE_RESTAURANTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT)";
        db.execSQL(CREATE_RESTAURANT_TABLE);

        // Créer la table des menus avec la nouvelle colonne image_url
        String CREATE_MENU_TABLE = "CREATE TABLE " + TABLE_MENU + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_MENU_ITEM + " TEXT, " +
                COLUMN_PRICE + " REAL, " +
                COLUMN_RESTAURANT_ID + " INTEGER, " +
                COLUMN_IMAGE_URL + " TEXT, " +  // Ajouter la colonne image_url
                "FOREIGN KEY(" + COLUMN_RESTAURANT_ID + ") REFERENCES " + TABLE_RESTAURANTS + "(" + COLUMN_ID + "))";
        db.execSQL(CREATE_MENU_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Mise à jour de la base de données (ajouter la colonne image_url si nécessaire)
        if (oldVersion < 2) {
            // Ajouter la colonne image_url à la table menu si elle n'existe pas encore
            db.execSQL("ALTER TABLE " + TABLE_MENU + " ADD COLUMN " + COLUMN_IMAGE_URL + " TEXT");
        }
    }

    // Méthode pour insérer un élément de menu avec l'URL de l'image
    public void addMenuItem(long restaurantId, String menuItem, double price, String imageUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RESTAURANT_ID, restaurantId);
        values.put(COLUMN_MENU_ITEM, menuItem);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_IMAGE_URL, imageUrl);  // Ajouter l'URL de l'image

        db.insert(TABLE_MENU, null, values);
        db.close();
    }

    // Méthode pour récupérer les éléments de menu pour un restaurant, y compris l'URL de l'image
    public Cursor getMenuItemsForRestaurant(long restaurantId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_MENU,
                new String[]{COLUMN_ID, COLUMN_MENU_ITEM, COLUMN_PRICE, COLUMN_RESTAURANT_ID, COLUMN_IMAGE_URL},  // Inclure image_url
                COLUMN_RESTAURANT_ID + " = ?",
                new String[]{String.valueOf(restaurantId)},
                null, null, null);
    }

    // Méthode pour supprimer un élément du menu
    public void deleteMenuItem(long itemId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            // Suppression de l'élément du menu par ID
            db.delete(TABLE_MENU, COLUMN_ID + " = ?", new String[]{String.valueOf(itemId)});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }
}
