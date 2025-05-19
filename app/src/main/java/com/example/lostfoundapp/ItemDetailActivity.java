package com.example.lostfoundapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ItemDetailActivity extends AppCompatActivity {

    TextView titleView, dateView, locationView;
    Button removeButton;
    DatabaseHelper db;
    int itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        titleView = findViewById(R.id.detailTitle);
        dateView = findViewById(R.id.detailDate);
        locationView = findViewById(R.id.detailLocation);
        removeButton = findViewById(R.id.removeButton);
        db = new DatabaseHelper(this);

        itemId = getIntent().getIntExtra("id", -1);
        Item item = null;
        for (Item i : db.getAllItems()) {
            if (i.id == itemId) {
                item = i;
                break;
            }
        }

        if (item != null) {
            titleView.setText(item.type + " " + item.name);
            dateView.setText(item.date);
            locationView.setText("At " + item.location);
        }

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.deleteItem(itemId);
                finish(); // Close and return
            }
        });
    }
}
