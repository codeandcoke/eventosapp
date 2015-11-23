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
import android.widget.ListView;
import android.widget.Toast;

import com.sfaci.eventosapp.R;
import com.sfaci.eventosapp.adapters.MonumentoAdapter;
import com.sfaci.eventosapp.base.Monumento;
import com.sfaci.eventosapp.util.Constants;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Activity para listar los monumentos descargados del catálogo de datos
 * del ayuntamiento de Zaragoza
 * Los monumentos se leen a través de Internet en formato JSON y se parsean en tiempo real
 * para listarlos en el ListView
 * Como usamos Internet para el acceso a los datos debemos prepararlo en una AsyncTask
 *
 * @author Santiago Faci
 * @version curso 2015-2016
 */
public class ListadoMonumentos extends Activity {

    private MonumentoAdapter adapter;
    private ArrayList<Monumento> listaMonumentos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_monumentos);

        listaMonumentos = new ArrayList<Monumento>();
        adapter = new MonumentoAdapter(this, R.layout.monumento_item, listaMonumentos);
        ListView lvMonumentos = (ListView) findViewById(R.id.lvMonumentos);
        lvMonumentos.setAdapter(adapter);

        registerForContextMenu(lvMonumentos);
    }

    private void cargarListaMonumentos() {

        TareaDescarga tarea = new TareaDescarga();
        tarea.execute(Constants.URL);

    }

    @Override
    protected void onResume() {
        super.onResume();

        cargarListaMonumentos();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.ctx_listado_monumentos, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_opinar:
                // TODO Aqui podríamos filtrar y registrar la opinión asociándola sólo al monumento seleccionado
                Intent intent = new Intent(this, RegistroOpinion.class);
                startActivity(intent);
                return true;
            case R.id.action_ver_opiniones:
                // TODO Aqui podríamos filtrar y sólo ver las opiniones del Monumento seleccionado
                Intent intentVer = new Intent(this, ListadoOpiniones.class);
                startActivity(intentVer);
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_listado_monumentos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    private class TareaDescarga extends AsyncTask<String, Void, Void> {

        private boolean error = false;
        private ProgressDialog dialog;

        /**
         * Método que ejecuta la tarea en segundo plano
         * @param params
         * @return
         */
        @Override
        protected Void doInBackground(String... params) {
;
            String url = params[0];
            InputStream is = null;
            String resultado = null;
            JSONObject json = null;
            JSONArray jsonArray = null;

            try {
                // Conecta con la URL y obtenemos el fichero con los datos
                HttpClient clienteHttp = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                HttpResponse respuesta = clienteHttp.execute(httpPost);
                HttpEntity entity = respuesta.getEntity();
                is = entity.getContent();

                // Lee el fichero de datos y genera una cadena de texto como resultado
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                String linea = null;

                while ((linea = br.readLine()) != null)
                    sb.append(linea + "\n");

                is.close();
                resultado = sb.toString();

                json = new JSONObject(resultado);
                jsonArray = json.getJSONArray("features");

                String titulo = null;
                String link = null;
                String coordenadas = null;
                Monumento monumento = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    titulo = jsonArray.getJSONObject(i).getJSONObject("properties").getString("title");
                    link = jsonArray.getJSONObject(i).getJSONObject("properties").getString("link");
                    coordenadas = jsonArray.getJSONObject(i).getJSONObject("geometry").getString("coordinates");
                    coordenadas = coordenadas.substring(1, coordenadas.length() - 1);
                    String latlong[] = coordenadas.split(",");

                    monumento = new Monumento();
                    monumento.setTitulo(titulo);
                    monumento.setLink(link);
                    monumento.setLatitud(Float.parseFloat(latlong[0]));
                    monumento.setLongitud(Float.parseFloat(latlong[1]));
                    listaMonumentos.add(monumento);
                }

            } catch (ClientProtocolException cpe) {
                cpe.printStackTrace();
                error = true;
            } catch (IOException ioe) {
                ioe.printStackTrace();
                error = true;
            } catch (JSONException jse) {
                jse.printStackTrace();
                error = true;
            }

            return null;
        }

        /**
         * Método que se ejecuta si la tarea es cancelada antes de terminar
         */
        @Override
        protected void onCancelled() {
            super.onCancelled();
            adapter.clear();
            listaMonumentos = new ArrayList<Monumento>();
        }

        /**
         * Método que se ejecuta durante el progreso de la tarea
         * @param progreso
         */
        @Override
        protected void onProgressUpdate(Void... progreso) {
            super.onProgressUpdate(progreso);

            adapter.notifyDataSetChanged();
        }

        /**
         * Método ejecutado automáticamente justo antes de lanzar la tarea en segundo plano
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(ListadoMonumentos.this);
            dialog.setTitle(R.string.mensaje_cargando);
            dialog.show();
        }

        /**
         * Método ejecutado automáticamente justo después de terminar la parte en segundo plano
         * Es la parte donde podemos interactuar con el UI para notificar lo sucedido al usuario
         * @param resultado
         */
        @Override
        protected void onPostExecute(Void resultado) {
            super.onPostExecute(resultado);

            if (error) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.mensaje_error), Toast.LENGTH_SHORT).show();
                return;
            }

            if (dialog != null)
                dialog.dismiss();

            adapter.notifyDataSetChanged();
        }
    }
}
