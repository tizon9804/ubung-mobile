package com.ubung.tc.ubungmobile.modelo.persistencia.entidades;

/**
 * Created by cvargasc on 2/03/15.
 */
public class Zona {

    private long id;
    private String nombre;
    private double[] latLongZoom;
    private int radio;

    public Zona(long id, String nombre, double[] latLongZoom, int radio) {
        this.id = id;
        this.nombre = nombre;
        this.latLongZoom = latLongZoom;
        this.radio = radio;
    }

    public long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public double[] getLatLongZoom() {
        return latLongZoom;
    }

    public int getRadio() {
        return radio;
    }
}
