package com.titantrack.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.titantrack.R;
import com.titantrack.models.SeriesRecord;
import java.util.List;

public class SeriesAdapter extends RecyclerView.Adapter<SeriesAdapter.SeriesViewHolder> {

    private final List<SeriesRecord> series;

    public SeriesAdapter(List<SeriesRecord> series) {
        this.series = series;
    }

    @NonNull
    @Override
    public SeriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_series, parent, false);
        return new SeriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeriesViewHolder holder, int position) {
        SeriesRecord s = series.get(position);
        String line = "Serie " + s.getNumero() + " · " + s.getEjercicio()
                + "  —  " + s.getKg() + " kg × " + s.getReps() + " reps";
        holder.txtLine.setText(line);
    }

    @Override
    public int getItemCount() {
        return series.size();
    }

    static class SeriesViewHolder extends RecyclerView.ViewHolder {
        TextView txtLine;
        SeriesViewHolder(@NonNull View itemView) {
            super(itemView);
            txtLine = itemView.findViewById(R.id.txtSeriesLine);
        }
    }
}