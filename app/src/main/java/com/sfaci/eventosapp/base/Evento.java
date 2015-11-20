package com.sfaci.eventosapp.base;

import java.util.Date;

/**
 * Clave Evento
 * @author Santiago Faci
 * @version curso 2015-2016
 */
public class Evento {

    private long id;
    private String nombre;
    private String descripcion;
    private String direccion;
    private float precio;
    private Date fecha;
    private int aforo;

    public Evento() {

    }

    public long getId() { return id; }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getAforo() {
        return aforo;
    }

    public void setAforo(int aforo) {
        this.aforo = aforo;
    }
}
