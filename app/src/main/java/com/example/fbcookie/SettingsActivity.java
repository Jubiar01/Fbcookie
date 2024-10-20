package com.example.fbcookie;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        RecyclerView recyclerView = findViewById(R.id.settings_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<String> settingsList = new ArrayList<>();
        settingsList.add("User-Agent");
        // Add more settings here

        SettingsAdapter adapter = new SettingsAdapter(settingsList, this);
        recyclerView.setAdapter(adapter);
    }
}