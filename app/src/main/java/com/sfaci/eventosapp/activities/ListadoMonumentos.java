package com.sfaci.eventosapp.activities;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

public class ListadoMonumentos extends Activity {

    private MonumentoAdapter adapter;
    private ArrayList<Monumento> listaMonumentos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_monumentos);

        listaMonumentos = new ArrayList<Monumento>();
        adapter = new MonumentoAdapter(this, R.layout.monumento_item, listaMonumentos);
        ListView lvListaMonumentos = (ListView) findViewById(R.id.lvListaMonumentos);
        lvListaMonumentos.setAdapter(adapter);
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

                String nombre = null;
                String descripcion = null;
                String categoria = null;
                String link = null;
                String coordenadas = null;
                Monumento monumento = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    nombre = jsonArray.getJSONObject(i).getJSONObject("properties").getString("title");
                    link = jsonArray.getJSONObject(i).getJSONObject("properties").getString("link");
                    coordenadas = jsonArray.getJSONObject(i).getJSONObject("geometry").getString("coordinates");
                    coordenadas = coordenadas.substring(1, coordenadas.length() - 1);
                    String latlong[] = coordenadas.split(",");

                    monumento = new Monumento();
                    monumento.setTitulo(nombre);
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

        @Override
        protected void onCancelled() {
            super.onCancelled();
            adapter.clear();
            listaMonumentos = new ArrayList<Monumento>();
        }

        @Override
        protected void onProgressUpdate(Void... progreso) {
            super.onProgressUpdate(progreso);

            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(Void resultado) {
            super.onPostExecute(resultado);

            if (error) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.mensaje_error), Toast.LENGTH_SHORT).show();
                return;
            }

            adapter.notifyDataSetChanged();
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.mensaje_ok), Toast.LENGTH_SHORT).show();
        }
    }
}
