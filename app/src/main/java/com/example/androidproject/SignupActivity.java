package com.example.androidproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "MyAppPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        EditText fullName = findViewById(R.id.fullName);
        EditText email = findViewById(R.id.email);
        EditText phone = findViewById(R.id.phone);
        EditText username = findViewById(R.id.username);
        EditText password = findViewById(R.id.password);
        Button signupButton = findViewById(R.id.signupButton);

        signupButton.setOnClickListener(v -> {
            String name = fullName.getText().toString();
            String emailAddress = email.getText().toString();
            String phoneNumber = phone.getText().toString();
            String user = username.getText().toString();
            String pass = password.getText().toString();

            if (!name.isEmpty() && !emailAddress.isEmpty() && !phoneNumber.isEmpty() && !user.isEmpty() && !pass.isEmpty()) {
                // Save user information
                SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("fullName", name);
                editor.putString("email", emailAddress);
                editor.putString("phone", phoneNumber);
                editor.putString("username", user);
                editor.putString("password", pass);
                editor.putBoolean("isLoggedIn", true); // Mark the user as logged in
                editor.apply();

                Toast.makeText(SignupActivity.this, "Signup Successful", Toast.LENGTH_SHORT).show();

                // Navigate to MainActivity
                startActivity(new Intent(SignupActivity.this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(SignupActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

