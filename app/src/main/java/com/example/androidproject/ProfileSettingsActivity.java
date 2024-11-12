package com.example.androidproject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileSettingsActivity extends AppCompatActivity {
    private EditText fullName, email, phone;
    private DatabaseClient db;
    private UserDao userDao;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        fullName = findViewById(R.id.fullName);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        Button updateButton = findViewById(R.id.updateButton);

        db = DatabaseClient.getInstance(this);
        userDao = db.getAppDatabase().userDao();

        String username = getIntent().getStringExtra("username");
        Toast.makeText(this, "Welcome " + username, Toast.LENGTH_SHORT).show();
        loadUser(username);
        updateButton.setOnClickListener(v -> updateUser());
    }

    private void loadUser(String username) {
        AsyncTask.execute(() -> {
            currentUser = userDao.getUserByUsername(username);
            runOnUiThread(() -> {
                if (currentUser != null) {
                    fullName.setText(currentUser.getFullName());
                    email.setText(currentUser.getEmail());
                    phone.setText(currentUser.getPhone());
                }
            });
        });
    }

    private void updateUser() {
        if (currentUser == null) {
            Toast.makeText(this, "User data not loaded. Please try again.", Toast.LENGTH_SHORT).show();
            return;
        }
        String name = fullName.getText().toString();
        String emailAddress = email.getText().toString();
        String phoneNumber = phone.getText().toString();

        if (!name.isEmpty() && !emailAddress.isEmpty() && !phoneNumber.isEmpty()) {
            currentUser.setFullName(name);
            currentUser.setEmail(emailAddress);
            currentUser.setPhone(phoneNumber);

            AsyncTask.execute(() -> {
                userDao.updateUser(currentUser);
                runOnUiThread(() ->
                        Toast.makeText(ProfileSettingsActivity.this, "Information Updated", Toast.LENGTH_SHORT).show()
                );
            });
        } else {
            Toast.makeText(ProfileSettingsActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        }
    }
}
