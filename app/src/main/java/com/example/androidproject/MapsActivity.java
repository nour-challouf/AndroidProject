package com.example.androidproject;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.SphericalUtil;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker restaurantMarker, clientMarker;

    private LatLng restaurantLocation = new LatLng(36.902126274832725, 10.185666493822774); // Example: New York City (Restaurant Location)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Initialize map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Set initial map position (for example, to NYC)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(restaurantLocation, 12));

        // Add restaurant marker
        restaurantMarker = mMap.addMarker(new MarkerOptions().position(restaurantLocation).title("Restaurant"));

        // Set up map click listener to place client marker
        mMap.setOnMapClickListener(latLng -> {
            if (clientMarker != null) {
                clientMarker.remove();  // Remove the previous client marker if it exists
            }
            clientMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Client Location"));

            // Draw line between restaurant and client
            mMap.addPolyline(new PolylineOptions().add(restaurantLocation, latLng));

            // Calculate distance between restaurant and client
            double distance = SphericalUtil.computeDistanceBetween(restaurantLocation, latLng);

            // Convert to kilometers for easier price calculation
            double distanceInKm = distance / 1000;

            // Calculate price based on distance
            double price = calculatePrice(distanceInKm);

            // Pass the price and distance to the next activity
            Intent intent = new Intent(MapsActivity.this, ReceiptActivity.class);
            intent.putExtra("distance", distanceInKm);
            intent.putExtra("price", price);
            startActivity(intent);
            finish(); // Close MapsActivity and show the receipt
        });
    }

    // Calculate price based on distance
    private double calculatePrice(double distanceInKm) {
        double basePrice = 5.0; // Base price for delivery
        double pricePerKm = 2.0; // Price per kilometer

        return basePrice + (pricePerKm * distanceInKm);  // Price based on distance
    }
}
