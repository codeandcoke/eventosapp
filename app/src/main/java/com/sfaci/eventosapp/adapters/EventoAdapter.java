package com.sfaci.eventosapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sfaci.eventosapp.R;
import com.sfaci.eventosapp.base.Evento;
import com.sfaci.eventosapp.util.Util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Adaptador para listar los Eventos en un ListView
 *
 * @author Santiago Faci
 * @version curso 2015-2016
 */
public class EventoAdapter extends ArrayAdapter<Evento> {

    private Context contexto;
    private int layoutId;
    private ArrayList<Evento> datos;

    public EventoAdapter(Context contexto, int layoutId, ArrayList<Evento> datos) {
        super(contexto, layoutId, datos);
        this.contexto = contexto;
        this.layoutId = layoutId;
        this.datos = datos;
    }

    public void setDatos(ArrayList<Evento> datos) {
        this.datos = datos;
    }

    @Override
    public View getView(int posicion, View view, ViewGroup padre) {

        View fila = view;
        ItemEvento item = null;

        if (fila == null) {
            LayoutInflater inflater = ((Activity) contexto).getLayoutInflater();
            fila = inflater.inflate(layoutId, padre, false);

            item = new ItemEvento();
            item.imagen = (ImageView) fila.findViewById(R.id.ivImagen);
            item.nombre = (TextView) fila.findViewById(R.id.tvNombre);
            item.fecha = (TextView) fila.findViewById(R.id.tvFecha);
            item.precio = (TextView) fila.findViewById(R.id.tvPrecio);

            fila.setTag(item);
        }
        else {
            item = (ItemEvento) fila.getTag();
        }

        Evento evento = datos.get(posicion);
        item.imagen.setImageBitmap(evento.getImagen());
        item.nombre.setText(evento.getNombre());
        item.fecha.setText(Util.formatearFecha(evento.getFecha()));
        if (evento.getPrecio() == 0)
            item.precio.setText("Gratis");
        else {
            DecimalFormat decimalFormat = new DecimalFormat("#.## €");
            item.precio.setText(decimalFormat.format(evento.getPrecio()));
        }

        return fila;
    }

    @Override
    public int getCount() {
        return datos.size();
    }

    @Override
    public Evento getItem(int posicion) {

        return datos.get(posicion);
    }

    @Override
    public long getItemId(int posicion) {
        return posicion;
    }

    static class ItemEvento {

        ImageView imagen;
        TextView nombre;
        TextView fecha;
        TextView precio;
    }
}
