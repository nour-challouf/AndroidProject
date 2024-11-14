package com.example.androidproject;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.room.Room;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    AppDatabase appDatabase;
    EditText usernameInput, passwordInput;
    Button loginButton, signupLink ,mapLink;
    DatabaseClient db;
    UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "app_database")
                .fallbackToDestructiveMigration()
                .build();
        setContentView(R.layout.activity_login);

        usernameInput = findViewById(R.id.username);
        passwordInput = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        signupLink = findViewById(R.id.signupLink);
        mapLink = findViewById(R.id.mapLink);

        db = DatabaseClient.getInstance(this);
        userDao = db.getAppDatabase().userDao();

        // Link to Signup Activity
        signupLink.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
        });

        mapLink.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, MapsActivity.class));
        });
loadDeliveries();
        // Login Button Click Listener
        loginButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString();
            String password = passwordInput.getText().toString();
            validateUser(username, password);
        });

    }

    private void insertSampleData() {
        // Add some sample deliveries to test the database interaction
        Delivery delivery = new Delivery(10.5, 15.0);  // Example: distance and price
        AsyncTask.execute(() -> {
            appDatabase.deliveryDao().insert(delivery);
            Log.d("DatabaseCheck", "Sample delivery inserted");
        });
    }

    public void loadDeliveries() {
        // Observe the LiveData from the deliveryDao
        appDatabase.deliveryDao().getAllDeliveries().observe(this, new Observer<List<Delivery>>() {
            @Override
            public void onChanged(List<Delivery> deliveries) {
                // When data changes, log the deliveries
                for (Delivery delivery : deliveries) {
                    Log.d("DatabaseCheck", "Delivery: " + delivery.toString());
                }
            }
        });
    }

    private void validateUser(String username, String password) {
        AsyncTask.execute(() -> {
            User user = userDao.getUserByUsername(username);
            runOnUiThread(() -> {
                if (user != null && user.getPassword().equals(password)) {
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
