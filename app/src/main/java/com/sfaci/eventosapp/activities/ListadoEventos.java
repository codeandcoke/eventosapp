package com.sfaci.eventosapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.sfaci.eventosapp.R;
import com.sfaci.eventosapp.adapters.EventoAdapter;
import com.sfaci.eventosapp.adapters.MonumentoAdapter;
import com.sfaci.eventosapp.base.Evento;
import com.sfaci.eventosapp.database.Database;

import java.util.ArrayList;

/**
 * Activity que lista los eventos que hay registrados en la Base de Datos
 * Los eventos están almacenados en una Base de Datos SQLite en el dispositivo
 *
 * @author Santiago Faci
 * @version curso 2015-2016
 */
public class ListadoEventos extends Activity {

    private EventoAdapter adapter;
    private ArrayList<Evento> listaEventos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_eventos);

        Database db = new Database(this);
        listaEventos = db.getEventos();
        adapter = new EventoAdapter(this, R.layout.evento_item, listaEventos);
        ListView lvEventos = (ListView) findViewById(R.id.lvEventos);
        lvEventos.setAdapter(adapter);
    }

    private void recargarEventos() {

        Database db = new Database(this);
        listaEventos = db.getEventos();
        adapter.setDatos(listaEventos);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();

        recargarEventos();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_listado_eventos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_monumentos:
                Intent intentListado = new Intent(this, ListadoMonumentos.class);
                startActivity(intentListado);
                break;
            case R.id.action_anadir_evento:
                Intent intentRegistro = new Intent(this, RegistroEventos.class);
                startActivity(intentRegistro);
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
