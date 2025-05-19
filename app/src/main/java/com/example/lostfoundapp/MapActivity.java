package com.example.lostfoundapp;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import java.io.IOException;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        db = new DatabaseHelper(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Run geocoding in background thread
        new Thread(() -> {
            List<Item> itemList = db.getAllItems();
            Geocoder geocoder = new Geocoder(MapActivity.this);

            for (Item item : itemList) {
                try {
                    List<Address> addresses = geocoder.getFromLocationName(item.getLocation(), 1);
                    if (!addresses.isEmpty()) {
                        Address address = addresses.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                        // UI update must be done on main thread
                        runOnUiThread(() -> {
                            mMap.addMarker(new MarkerOptions()
                                    .position(latLng)
                                    .title(item.getName())
                                    .snippet(item.getType() + " - " + item.getDate()));
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // Move camera to first item if available
            if (!itemList.isEmpty()) {
                try {
                    List<Address> addresses = geocoder.getFromLocationName(itemList.get(0).getLocation(), 1);
                    if (!addresses.isEmpty()) {
                        Address address = addresses.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                        runOnUiThread(() -> {
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

