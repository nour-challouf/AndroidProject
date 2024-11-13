package com.example.androidproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "MyAppPrefs";
    private static final String KEY_LOGGED_IN = "isLoggedIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Définir le bouton de déconnexion
        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> {
            // Effacer l'état de connexion
            SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(KEY_LOGGED_IN, false);
            editor.apply();

            // Retour à LoginActivity
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        // Définir le bouton des paramètres du profil
        Button profileSettingsButton = findViewById(R.id.profileSettingsButton);
        profileSettingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProfileSettingsActivity.class);
            intent.putExtra("username", getIntent().getStringExtra("username"));
            startActivity(intent);
        });

        // Ajouter le bouton Liste des Restaurants
        Button restaurantListButton = findViewById(R.id.restaurantListButton);
        restaurantListButton.setOnClickListener(v -> {
            // Ouvrir l'activité contenant la liste des restaurants
            Intent intent = new Intent(MainActivity.this, RestaurantMainActivity.class);
            startActivity(intent);
        });
    }
}
