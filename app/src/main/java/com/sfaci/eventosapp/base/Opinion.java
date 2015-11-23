package com.sfaci.eventosapp.base;

import java.util.Date;

/**
 * Clase para representar a las opiniones de los Usuarios
 *
 * TODO Asociarlas a un monumento
 *
 * @author Santiago Faci
 * @version curso 2015-2016
 */
public class Opinion {

    private int id;
    private String titulo;
    private String texto;
    private Date fecha;
    private int puntuacion;

    public Opinion() {

    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }
}
