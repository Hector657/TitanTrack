package com.titantrack.models;

public class Exercise {
    private final String name;
    private final String muscleGroup;
    private final String level;
    private final int durationMin;
    private final String imageUrl;

    public Exercise(String name, String muscleGroup, String level, int durationMin, String imageUrl) {
        this.name = name;
        this.muscleGroup = muscleGroup;
        this.level = level;
        this.durationMin = durationMin;
        this.imageUrl = imageUrl;
    }

    public String getName() { return name; }
    public String getMuscleGroup() { return muscleGroup; }
    public String getLevel() { return level; }
    public int getDurationMin() { return durationMin; }
    public String getImageUrl() { return imageUrl; }
}