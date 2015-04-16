package com.ubung.tc.ubungmobile.modelo.persistencia;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.ubung.tc.ubungmobile.modelo.Persistencia;
import com.ubung.tc.ubungmobile.modelo.Singleton;
import com.ubung.tc.ubungmobile.modelo.excepciones.ExcepcionPersistencia;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.*;

import java.util.ArrayList;

/**
 * Created by cvargasc on 15/04/15.
 */
public class ManejadorPersistencia implements Persistencia {

    public static final String DEPORTE = "deporte";
    public static final String EVENTO = "evento";
    public static final String ZONA = "zona";
    public static final String USUARIO = "usuario";

    private Singleton singleton;

    public ManejadorPersistencia(Singleton singleton) {
        this.singleton = singleton;
    }

    public void registrarSubclasesParseObject() {
        ParseObject.registerSubclass(Usuario.class);
        ParseObject.registerSubclass(Zona.class);
        ParseObject.registerSubclass(Deporte.class);
        ParseObject.registerSubclass(Evento.class);
    }

    @Override
    public ArrayList<Deporte> darDeportes() throws ParseException {
        ParseQuery<Deporte> query = ParseQuery.getQuery(ManejadorPersistencia.DEPORTE);
        ArrayList<Deporte> respuesta = new ArrayList<Deporte>(query.find());
        return respuesta;
    }

    @Override
    public Deporte darDeporte(String id) throws ParseException {
        ParseQuery<Deporte> query = ParseQuery.getQuery(ManejadorPersistencia.DEPORTE);
        return query.get(id);
    }

    @Override
    public ArrayList<Usuario> darUsuarios() throws ParseException {
        ParseQuery<Usuario> query = ParseQuery.getQuery(ManejadorPersistencia.USUARIO);
        ArrayList<Usuario> respuesta = new ArrayList<Usuario>(query.find());
        return respuesta;
    }

    @Override
    public Usuario darUsuario(String id) {
        return null;
    }

    @Override
    public Usuario buscarUsuario(String nombreUsuario) {
        return null;
    }

    @Override
    public ArrayList<Zona> darZonas() throws ParseException {
        ParseQuery<Zona> query = ParseQuery.getQuery(ManejadorPersistencia.ZONA);
        ArrayList<Zona> respuesta = new ArrayList<Zona>(query.find());
        return respuesta;
    }

    @Override
    public Zona darZona(String id) {
        return null;
    }

    @Override
    public void actualizarEvento(Evento evento) throws ExcepcionPersistencia {

    }

    @Override
    public ArrayList<Evento> darEventos() {
        return null;
    }

    @Override
    public ArrayList<Evento> buscarEventos(String idZona) {
        return null;
    }

    @Override
    public Evento darEvento(String id) {
        return null;
    }
}
