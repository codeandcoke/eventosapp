package com.sfaci.eventosapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sfaci.eventosapp.base.Evento;

import static com.sfaci.eventosapp.util.Constants.DESCRIPCION_EVENTO;
import static com.sfaci.eventosapp.util.Constants.ID;
import static com.sfaci.eventosapp.util.Constants.NOMBRE_EVENTO;
import static com.sfaci.eventosapp.util.Constants.TABLA_EVENTOS;

/**
 * Clase que gestiona el acceso a la Base de Datos SQLite
 * @author Santiago Faci
 * @version curso 2015-2016
 */
public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "eventosapp.db";
    private static final int DATABASE_VERSION = 1;

    // Claúsula SELECT y ORDER BY para utilizar a la hora de consultar los datos
    private static String[] SELECT_CURSOR = {ID, NOMBRE_EVENTO, DESCRIPCION_EVENTO };
    private static String ORDER_BY = NOMBRE_EVENTO + " DESC";

    public Database(Context contexto) {
        super(contexto, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Lanza las instrucciones que crea las tablas
        db.execSQL("CREATE TABLE XXXXXXXXXX");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Lanza las instrucciones que actualizan la base de datos
        // db.execSQL("DROP TABLE XXXXX";
        // onCreate(db);
    }

    public void nuevoEvento(Evento evento) {

        SQLiteDatabase db = getWritableDatabase();

        // TODO Rellenar todos los campos de un Evento
        ContentValues values = new ContentValues();
        values.put(NOMBRE_EVENTO, evento.getNombre());
        values.put(DESCRIPCION_EVENTO, evento.getDescripcion());

        db.insertOrThrow(TABLA_EVENTOS, null, values);

    }

    public Cursor getEventos() {

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLA_EVENTOS, SELECT_CURSOR, null, null, null, null, ORDER_BY);
        return cursor;
    }
}
