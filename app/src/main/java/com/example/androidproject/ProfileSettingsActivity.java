package com.example.androidproject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileSettingsActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "MyAppPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        EditText fullName = findViewById(R.id.fullName);
        EditText email = findViewById(R.id.email);
        EditText phone = findViewById(R.id.phone);
        Button updateButton = findViewById(R.id.updateButton);

        // Load user information
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        fullName.setText(preferences.getString("fullName", ""));
        email.setText(preferences.getString("email", ""));
        phone.setText(preferences.getString("phone", ""));

        updateButton.setOnClickListener(v -> {
            String name = fullName.getText().toString();
            String emailAddress = email.getText().toString();
            String phoneNumber = phone.getText().toString();

            if (!name.isEmpty() && !emailAddress.isEmpty() && !phoneNumber.isEmpty()) {
                // Save updated user information
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("fullName", name);
                editor.putString("email", emailAddress);
                editor.putString("phone", phoneNumber);
                editor.apply();

                Toast.makeText(ProfileSettingsActivity.this, "Information Updated", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ProfileSettingsActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

