package com.titantrack.models;

public class SeriesRecord {
    private final int numero;
    private final String ejercicio;
    private final double kg;
    private final int reps;

    public SeriesRecord(int numero, String ejercicio, double kg, int reps) {
        this.numero = numero;
        this.ejercicio = ejercicio;
        this.kg = kg;
        this.reps = reps;
    }

    public int getNumero() { return numero; }
    public String getEjercicio() { return ejercicio; }
    public double getKg() { return kg; }
    public int getReps() { return reps; }
}