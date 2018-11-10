package com.sfaci.eventosapp.util;

/**
 * Constantes de la aplicación
 *
 * @author Santiago Faci
 * @version curso 2015-2016
 */
public class Constants {

    public final static String URL = "http://www.zaragoza.es/georref/json/hilo/verconsulta_Piezas?georss_tag_1=-&georss_materiales=-&georss_tematica=-&georss_barrio=-&georss_epoca=-";
    // Para lanzar contra el emulador
    //public final static String SERVER_URL = "http://10.0.2.2:8080";
    // Para lanzar en Tomcat
    public final static String SERVER_URL = "http://192.168.0.5:8080";

    public final static String TABLA_EVENTOS = "eventos";
    public final static String ID = "id";
    public final static String NOMBRE_EVENTO = "titulo";
    public final static String DESCRIPCION_EVENTO = "descripcion";
    public final static String DIRECCION_EVENTO = "direccion";
    public final static String FECHA_EVENTO = "fecha";
    public final static String PRECIO_EVENTO = "precio";
    public final static String AFORO_EVENTO = "aforo";
    public final static String IMAGEN_EVENTO = "imagen";
}
