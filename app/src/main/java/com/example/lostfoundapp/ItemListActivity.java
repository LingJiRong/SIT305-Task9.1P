
package com.example.lostfoundapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ItemListActivity extends AppCompatActivity implements ItemAdapter.OnItemClickListener {

    RecyclerView recyclerView;
    DatabaseHelper db;
    ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        recyclerView = findViewById(R.id.recyclerView);
        db = new DatabaseHelper(this);
        List<Item> items = db.getAllItems();

        adapter = new ItemAdapter(items, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(Item item) {
        Intent intent = new Intent(this, ItemDetailActivity.class);
        intent.putExtra("id", item.id);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.updateItems(db.getAllItems());
    }
}
