package com.titantrack.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.titantrack.R;
import com.titantrack.adapters.SeriesAdapter;
import com.titantrack.models.SeriesRecord;
import com.titantrack.utils.TrainingDbHelper;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        ImageButton btnBack = findViewById(R.id.btnBackHistory);
        TextView txtEmpty = findViewById(R.id.txtEmpty);
        RecyclerView recycler = findViewById(R.id.recyclerHistory);

        btnBack.setOnClickListener(v -> finish());

        // Lee el historial desde SQLite
        TrainingDbHelper dbHelper = new TrainingDbHelper(this);
        List<SeriesRecord> historial = dbHelper.obtenerHistorial();

        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(new SeriesAdapter(historial));

        // Mensaje si está vacío
        if (historial.isEmpty()) {
            txtEmpty.setVisibility(View.VISIBLE);
            recycler.setVisibility(View.GONE);
        }
    }
}