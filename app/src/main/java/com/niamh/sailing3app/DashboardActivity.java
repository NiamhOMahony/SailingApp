package com.niamh.sailing3app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.niamh.sailing3app.SafetyCRUD.ShowSafetyList.SafetyListActivity;

public class DashboardActivity extends AppCompatActivity {

    ImageView safetyImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        /*Code adapted from How to Implement Bottom Navigation With Activities in Android Studio
         | BottomNav | Android Coding https://www.youtube.com/watch?v=JjfSjMs0ImQ*/
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setSelectedItemId(R.id.menuHome);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.menuSettings:
                    startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.menuHome:
                case R.id.menuUser:
                    return true;
            }
            return false;
        });

        safetyImageView = findViewById(R.id.safetyImageView);

        safetyImageView.setOnClickListener(v -> {
            Intent safetyIntent = new Intent(getApplicationContext(), SafetyListActivity.class);
            startActivity(safetyIntent);
        });

    }

}