package com.titantrack.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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

    private ActivityResultLauncher<String> cameraPermissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // --- Mock data con imágenes temáticas (LoremFlickr, por palabra clave) ---
        List<Exercise> exercises = new ArrayList<>();
        exercises.add(new Exercise("Press de banca", "Pecho", "Intermedio", 20, "https://picsum.photos/seed/bench/200/200"));
        exercises.add(new Exercise("Sentadilla", "Piernas", "Avanzado", 25, "https://picsum.photos/seed/squat/200/200"));
        exercises.add(new Exercise("Peso muerto", "Espalda", "Avanzado", 30, "https://picsum.photos/seed/deadlift/200/200"));
        exercises.add(new Exercise("Press militar", "Hombros", "Intermedio", 15, "https://picsum.photos/seed/ohp/200/200"));
        exercises.add(new Exercise("Curl de bíceps", "Brazos", "Principiante", 12, "https://picsum.photos/seed/biceps/200/200"));
        exercises.add(new Exercise("Extensión de tríceps", "Brazos", "Principiante", 12, "https://picsum.photos/seed/triceps/200/200"));
        exercises.add(new Exercise("Dominadas", "Espalda", "Avanzado", 18, "https://picsum.photos/seed/pullup/200/200"));
        exercises.add(new Exercise("Zancadas", "Piernas", "Intermedio", 20, "https://picsum.photos/seed/lunge/200/200"));
        exercises.add(new Exercise("Plancha abdominal", "Core", "Principiante", 10, "https://picsum.photos/seed/plank/200/200"));
        exercises.add(new Exercise("Remo con barra", "Espalda", "Intermedio", 22, "https://picsum.photos/seed/row/200/200"));
        exercises.add(new Exercise("Elevaciones laterales", "Hombros", "Principiante", 10, "https://picsum.photos/seed/lateral/200/200"));
        exercises.add(new Exercise("Hip thrust", "Glúteos", "Intermedio", 18, "https://picsum.photos/seed/hipthrust/200/200"));

        RecyclerView recycler = findViewById(R.id.recyclerExercises);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(new ExerciseAdapter(exercises));

        TextView title = findViewById(R.id.txtSectionTitle);
        TextView placeholder = findViewById(R.id.txtPlaceholder);
        MaterialButton btnTraining = findViewById(R.id.btnTraining);
        MaterialButton btnCamera = findViewById(R.id.btnCamera);
        MaterialButton btnLogout = findViewById(R.id.btnLogout);
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);

        SessionManager session = new SessionManager(this);

        // --- Registro del lanzador de permiso (API moderna) ---
        cameraPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        Toast.makeText(this, "Permiso de cámara concedido", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show();
                    }
                });

        // Navegación entre secciones
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                title.setText(R.string.nav_home);
                recycler.setVisibility(View.GONE);
                placeholder.setText(R.string.dashboard_home);
                placeholder.setVisibility(View.VISIBLE);
                btnTraining.setVisibility(View.GONE);
                btnCamera.setVisibility(View.GONE);
                btnLogout.setVisibility(View.GONE);
            } else if (id == R.id.nav_workouts) {
                title.setText(R.string.catalog_title);
                recycler.setVisibility(View.VISIBLE);
                placeholder.setVisibility(View.GONE);
                btnTraining.setVisibility(View.VISIBLE);
                btnCamera.setVisibility(View.GONE);
                btnLogout.setVisibility(View.GONE);
            } else if (id == R.id.nav_profile) {
                title.setText(R.string.nav_profile);
                recycler.setVisibility(View.GONE);
                placeholder.setText("Sesión: " + session.getUsername());
                placeholder.setVisibility(View.VISIBLE);
                btnTraining.setVisibility(View.GONE);
                btnCamera.setVisibility(View.VISIBLE);
                btnLogout.setVisibility(View.VISIBLE);
            }
            return true;
        });

        // Entrenamiento
        btnTraining.setOnClickListener(v ->
                startActivity(new Intent(DashboardActivity.this, TrainingActivity.class)));
        btnTraining.setOnLongClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, HistoryActivity.class));
            return true;
        });

        // --- Permiso de cámara en tiempo de ejecución ---
        btnCamera.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "La cámara ya tiene permiso", Toast.LENGTH_SHORT).show();
            } else {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA);
            }
        });

        // Cerrar sesión
        btnLogout.setOnClickListener(v -> {
            session.logout();
            Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        bottomNav.setSelectedItemId(R.id.nav_workouts);
    }
}