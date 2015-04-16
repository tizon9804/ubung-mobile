package com.ubung.tc.ubungmobile.modelo.persistencia.entidades;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.ubung.tc.ubungmobile.modelo.persistencia.ManejadorPersistencia;

/**
 * Created by cvargasc on 15/04/15.
 */
@ParseClassName(ManejadorPersistencia.ZONA)
public class Zona extends ParseObject {

// -----------------------------------------------------
// CONSTANTES PARA LAS LLAVES DE LOS ATRIBUTOS
// -----------------------------------------------------
    private static final String NOMBRE = "nombre";
    private static final String LATLONGZOOM = "latlongzoom";
    private static final String RADIO = "radio";

// -----------------------------------------------------
// ATRIBUTOS
// -----------------------------------------------------
    private double[] LatLongZoom = null;


// -----------------------------------------------------
// GETTERS
// -----------------------------------------------------
    public String getId() {
        return getObjectId();
    }

    public String getNombre() {
        return getString(NOMBRE);
    }

    public double[] getLatLongZoom() {
        if (LatLongZoom == null) {
            String cadena = getString(LATLONGZOOM);
            String[] datos = cadena.split(":");
            LatLongZoom = new double[3];
            LatLongZoom[0] = Double.parseDouble(datos[0]);
            LatLongZoom[1] = Double.parseDouble(datos[1]);
            LatLongZoom[2] = Double.parseDouble(datos[2]);
        }
        return LatLongZoom;
    }

    public int getRadio() {
        return getInt(RADIO);
    }

}
