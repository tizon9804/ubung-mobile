package com.ubung.tc.ubungmobile.modelo.persistencia;

import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.ubung.tc.ubungmobile.modelo.Persistencia;
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
    public static final String USUARIO = "usuarios";


    public ManejadorPersistencia() {
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
    public void inicializar() {
        try {
            //Log.i(LOG_NAME + ".inicializar", "Limpiando el cache local...");
            //ParseObject.unpinAll();
            Log.i(LOG_NAME + ".inicializar", "Realizando cache local de los DEPORTES...");
            ParseQuery<Deporte> deportes = ParseQuery.getQuery(ManejadorPersistencia.DEPORTE);
            deportes.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ONLY);
            ParseObject.pinAll(deportes.find());
            Log.i(LOG_NAME + ".inicializar", "Realizando cache local de las ZONAS...");
            ParseQuery<Zona> zonas = ParseQuery.getQuery(ManejadorPersistencia.ZONA);
            zonas.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ONLY);
            ParseObject.pinAll(zonas.find());
        } catch (ParseException e) {
            Log.e(LOG_NAME + ".inicializar", "Error al actualizar el cache local :: "+e.getMessage());
        }
    }

    @Override
    public ArrayList<Deporte> darDeportes() throws ParseException {
        ParseQuery<Deporte> query = ParseQuery.getQuery(ManejadorPersistencia.DEPORTE);
        ArrayList<Deporte> respuesta = new ArrayList<>(query.find());
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
        ArrayList<Usuario> respuesta = new ArrayList<>(query.find());
        return respuesta;
    }

    @Override
    public Usuario darUsuario(String id) throws ParseException {
        ParseQuery<Usuario> query = ParseQuery.getQuery(ManejadorPersistencia.USUARIO);
        return query.get(id);
    }

    @Override
    public Usuario buscarUsuario(String nombreUsuario) throws ParseException {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo(Usuario.NOMBRE_USUARIO, nombreUsuario);
        return (Usuario) query.getFirst();
    }

    @Override
    public ArrayList<Zona> darZonas() throws ParseException {
        ParseQuery<Zona> query = ParseQuery.getQuery(ManejadorPersistencia.ZONA);
        ArrayList<Zona> respuesta = new ArrayList<>(query.find());
        return respuesta;
    }

    @Override
    public Zona darZona(String id) throws ParseException {
        ParseQuery<Zona> query = ParseQuery.getQuery(ManejadorPersistencia.ZONA);
        return query.get(id);
    }

    @Override
    public void actualizarEvento(Evento evento) throws ExcepcionPersistencia {

    }

    @Override
    public ArrayList<Evento> darEventos() throws ParseException {
        ParseQuery<Evento> query = ParseQuery.getQuery(ManejadorPersistencia.EVENTO);
        ArrayList<Evento> respuesta = new ArrayList<>(query.find());
        return respuesta;
    }

    @Override
    public ArrayList<Evento> buscarEventos(String idZona) throws ParseException {
        ParseQuery<Evento> query = ParseQuery.getQuery(ManejadorPersistencia.EVENTO);
        query.whereEqualTo(Evento.ZONA, darZona(idZona));
        ArrayList<Evento> respuesta = new ArrayList<>(query.find());
        return respuesta;
    }

    @Override
    public Evento darEvento(String id) throws ParseException {
        ParseQuery<Evento> query = ParseQuery.getQuery(ManejadorPersistencia.EVENTO);
        return query.get(id);
    }
}
