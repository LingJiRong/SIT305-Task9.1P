package com.example.lostfoundapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class AddItemActivity extends AppCompatActivity {

    RadioGroup postTypeGroup;
    RadioButton lostRadio, foundRadio;
    EditText nameInput, phoneInput, descriptionInput, dateInput, locationInput;
    Button saveButton, getCurrentLocationButton;
    DatabaseHelper db;
    FusedLocationProviderClient fusedLocationClient;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        // ✅ Initialize Places API (add your real API key here)
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyCCnLm3ivIyroLeDXEt2UOnznd2zbx0Oc8", Locale.getDefault());
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        postTypeGroup = findViewById(R.id.postTypeGroup);
        lostRadio = findViewById(R.id.radioLost);
        foundRadio = findViewById(R.id.radioFound);
        nameInput = findViewById(R.id.nameInput);
        phoneInput = findViewById(R.id.phoneInput);
        descriptionInput = findViewById(R.id.descriptionInput);
        dateInput = findViewById(R.id.dateInput);
        locationInput = findViewById(R.id.locationInput);
        saveButton = findViewById(R.id.saveButton);
        getCurrentLocationButton = findViewById(R.id.getCurrentLocationButton);

        db = new DatabaseHelper(this);

        // 🔹 Autocomplete location input
        locationInput.setOnClickListener(v -> {
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS);
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                    .build(AddItemActivity.this);
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
        });

        // 🔹 Get current location button
        getCurrentLocationButton.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_REQUEST_CODE);
                return;
            }

            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                            try {
                                List<Address> addresses = geocoder.getFromLocation(
                                        location.getLatitude(), location.getLongitude(), 1);
                                if (!addresses.isEmpty()) {
                                    locationInput.setText(addresses.get(0).getAddressLine(0));
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        });

        // 🔹 Save the item
        saveButton.setOnClickListener(v -> {
            String type = lostRadio.isChecked() ? "Lost" : "Found";
            String name = nameInput.getText().toString();
            String phone = phoneInput.getText().toString();
            String description = descriptionInput.getText().toString();
            String date = dateInput.getText().toString();
            String location = locationInput.getText().toString();

            db.insertItem(type, name, phone, description, date, location);
            finish();
        });
    }

    // 🔹 Handle autocomplete result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            locationInput.setText(place.getAddress());
        }
    }

    // 🔹 Handle location permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocationButton.performClick();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

