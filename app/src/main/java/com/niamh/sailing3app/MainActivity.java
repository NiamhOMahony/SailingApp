package com.niamh.sailing3app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.niamh.sailing3app.SafetyCRUD.ShowSafetyList.SafetyListActivity;

public class MainActivity extends AppCompatActivity {

    Button instructorButton;
    Button adminButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instructorButton = findViewById(R.id.instructorButton);
        adminButton = findViewById(R.id.adminButton);

        instructorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent instructorIntent = new Intent(getApplicationContext(), Dashboard2Activity.class);
                startActivity(instructorIntent);
            }
        });

        adminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent admninIntent = new Intent(getApplicationContext(), DashboardActivity.class);
                startActivity(admninIntent);
            }
        });
    }
}