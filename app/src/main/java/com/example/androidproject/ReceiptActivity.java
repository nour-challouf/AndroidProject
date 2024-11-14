package com.example.androidproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

public class ReceiptActivity extends AppCompatActivity {
    DatabaseClient db;

    Button confirmButton;
    DeliveryDao deliveryDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        // Initialize the DatabaseClient
        db = DatabaseClient.getInstance(getApplicationContext());  // Proper initialization

        // Now, initialize the deliveryDao
        deliveryDao = db.getAppDatabase().deliveryDao();

        confirmButton = findViewById(R.id.confirmButton);

        // Get the distance, price, and travel time passed from MapsActivity
        double distance = getIntent().getDoubleExtra("distance", 0);
        double price = getIntent().getDoubleExtra("price", 0);

        // Find the TextViews and set the values
        TextView distanceTextView = findViewById(R.id.distanceTextView);
        TextView priceTextView = findViewById(R.id.priceTextView);

        distanceTextView.setText("Distance: " + String.format("%.2f", distance) + " km");
        priceTextView.setText("Price: $" + String.format("%.2f", price));

        confirmButton.setOnClickListener(v -> {
            // Assuming you have a Delivery object that needs to be confirmed
            Delivery delivery = new Delivery(distance, price); // Populate the Delivery object
            confirm(delivery);  // Call the confirm method
        });
    }


    private void confirm(Delivery delivery) {
        AsyncTask.execute(() -> {
                deliveryDao.insert(delivery);
            Log.d("DatabaseCheck", "Delivery inserted: " + delivery.toString());
            System.out.println("DatabaseCheck"+ "Delivery inserted: " + delivery.toString());

            runOnUiThread(() -> {
                    Toast.makeText(ReceiptActivity.this, "Confirmation Successful",
                            Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ReceiptActivity.this,
                            DeliveryListActivity.class));
                    finish();
                });

        });
    }
}

