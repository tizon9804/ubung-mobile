package com.ubung.tc.ubungmobile.modelo.persistencia;

import com.ubung.tc.ubungmobile.modelo.Singleton;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Deporte;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Evento;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Usuario;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Zona;

import java.util.ArrayList;

/**
 * Created by cvargasc on 1/03/15.
 */
public class ManejadorPersistencia {

    // -----------------------------------------------------
// ATRIBUTOS
// -----------------------------------------------------
    private Singleton singleton;
    private ManejadorBD manejadorBD;

    // -----------------------------------------------------
// CONSTRUCTOR
// -----------------------------------------------------
    public ManejadorPersistencia(Singleton singleton) {
        this.singleton = singleton;
        manejadorBD = new ManejadorBD(singleton);
    }

    // -----------------------------------------------------
// MÉTODOS
// -----------------------------------------------------
    public Deporte darDeporte(int id) {
        return null;
    }

    public Evento darEvento(int id) {
        return null;
    }

    public Usuario darUsuario(int id) {
        return null;
    }

    public Zona darZona(int id) {
        return null;
    }

    public ArrayList<Deporte> darDeportes() {
        return null;
    }

    public ArrayList<Zona> darZonas() {
        return null;
    }
}
