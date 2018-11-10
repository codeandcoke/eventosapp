package com.sfaci.eventosapp.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.sfaci.eventosapp.R;
import com.sfaci.eventosapp.adapters.EventoAdapter;
import com.sfaci.eventosapp.base.EventoImagen;
import com.sfaci.eventosapp.base.Evento;
import com.sfaci.eventosapp.database.Database;
import com.sfaci.eventosapp.util.Util;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

import static com.sfaci.eventosapp.util.Constants.SERVER_URL;

/**
 * Activity que lista los eventos que hay registrados en la Base de Datos
 * Los eventos están almacenados en una Base de Datos SQLite en el dispositivo
 *
 * @author Santiago Faci
 * @version curso 2015-2016
 */
public class ListadoEventos extends Activity {

    private EventoAdapter adapter;
    private ArrayList<EventoImagen> listaEventos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_eventos);

        Database db = new Database(this);
        listaEventos = db.getEventos();
        adapter = new EventoAdapter(this, R.layout.evento_item, listaEventos);
        ListView lvEventos = (ListView) findViewById(R.id.lvEventos);
        lvEventos.setAdapter(adapter);

        registerForContextMenu(lvEventos);
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.ctx_listado_eventos, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_compartir_evento:

                AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                EventoImagen evento = listaEventos.get(menuInfo.position);

                Evento eventoServidor = new Evento();
                eventoServidor.setNombre(evento.getNombre());
                eventoServidor.setDescripcion(evento.getDescripcion());
                eventoServidor.setPrecio(evento.getPrecio());
                eventoServidor.setDireccion(evento.getDireccion());
                eventoServidor.setAforo(evento.getAforo());
                eventoServidor.setFecha(evento.getFecha());
                eventoServidor.setImagen(Util.encodeBase64(evento.getImagen()));

                WebService webService = new WebService();
                /*webService.execute(evento.getNombre(), evento.getDescripcion(), evento.getDireccion(),
                        String.valueOf(evento.getPrecio()), String.valueOf(evento.getFecha().getTime()), String.valueOf(evento.getAforo()),
                        Util.encodeBase64(evento.getImagen()));*/
                webService.execute(eventoServidor);
                return true;
            default:
                return false;
        }
    }

    private class WebService extends AsyncTask<Evento, Void, Void> {

        private ProgressDialog dialog;

        @Override
        protected Void doInBackground(Evento... params) {
            // Llamada a un Servicio Web con paso de parámetros
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            try {
                /*restTemplate.postForObject(SERVER_URL + "/add_evento?nombre=" + params[0] + "&descripcion=" + params[1] + "&direccion=" + params[2] +
                        "&precio=" + params[3] + "&fecha=" + params[4] + "&aforo=" + params[5] + "&imagen=" + params[6], null, Void.class);*/
                restTemplate.postForObject(SERVER_URL + "/add_evento", params[0], Void.class);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*dialog = new ProgressDialog(ListadoEventos.this);
            dialog.setTitle(R.string.mensaje_enviando);
            dialog.show();*/
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            /*if (dialog != null)
                dialog.dismiss();*/
        }
    }
}
