package com.titantrack.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.titantrack.R;
import com.titantrack.adapters.ExerciseAdapter;
import com.titantrack.models.Exercise;
import com.titantrack.utils.SessionManager;
import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // --- Mock data ---
        List<Exercise> exercises = new ArrayList<>();
        exercises.add(new Exercise("Press de banca", "Pecho", "Intermedio", 20));
        exercises.add(new Exercise("Sentadilla", "Piernas", "Avanzado", 25));
        exercises.add(new Exercise("Peso muerto", "Espalda", "Avanzado", 30));
        exercises.add(new Exercise("Press militar", "Hombros", "Intermedio", 15));
        exercises.add(new Exercise("Curl de bíceps", "Brazos", "Principiante", 12));
        exercises.add(new Exercise("Extensión de tríceps", "Brazos", "Principiante", 12));
        exercises.add(new Exercise("Dominadas", "Espalda", "Avanzado", 18));
        exercises.add(new Exercise("Zancadas", "Piernas", "Intermedio", 20));
        exercises.add(new Exercise("Plancha abdominal", "Core", "Principiante", 10));
        exercises.add(new Exercise("Remo con barra", "Espalda", "Intermedio", 22));
        exercises.add(new Exercise("Elevaciones laterales", "Hombros", "Principiante", 10));
        exercises.add(new Exercise("Hip thrust", "Glúteos", "Intermedio", 18));

        RecyclerView recycler = findViewById(R.id.recyclerExercises);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(new ExerciseAdapter(exercises));

        TextView title = findViewById(R.id.txtSectionTitle);
        TextView placeholder = findViewById(R.id.txtPlaceholder);
        MaterialButton btnLogout = findViewById(R.id.btnLogout);
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);

        SessionManager session = new SessionManager(this);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                title.setText(R.string.nav_home);
                recycler.setVisibility(View.GONE);
                btnLogout.setVisibility(View.GONE);
                placeholder.setText(R.string.dashboard_home);
                placeholder.setVisibility(View.VISIBLE);
            } else if (id == R.id.nav_workouts) {
                title.setText(R.string.catalog_title);
                placeholder.setVisibility(View.GONE);
                btnLogout.setVisibility(View.GONE);
                recycler.setVisibility(View.VISIBLE);
            } else if (id == R.id.nav_profile) {
                title.setText(R.string.nav_profile);
                recycler.setVisibility(View.GONE);
                // En Perfil mostramos saludo + botón de cerrar sesión
                placeholder.setText("Sesión: " + session.getUsername());
                placeholder.setVisibility(View.VISIBLE);
                btnLogout.setVisibility(View.VISIBLE);
            }
            return true;
        });

        // Cerrar sesión: borra preferencias y vuelve al Login
        btnLogout.setOnClickListener(v -> {
            session.logout();
            Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
        com.google.android.material.button.MaterialButton btnTraining = findViewById(R.id.btnTraining);
        btnTraining.setOnClickListener(v ->
                startActivity(new android.content.Intent(DashboardActivity.this, TrainingActivity.class)));
        bottomNav.setSelectedItemId(R.id.nav_workouts);
    }
}