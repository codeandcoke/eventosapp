package com.sfaci.eventosapp.base;

/**
 * Clave Monumento
 * @author Santiago Faci
 * @version curso 2015-2016
 */
public class Monumento {

    private String titulo;
    private String link;
    private float latitud;
    private float longitud;

    public Monumento() {

    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public float getLatitud() {
        return latitud;
    }

    public void setLatitud(float latitud) {
        this.latitud = latitud;
    }

    public float getLongitud() {
        return longitud;
    }

    public void setLongitud(float longitud) {
        this.longitud = longitud;
    }
}
