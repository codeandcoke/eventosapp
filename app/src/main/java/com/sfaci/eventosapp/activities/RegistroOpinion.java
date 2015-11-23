package com.sfaci.eventosapp.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sfaci.eventosapp.R;
import static com.sfaci.eventosapp.util.Constants.SERVER_URL;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Activity que registra las opiniones de los usuarios
 * en el servidor a través de un servicio web
 *
 * Puesto que nos comunicamos a través de la red, debemos hacerlo
 * mediante una AsyncTask
 *
 * @author Santiago Faci
 * @version curso 2015-2016
 */
public class RegistroOpinion extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_opinion);

        Button btGuardar = (Button) findViewById(R.id.btGuardar);
        btGuardar.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registro_opinion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btGuardar:
                EditText etTitulo = (EditText) findViewById(R.id.etTitulo);
                EditText etTexto = (EditText) findViewById(R.id.etTexto);
                EditText etPuntuacion = (EditText) findViewById(R.id.etPuntuacion);

                if (etPuntuacion.getText().toString().equals(""))
                    etPuntuacion.setText("-1");

                String titulo = etTitulo.getText().toString();
                String texto = etTexto.getText().toString();
                String puntuacion = etPuntuacion.getText().toString();

                WebService webService = new WebService();
                webService.execute(titulo, texto, puntuacion);

                break;
            default:
                break;
        }
    }

    private class WebService extends AsyncTask<String, Void, Void> {

        private ProgressDialog dialog;

        @Override
        protected Void doInBackground(String... params) {
            // Llamada a un Servicio Web con paso de parámetros
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getForObject(SERVER_URL + "/add_opinion?titulo=" + params[0] + "&texto=" + params[1] + "&puntuacion=" + params[2], Void.class);

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(RegistroOpinion.this);
            dialog.setTitle(R.string.mensaje_enviando);
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            EditText etTitulo = (EditText) findViewById(R.id.etTitulo);
            etTitulo.setText("");
            EditText etTexto = (EditText) findViewById(R.id.etTexto);
            etTexto.setText("");
            EditText etPuntuacion = (EditText) findViewById(R.id.etPuntuacion);
            etPuntuacion.setText("");

            if (dialog != null)
                dialog.dismiss();
        }
    }
}
