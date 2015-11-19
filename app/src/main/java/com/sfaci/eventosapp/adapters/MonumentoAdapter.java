package com.sfaci.eventosapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sfaci.eventosapp.R;
import com.sfaci.eventosapp.base.Monumento;

import java.util.ArrayList;

/**
 * Adaptador para listar los Eventos en un ListView
 * @author Santiago Faci
 * @version curso 2015-2016
 */
public class MonumentoAdapter extends ArrayAdapter<Monumento> {

    private Context contexto;
    private int layoutId;
    private ArrayList<Monumento> datos;

    public MonumentoAdapter(Context contexto, int layoutId, ArrayList<Monumento> datos) {
        super(contexto, layoutId, datos);
        this.contexto = contexto;
        this.layoutId = layoutId;
        this.datos = datos;
    }

    @Override
    public View getView(int posicion, View view, ViewGroup padre) {

        View fila = view;
        ItemMonumento item = null;

        if (fila == null) {
            LayoutInflater inflater = ((Activity) contexto).getLayoutInflater();
            fila = inflater.inflate(layoutId, padre, false);

            item = new ItemMonumento();
            item.imagen = (ImageView) fila.findViewById(R.id.ivImagen);
            item.titulo = (TextView) fila.findViewById(R.id.tvTitulo);

            fila.setTag(item);
        }
        else {
            item = (ItemMonumento) fila.getTag();
        }

        Monumento monumento = datos.get(posicion);
        item.imagen.setImageDrawable(contexto.getResources().getDrawable(R.drawable.ic_launcher));
        item.titulo.setText(monumento.getTitulo());

        return fila;
    }

    @Override
    public int getCount() {
        return datos.size();
    }

    @Override
    public Monumento getItem(int posicion) {

        return datos.get(posicion);
    }

    @Override
    public long getItemId(int posicion) {
        return posicion;
    }

    static class ItemMonumento {

        ImageView imagen;
        TextView titulo;
    }
}
