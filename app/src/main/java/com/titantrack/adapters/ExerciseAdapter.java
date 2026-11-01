package com.titantrack.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.titantrack.R;
import com.titantrack.models.Exercise;
import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {

    private final List<Exercise> exercises;

    public ExerciseAdapter(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_exercise, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        Exercise e = exercises.get(position);
        holder.txtName.setText(e.getName());
        holder.txtMuscle.setText(e.getMuscleGroup());
        holder.txtLevel.setText(e.getLevel());
        holder.txtDuration.setText(e.getDurationMin() + " min");

        // Carga asíncrona de la imagen con Glide (caché + redimensión -> evita OOM)
        Glide.with(holder.itemView.getContext())
                .load(e.getImageUrl())
                .centerCrop()
                .placeholder(R.color.titan_chip)
                .into(holder.imgExercise);

        // Color del nivel
        int colorRes;
        switch (e.getLevel()) {
            case "Principiante": colorRes = R.color.level_beginner; break;
            case "Intermedio":   colorRes = R.color.level_intermediate; break;
            case "Avanzado":     colorRes = R.color.level_advanced; break;
            default:             colorRes = R.color.titan_accent;
        }
        holder.txtLevel.setTextColor(
                ContextCompat.getColor(holder.itemView.getContext(), colorRes));
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        ImageView imgExercise;
        TextView txtName, txtMuscle, txtLevel, txtDuration;

        ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            imgExercise = itemView.findViewById(R.id.imgExercise);
            txtName = itemView.findViewById(R.id.txtName);
            txtMuscle = itemView.findViewById(R.id.txtMuscle);
            txtLevel = itemView.findViewById(R.id.txtLevel);
            txtDuration = itemView.findViewById(R.id.txtDuration);
        }
    }
}