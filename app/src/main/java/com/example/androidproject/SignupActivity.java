package com.example.androidproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    EditText fullName, email, phone, username, password;
    Button signupButton;
    DatabaseClient db;
    UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        fullName = findViewById(R.id.fullName);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        signupButton = findViewById(R.id.signupButton);

        db = DatabaseClient.getInstance(this);
        userDao = db.getAppDatabase().userDao();

        signupButton.setOnClickListener(v -> {
            String name = fullName.getText().toString();
            String emailAddress = email.getText().toString();
            String phoneNumber = phone.getText().toString();
            String user = username.getText().toString();
            String pass = password.getText().toString();

            if (!name.isEmpty() && !emailAddress.isEmpty() && !phoneNumber.isEmpty() && !user.isEmpty() && !pass.isEmpty()) {
                registerUser(new User(name, emailAddress, phoneNumber, user, pass));
            } else {
                Toast.makeText(SignupActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registerUser(User user) {
        AsyncTask.execute(() -> {
            if (userDao.getUserByUsername(user.getUsername()) == null) {
                userDao.insert(user);
                runOnUiThread(() -> {
                    Toast.makeText(SignupActivity.this, "Signup Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                    finish();
                });
            } else {
                runOnUiThread(() -> Toast.makeText(SignupActivity.this, "Username already exists", Toast.LENGTH_SHORT).show());
            }
        });
    }
}
