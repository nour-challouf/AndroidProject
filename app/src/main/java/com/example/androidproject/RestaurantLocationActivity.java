package com.example.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class RestaurantLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng selectedLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_location);

        // Initialize the map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Set up the default location
        LatLng defaultLocation = new LatLng(40.7128, -74.0060);  // Example: New York City
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 15));

        // Add a marker on click
        mMap.setOnMapClickListener(latLng -> {
            mMap.clear();  // Remove previous markers
            selectedLocation = latLng;
            mMap.addMarker(new MarkerOptions().position(latLng).title("Restaurant Location"));
        });
    }

    @Override
    public void onBackPressed() {
        if (selectedLocation != null) {
            // Send the selected location back to the previous activity
            Intent intent = new Intent();
            intent.putExtra("LATITUDE", selectedLocation.latitude);
            intent.putExtra("LONGITUDE", selectedLocation.longitude);
            setResult(RESULT_OK, intent);
            System.out.println(selectedLocation.latitude);
        }
        super.onBackPressed();
    }
}
