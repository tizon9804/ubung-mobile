package com.ubung.tc.ubungmobile.modelo;

import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Deporte;

import java.util.ArrayList;

/*
Metodos que consumira la interfaz
 */
public interface InterfazUbung {

    /* Devuelve todos los deportes */
    public ArrayList<Deporte> darDeportes();

    /* Devuelve el deporte dado su id */
    public Deporte darDeporte(int id);
}
