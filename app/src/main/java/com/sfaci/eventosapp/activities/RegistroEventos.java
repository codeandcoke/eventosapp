package com.sfaci.eventosapp.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.sfaci.eventosapp.R;
import com.sfaci.eventosapp.base.Evento;
import com.sfaci.eventosapp.database.Database;
import com.sfaci.eventosapp.util.Util;

import java.text.ParseException;

/**
 * Activity para registrar Eventos en la Base de Datos SQLite
 * en el dispositivo
 *
 * @authro Santiago Faci
 * @version curso 2015-2016
 */
public class RegistroEventos extends Activity implements View.OnClickListener {

    private int RESULTADO_CARGA_IMAGEN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_eventos);

        Button btGuardar = (Button) findViewById(R.id.btGuardar);
        btGuardar.setOnClickListener(this);
        Button btCerrar = (Button) findViewById(R.id.btCerrar);
        btCerrar.setOnClickListener(this);
        ImageView ivImagen = (ImageView) findViewById(R.id.ivImagen);
        ivImagen.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registro_eventos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if ((requestCode == RESULTADO_CARGA_IMAGEN) && (resultCode == RESULT_OK) && (data != null)) {
            // Obtiene el Uri de la imagen seleccionada por el usuario
            Uri imagenSeleccionada = data.getData();
            String[] ruta = {MediaStore.Images.Media.DATA };

            // Realiza una consulta a la galería de imágenes solicitando la imagen seleccionada
            Cursor cursor = getContentResolver().query(imagenSeleccionada, ruta, null, null, null);
            cursor.moveToFirst();

            // Obtiene la ruta a la imagen
            int indice = cursor.getColumnIndex(ruta[0]);
            String picturePath = cursor.getString(indice);
            cursor.close();

            // Carga la imagen en la vista ImageView que hay encima del botón
            ImageView imageView = (ImageView) findViewById(R.id.ivImagen);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btGuardar:
                EditText etNombre = (EditText) findViewById(R.id.etNombre);
                EditText etDescripcion = (EditText) findViewById(R.id.etDescripcion);
                EditText etDireccion = (EditText) findViewById(R.id.etDireccion);
                EditText etPrecio = (EditText) findViewById(R.id.etPrecio);
                EditText etFecha = (EditText) findViewById(R.id.etFecha);
                EditText etAforo = (EditText) findViewById(R.id.etAforo);
                ImageView ivImagen = (ImageView) findViewById(R.id.ivImagen);

                if (etPrecio.getText().toString().equals(""))
                    etPrecio.setText("0");

                if (etAforo.getText().toString().equals(""))
                    etAforo.setText("0");

                try {
                    Evento evento = new Evento();
                    evento.setNombre(etNombre.getText().toString());
                    evento.setDescripcion(etDescripcion.getText().toString());
                    evento.setDireccion(etDireccion.getText().toString());
                    evento.setPrecio(Float.parseFloat(etPrecio.getText().toString()));
                    evento.setFecha(Util.parsearFecha(etFecha.getText().toString()));
                    evento.setAforo(Integer.parseInt(etAforo.getText().toString()));
                    evento.setImagen(((BitmapDrawable) ivImagen.getDrawable()).getBitmap());

                    Database db = new Database(this);
                    db.nuevoEvento(evento);
                    Toast.makeText(this, R.string.mensaje_nuevo_evento, Toast.LENGTH_LONG).show();
                    etNombre.setText("");
                    etDescripcion.setText("");
                    etDireccion.setText("");
                    etPrecio.setText("");
                    etFecha.setText("");
                    etAforo.setText("");
                    ivImagen.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher));
                } catch (ParseException pe) {
                    Toast.makeText(this, R.string.mensaje_error_fecha, Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.btCerrar:

                EditText nombre = (EditText) findViewById(R.id.etNombre);
                if (nombre.getText().toString().equals("")) {
                    onBackPressed();
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.mensaje_esta_seguro)
                        .setPositiveButton(R.string.label_si, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                onBackPressed();
                            }
                        })
                        .setNegativeButton(R.string.label_no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.ivImagen:
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULTADO_CARGA_IMAGEN);
                break;
            default:
                break;
        }
    }
}
