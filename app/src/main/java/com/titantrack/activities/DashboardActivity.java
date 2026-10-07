package com.titantrack.activities;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.titantrack.R;

public class DashboardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        TextView content = findViewById(R.id.txtContent);
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                content.setText(R.string.dashboard_home);
            } else if (id == R.id.nav_workouts) {
                content.setText(R.string.dashboard_workouts);
            } else if (id == R.id.nav_profile) {
                content.setText(R.string.dashboard_profile);
            }
            return true;
        });
    }
}