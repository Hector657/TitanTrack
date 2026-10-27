package com.titantrack.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.titantrack.R;
import com.titantrack.adapters.SeriesAdapter;
import com.titantrack.models.SeriesRecord;
import com.titantrack.utils.TrainingDbHelper;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TrainingActivity extends AppCompatActivity {

    private TextView txtTimer;
    private Spinner spinnerExercise;
    private MaterialButton btnStartRest, btnAddSeries, btnFinish;
    private EditText inputKg, inputReps, inputRest;
    private CountDownTimer countDownTimer;

    private final List<SeriesRecord> seriesList = new ArrayList<>();
    private SeriesAdapter adapter;
    private TrainingDbHelper dbHelper;

    private final String[] ejercicios = {
            "Press de banca", "Sentadilla", "Peso muerto", "Press militar",
            "Curl de bíceps", "Dominadas", "Zancadas", "Plancha abdominal",
            "Remo con barra", "Hip thrust"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        dbHelper = new TrainingDbHelper(this);

        ImageButton btnBack = findViewById(R.id.btnBack);
        txtTimer = findViewById(R.id.txtTimer);
        spinnerExercise = findViewById(R.id.spinnerExercise);
        btnStartRest = findViewById(R.id.btnStartRest);
        btnAddSeries = findViewById(R.id.btnAddSeries);
        btnFinish = findViewById(R.id.btnFinish);
        inputKg = findViewById(R.id.inputKg);
        inputReps = findViewById(R.id.inputReps);
        inputRest = findViewById(R.id.inputRest);

        btnBack.setOnClickListener(v -> finish());

        ArrayAdapter<String> spinnerAdapter =
                new ArrayAdapter<>(this, R.layout.spinner_item, ejercicios);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerExercise.setAdapter(spinnerAdapter);

        RecyclerView recycler = findViewById(R.id.recyclerSeries);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SeriesAdapter(seriesList);
        recycler.setAdapter(adapter);

        // Registrar serie (en memoria)
        btnAddSeries.setOnClickListener(v -> {
            String kgStr = inputKg.getText().toString().trim();
            String repsStr = inputReps.getText().toString().trim();

            if (TextUtils.isEmpty(kgStr)) {
                inputKg.setError("Introduce el peso");
                return;
            }
            if (TextUtils.isEmpty(repsStr)) {
                inputReps.setError("Introduce las repeticiones");
                return;
            }

            String ejercicio = spinnerExercise.getSelectedItem().toString();
            double kg = Double.parseDouble(kgStr);
            int reps = Integer.parseInt(repsStr);
            int numero = seriesList.size() + 1;

            seriesList.add(new SeriesRecord(numero, ejercicio, kg, reps));
            adapter.notifyItemInserted(seriesList.size() - 1);

            inputKg.setText("");
            inputReps.setText("");
            Toast.makeText(this, "Serie " + numero + " registrada", Toast.LENGTH_SHORT).show();

            startRestTimer();
        });

        btnStartRest.setOnClickListener(v -> startRestTimer());

        // Finalizar y guardar la sesión en disco (SQLite)
        btnFinish.setOnClickListener(v -> {
            if (seriesList.isEmpty()) {
                Toast.makeText(this, "No hay series que guardar", Toast.LENGTH_SHORT).show();
                return;
            }
            String fecha = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                    .format(new Date());
            for (SeriesRecord s : seriesList) {
                dbHelper.insertarSerie(s.getEjercicio(), s.getKg(), s.getReps(), fecha);
            }
            Toast.makeText(this, "Sesión guardada (" + seriesList.size() + " series)",
                    Toast.LENGTH_LONG).show();
            // Ir al historial
            startActivity(new Intent(this, HistoryActivity.class));
            finish();
        });
    }

    private void startRestTimer() {
        long restSeconds = 30;
        String restStr = inputRest.getText().toString().trim();
        if (!TextUtils.isEmpty(restStr)) {
            restSeconds = Long.parseLong(restStr);
        }
        if (restSeconds <= 0) restSeconds = 30;
        long restMillis = restSeconds * 1000;

        if (countDownTimer != null) countDownTimer.cancel();
        btnStartRest.setEnabled(false);
        btnAddSeries.setEnabled(false);

        countDownTimer = new CountDownTimer(restMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long totalSec = millisUntilFinished / 1000;
                long min = totalSec / 60;
                long sec = totalSec % 60;
                txtTimer.setText(String.format(Locale.getDefault(), "%02d:%02d", min, sec));
            }

            @Override
            public void onFinish() {
                txtTimer.setText("00:00");
                btnStartRest.setEnabled(true);
                btnAddSeries.setEnabled(true);
                Toast.makeText(TrainingActivity.this, "¡Descanso terminado!", Toast.LENGTH_SHORT).show();
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) countDownTimer.cancel();
    }
}