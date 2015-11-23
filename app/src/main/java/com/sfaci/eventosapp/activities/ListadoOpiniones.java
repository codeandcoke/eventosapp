package com.sfaci.eventosapp.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.sfaci.eventosapp.R;
import com.sfaci.eventosapp.adapters.OpinionAdapter;
import com.sfaci.eventosapp.base.Opinion;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.sfaci.eventosapp.util.Constants.SERVER_URL;

/**
 * Activity que lista las opiniones de los usuarios
 * Los datos se consiguen mediante una llamada a un servicio web
 * que hemos creado en el proyecto del lado servidor
 * Puesto que accedemos a datos a través de la red, debemos hacerlo en una AsyncTask
 *
 * TODO Mostrar sólo las opiniones para un monumento determinado
 *
 * @author Santiago Faci
 * @version curso 2015-2016
 */
public class ListadoOpiniones extends Activity {

    private OpinionAdapter adapter;
    private List<Opinion> listaOpiniones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_opiniones);

        listaOpiniones = new ArrayList<Opinion>();
        adapter = new OpinionAdapter(this, R.layout.opinion_item, listaOpiniones);
        ListView lvOpiniones = (ListView) findViewById(R.id.lvOpiniones);
        lvOpiniones.setAdapter(adapter);

        WebService webService = new WebService();
        webService.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_listado_opiniones, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        return super.onOptionsItemSelected(item);
    }

    private class WebService extends AsyncTask<String, Void, Void> {

        private ProgressDialog dialog;

        @Override
        protected Void doInBackground(String... params) {

            // Llamada a un servicio web y recogida de los datos que devuelve
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            Opinion[] opinionesArray = restTemplate.getForObject(SERVER_URL + "/opiniones", Opinion[].class);
            listaOpiniones.addAll(Arrays.asList(opinionesArray));

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            listaOpiniones.clear();

            dialog = new ProgressDialog(ListadoOpiniones.this);
            dialog.setTitle(R.string.mensaje_cargando);
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (dialog != null)
                dialog.dismiss();

            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            adapter.notifyDataSetChanged();
        }
    }
}
