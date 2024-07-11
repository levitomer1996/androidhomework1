package com.example.homework1;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Get location data from intent
        Intent intent = getIntent();
        String location = intent.getStringExtra("location");

        // Parse the location (replace with actual location data handling)
        LatLng latLng = parseLocation(location);

        // Add a marker and move the camera
        mMap.addMarker(new MarkerOptions().position(latLng).title("Marker at " + location));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    private LatLng parseLocation(String location) {
        // Replace with actual location parsing logic
        // For demonstration, using a dummy location (Sydney)
        return new LatLng(-34, 151);
    }
}
