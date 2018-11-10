package com.sfaci.eventosapp.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Métodos de utilidad
 *
 * @author Santiago Faci
 * @version curso 2015-2016
 */
public class Util {

    /**
     * Convierte un Bitmap en un array de bytes
     * @param bitmap
     * @return
     */
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
        return bos.toByteArray();
    }

    /**
     * Convierte un array de bytes en un objeto Bitmap
     * @param bytes
     * @return
     */
    public static Bitmap getBitmap(byte[] bytes) {

        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * Convierte una fecha de Date a String
     * @param fecha
     * @return
     */
    public static String formatearFecha(Date fecha) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(fecha);
    }

    /**
     * Convierte una fecha de String a Date
     * @param fecha
     * @return
     */
    public static Date parsearFecha(String fecha) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.parse(fecha);
    }

    public static String encodeBase64(Bitmap imagen) {

        String code = null;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imagen.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();

        code = Base64.encodeToString(bytes, Base64.URL_SAFE);

        return code;
    }

    public static Bitmap decodeBase64(String imagen) {

        byte[] bytes = Base64.decode(imagen, 0);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
