package com.sfaci.eventosapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sfaci.eventosapp.base.Evento;
import com.sfaci.eventosapp.util.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.sfaci.eventosapp.util.Constants.AFORO_EVENTO;
import static com.sfaci.eventosapp.util.Constants.DESCRIPCION_EVENTO;
import static com.sfaci.eventosapp.util.Constants.DIRECCION_EVENTO;
import static com.sfaci.eventosapp.util.Constants.FECHA_EVENTO;
import static com.sfaci.eventosapp.util.Constants.ID;
import static com.sfaci.eventosapp.util.Constants.IMAGEN_EVENTO;
import static com.sfaci.eventosapp.util.Constants.NOMBRE_EVENTO;
import static com.sfaci.eventosapp.util.Constants.PRECIO_EVENTO;
import static com.sfaci.eventosapp.util.Constants.TABLA_EVENTOS;

/**
 * Clase que gestiona el acceso a la Base de Datos SQLite
 *
 * @author Santiago Faci
 * @version curso 2015-2016
 */
public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "eventosapp.db";
    private static final int DATABASE_VERSION = 1;

    // Claúsula SELECT y ORDER BY para utilizar a la hora de consultar los datos
    private static String[] SELECT_CURSOR = {ID, NOMBRE_EVENTO, DESCRIPCION_EVENTO, DIRECCION_EVENTO, PRECIO_EVENTO, FECHA_EVENTO, AFORO_EVENTO, IMAGEN_EVENTO };
    private static String ORDER_BY = FECHA_EVENTO + " DESC";

    public Database(Context contexto) {
        super(contexto, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLA_EVENTOS + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NOMBRE_EVENTO + " TEXT, " +
            DESCRIPCION_EVENTO + " TEXT, " + DIRECCION_EVENTO + " TEXT, " + PRECIO_EVENTO + " REAL, " +
            FECHA_EVENTO + " TEXT, " + AFORO_EVENTO + " INT, " + IMAGEN_EVENTO + " BLOB)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_EVENTOS);
        onCreate(db);
    }

    /**
     * Crea un nuevo evento en la Base de Datos
     * @param evento
     */
    public void nuevoEvento(Evento evento) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NOMBRE_EVENTO, evento.getNombre());
        values.put(DESCRIPCION_EVENTO, evento.getDescripcion());
        values.put(DIRECCION_EVENTO, evento.getDireccion());
        values.put(PRECIO_EVENTO, evento.getPrecio());
        values.put(FECHA_EVENTO, Util.formatearFecha(evento.getFecha()));
        values.put(AFORO_EVENTO, evento.getAforo());
        values.put(IMAGEN_EVENTO, Util.getBytes(evento.getImagen()));

        db.insertOrThrow(TABLA_EVENTOS, null, values);
        db.close();

        // Tambien se pueden lanzar sentencia SQL
        // String[] argumentos = new String[]{arg1, arg2, arg3};
        //db.execSQL("INSERT INTO . . . . ? ? ?", argumentos);
    }

    /**
     * Modifica un evento de la Base de Datos
     * @param evento
     */
    public void modificarEvento(Evento evento) {

        SQLiteDatabase db = getWritableDatabase();

        // TODO Rellenar todos los campos de un Evento
        ContentValues values = new ContentValues();
        values.put(NOMBRE_EVENTO, evento.getNombre());
        values.put(DESCRIPCION_EVENTO, evento.getDescripcion());

        String[] argumentos = new String[]{String.valueOf(evento.getId())};
        db.update(TABLA_EVENTOS, values, "id = ?", argumentos);
        db.close();

        // Tambien se pueden lanzar sentencia SQL
        // String[] argumentos = new String[]{arg1, arg2, arg3};
        //db.execSQL("UPDATE eventos SET . . . ? ? ?", argumentos);
    }

    /**
     * Elimina un evento de la Base de Datos
     * @param evento
     */
    public void eliminarEvento(Evento evento) {

        SQLiteDatabase db = getWritableDatabase();

        String[] argumentos = new String[]{String.valueOf(evento.getId())};
        db.delete(TABLA_EVENTOS, "id = ?", argumentos);
        db.close();

        // Tambien se pueden lanzar sentencia SQL
        // String[] argumentos = new String[]{arg1, arg2, arg3};
        //db.execSQL("DELETE FROM . . . . ? ? ?", argumentos);
    }

    /**
     * Obtiene todos los eventos de la Base de Datos como un ArrayList
     * @return
     */
    public ArrayList<Evento> getEventos() {

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLA_EVENTOS, SELECT_CURSOR, null, null, null, null, ORDER_BY);

        ArrayList<Evento> listaEventos = new ArrayList<Evento>();
        Evento evento = null;
        while (cursor.moveToNext()) {
            evento = new Evento();
            evento.setId(cursor.getLong(0));
            evento.setNombre(cursor.getString(1));
            evento.setDescripcion(cursor.getString(2));
            evento.setDireccion(cursor.getString(3));
            evento.setPrecio(cursor.getFloat(4));
            try {
                evento.setFecha(Util.parsearFecha(cursor.getString(5)));
            } catch (ParseException pe) {
                // Si no se puede leer la fecha se coloca la de hoy por defecto
                evento.setFecha(new Date(System.currentTimeMillis()));
            }
            evento.setAforo(cursor.getInt(6));
            evento.setImagen(Util.getBitmap(cursor.getBlob(7)));

            listaEventos.add(evento);
        }
        db.close();

        return listaEventos;

        // Tambien se pueden lanzar sentencia SQL
        // String[] argumentos = new String[]{arg1, arg2, arg3};
        //db.rawQuery("SELECT nombre, descripcion . . . WHERE . . . ? ? ?", argumentos);
    }
}
