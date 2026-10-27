package com.titantrack.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.titantrack.models.SeriesRecord;
import java.util.ArrayList;
import java.util.List;

public class TrainingDbHelper extends SQLiteOpenHelper {

    // --- Esquema: constantes ---
    private static final String DB_NAME = "titantrack.db";
    private static final int DB_VERSION = 1;

    public static final String TABLE = "tabla_historial";
    public static final String COL_ID = "id";
    public static final String COL_EJERCICIO = "ejercicio";
    public static final String COL_KG = "kg";
    public static final String COL_REPS = "reps";
    public static final String COL_FECHA = "fecha";

    // Sentencia DDL de creación de la tabla
    private static final String SQL_CREATE =
            "CREATE TABLE " + TABLE + " (" +
                    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_EJERCICIO + " TEXT, " +
                    COL_KG + " REAL, " +
                    COL_REPS + " INTEGER, " +
                    COL_FECHA + " TEXT)";

    private static final String SQL_DROP = "DROP TABLE IF EXISTS " + TABLE;

    public TrainingDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP);
        onCreate(db);
    }

    // --- INSERT: vuelca una serie en disco ---
    public long insertarSerie(String ejercicio, double kg, int reps, String fecha) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_EJERCICIO, ejercicio);
        values.put(COL_KG, kg);
        values.put(COL_REPS, reps);
        values.put(COL_FECHA, fecha);
        long id = db.insert(TABLE, null, values);
        db.close();
        return id;
    }

    // --- SELECT: recupera el historial con un Cursor ---
    public List<SeriesRecord> obtenerHistorial() {
        List<SeriesRecord> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT * FROM " + TABLE + " ORDER BY " + COL_ID + " DESC";
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                String ejercicio = cursor.getString(cursor.getColumnIndexOrThrow(COL_EJERCICIO));
                double kg = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_KG));
                int reps = cursor.getInt(cursor.getColumnIndexOrThrow(COL_REPS));
                String fecha = cursor.getString(cursor.getColumnIndexOrThrow(COL_FECHA));
                lista.add(new SeriesRecord(0, ejercicio + "  ·  " + fecha, kg, reps));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lista;
    }
}