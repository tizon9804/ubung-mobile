package com.ubung.tc.ubungmobile.modelo.persistencia;

import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.ubung.tc.ubungmobile.modelo.Persistencia;
import com.ubung.tc.ubungmobile.modelo.Singleton;
import com.ubung.tc.ubungmobile.modelo.excepciones.ExcepcionPersistencia;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.*;

import java.util.ArrayList;

/**
 * Created by cvargasc on 15/04/15.
 */
public class ManejadorPersistencia implements Persistencia {

    public static final String LOG_NAME = "MnjdPersist";

    public static final String DEPORTE = "deportes";
    public static final String EVENTO = "eventos";
    public static final String ZONA = "zonas";
    public static final String USUARIO = "_User";

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

    /**
     * Sincroniza todos los elementos en el almacenamiento local
     */
    public void actualizarCacheLocal() {
        try {
            Log.i(LOG_NAME + ".cacheLocal", "Realizando cache local de los DEPORTES...");
            ParseQuery<Deporte> deportes = ParseQuery.getQuery(ManejadorPersistencia.DEPORTE);
            ParseObject.pinAllInBackground(deportes.find());
            Log.i(LOG_NAME + ".cacheLocal", "Realizando cache local de las ZONAS...");
            ParseQuery<Zona> zonas = ParseQuery.getQuery(ManejadorPersistencia.ZONA);
            ParseObject.pinAllInBackground(zonas.find());
            Log.i(LOG_NAME + ".cacheLocal", "Realizando cache local de los EVENTOS...");
            ParseQuery<Evento> eventos = ParseQuery.getQuery(ManejadorPersistencia.EVENTO);
            ParseObject.pinAllInBackground(eventos.find());
            Log.i(LOG_NAME + ".cacheLocal", "Realizando cache local de los USUARIOS...");
            ParseQuery<Usuario> usuarios = ParseQuery.getQuery(ManejadorPersistencia.USUARIO);
            ParseObject.pinAllInBackground(usuarios.find());
        } catch (ParseException e) {
            Log.e(LOG_NAME + ".cacheLocal", "Error al actualizar el cache local :: "+e.getMessage());
        }
    }

    @Override
    public ArrayList<Deporte> darDeportes() throws ParseException {
        ParseQuery<Deporte> query = ParseQuery.getQuery(ManejadorPersistencia.DEPORTE);
//        query.fromLocalDatastore();
        return new ArrayList<>(query.find());
    }

    @Override
    public Deporte darDeporte(String id) throws ParseException {
        ParseQuery<Deporte> query = ParseQuery.getQuery(ManejadorPersistencia.DEPORTE);
//        query.fromLocalDatastore();
        return query.get(id);
    }

    @Override
    public ArrayList<Usuario> darUsuarios() throws ParseException {
        ParseQuery<Usuario> query = ParseQuery.getQuery(ManejadorPersistencia.USUARIO);
//        query.fromLocalDatastore();
        return new ArrayList<>(query.find());
    }

    @Override
    public Usuario darUsuario(String id) throws ParseException {
        ParseQuery<Usuario> query = ParseQuery.getQuery(ManejadorPersistencia.USUARIO);
//        query.fromLocalDatastore();
        return query.get(id);
    }

    @Override
    public Usuario buscarUsuario(String nombreUsuario) throws ParseException {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
//        query.fromLocalDatastore();
        query.whereEqualTo(Usuario.NOMBRE_USUARIO, nombreUsuario);
        return (Usuario) query.getFirst();
    }

    @Override
    public ArrayList<Zona> darZonas() throws ParseException {
        ParseQuery<Zona> query = ParseQuery.getQuery(ManejadorPersistencia.ZONA);
//        query.fromLocalDatastore();
        return new ArrayList<>(query.find());
    }

    @Override
    public Zona darZona(String id) throws ParseException {
        ParseQuery<Zona> query = ParseQuery.getQuery(ManejadorPersistencia.ZONA);
//        query.fromLocalDatastore();
        return query.get(id);
    }

    //@Override
    public void actualizarEvento(Evento evento) throws ExcepcionPersistencia {
        //ToDo
    }

    @Override
    public ArrayList<Evento> darEventos() throws ParseException {
        ParseQuery<Evento> query = ParseQuery.getQuery(ManejadorPersistencia.EVENTO);
//        query.fromLocalDatastore();
        return new ArrayList<>(query.find());
    }

    @Override
    public ArrayList<Evento> buscarEventos(String idZona) throws ParseException {
        ParseQuery<Evento> query = ParseQuery.getQuery(ManejadorPersistencia.EVENTO);
//        query.fromLocalDatastore();
        query.whereEqualTo(Evento.ZONA, darZona(idZona));
        return new ArrayList<>(query.find());
    }

    @Override
    public Evento darEvento(String id) throws ParseException {
        ParseQuery<Evento> query = ParseQuery.getQuery(ManejadorPersistencia.EVENTO);
//        query.fromLocalDatastore();
        return query.get(id);
    }
}
