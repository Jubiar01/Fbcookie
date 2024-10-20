package com.example.fbcookie.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fbcookie.R;
import com.example.fbcookie.SettingsAdapter;
import com.google.gson.JsonSyntaxException;
import com.example.fbcookie.adapter.Item;
import com.example.fbcookie.adapter.ItemAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class VirtualActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private List<Item> itemList; // Changed to List<Item>
    private static final String PREFS_NAME = "virtual_items";
    private static final String KEY_ITEM_LIST = "item_list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_virtual);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemList = new ArrayList<>();
        loadItemList(); // Load items from SharedPreferences
        itemAdapter = new ItemAdapter(itemList, this);
        recyclerView.setAdapter(itemAdapter);

        FloatingActionButton createButton = findViewById(R.id.createButton);
        createButton.setOnClickListener(view -> showCreateItemDialog());
    }

    private void showCreateItemDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_create_item, null);
    builder.setView(dialogView);

    EditText titleInput = dialogView.findViewById(R.id.titleInput);
    Button createButton = dialogView.findViewById(R.id.createButton);

    AlertDialog dialog = builder.create();
    createButton.setOnClickListener(view -> {
        // Get the title here, inside the OnClickListener
        String title = titleInput.getText().toString(); 

        SharedPreferences prefs = getSharedPreferences(SettingsAdapter.PREFS_NAME, MODE_PRIVATE);
        String userAgent = prefs.getString(SettingsAdapter.KEY_USER_AGENT, "Default");
        if (userAgent.equals("Default")) {
            userAgent = ""; // Or some other default user agent string
        }

        Item newItem = new Item(title, "https://m.facebook.com", userAgent);
        itemList.add(newItem);
        itemAdapter.notifyItemInserted(itemList.size() - 1);
        saveItemList();
        dialog.dismiss();
    });

    dialog.show();
}
    public void openItemWebView(Item item) { // Pass the Item object
        Intent intent = new Intent(VirtualActivity.this, ItemWebViewActivity.class);
        intent.putExtra("item", item); // Pass the whole Item object
        startActivity(intent);
    }

     private void saveItemList() {
    SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
    SharedPreferences.Editor editor = prefs.edit();
    Gson gson = new Gson();
    String json = gson.toJson(itemList); // Convert the List<Item> to JSON
    editor.putString(KEY_ITEM_LIST, json);
    editor.apply();
}

private void loadItemList() {
    SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
    Gson gson = new Gson();
    String json = prefs.getString(KEY_ITEM_LIST, null);
    if (json != null) {
        try {
            // First, try to parse as List<Item> (new format)
            Type type = new TypeToken<ArrayList<Item>>() {}.getType();
            itemList = gson.fromJson(json, type);
        } catch (JsonSyntaxException e) {
            // If that fails, try to parse as List<String> (old format)
            try {
                Type type = new TypeToken<ArrayList<String>>() {}.getType();
                List<String> oldItemList = gson.fromJson(json, type);
                // Convert the old list to the new format
                itemList = new ArrayList<>();
                for (String title : oldItemList) {
                    itemList.add(new Item(title, "https://m.facebook.com", "")); // Use default values for other fields
                }
            } catch (JsonSyntaxException e2) {
                // If both fail, just create a new empty list
                itemList = new ArrayList<>();
            }
        }
    } else {
        itemList = new ArrayList<>();
    }
}
}