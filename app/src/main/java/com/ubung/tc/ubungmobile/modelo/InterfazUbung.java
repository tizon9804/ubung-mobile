package com.ubung.tc.ubungmobile.modelo;

import com.ubung.tc.ubungmobile.modelo.persistencia.Deporte;

/*
Metodos que consumira la interfaz
 */
public interface InterfazUbung {


    /*
     * Devuelve los ids de la imagenes de los deportes
     */
    public Integer[] getDeportes();

    /*
     * Dada la posicion del deporte seleccionado dar objeto deporte
     */
    public Deporte getDeporte(int pos);

}
