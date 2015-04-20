package com.ubung.tc.ubungmobile.modelo.persistencia.entidades;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.ubung.tc.ubungmobile.modelo.persistencia.ManejadorPersistencia;

import java.util.ArrayList;

/**
 * Created by cvargasc on 15/04/15.
 */
@ParseClassName(ManejadorPersistencia.USUARIO)
public class Usuario extends ParseUser {
    public static final String LOG_NAME = "Usuario";
// -----------------------------------------------------
// CONSTANTES PARA LAS LLAVES DE LOS ATRIBUTOS
// -----------------------------------------------------
    public static final String NOMBRE_USUARIO = "username";
    private static final String DEPORTE = "deporte";
    private static final String NUM_CELULAR = "numCelular";
    private static final String INSCRITO_A_LOS_EVENTOS = "inscritoALosEventos";

// -----------------------------------------------------
// CONSTRUCTORES
// -----------------------------------------------------
    public Usuario() {
        super();
    }

    public Usuario(String nombreUsuario, String contrasena) throws ParseException {
        super();
        setUsername(nombreUsuario);
        setPassword(contrasena);
        signUp();
    }
// -----------------------------------------------------
// MÉTODOS
// -----------------------------------------------------
    public void registrarInscripcionAEvento(Evento evento) {
        ParseRelation<Evento> eventosSeHaInscrito = getRelation(INSCRITO_A_LOS_EVENTOS);
        eventosSeHaInscrito.add(evento);
        saveInBackground();
    }

// -----------------------------------------------------
// SETTERS
// -----------------------------------------------------
    public void setNombreUsuario(String nombreUsuario) {
        setUsername(nombreUsuario);
        saveEventually();
    }

    public void setDeporte(Deporte deporte) {
        put(DEPORTE,deporte);
        saveEventually();
    }

    public void setNumCelular(long numCelular) {
        put(NUM_CELULAR,numCelular);
        saveEventually();
    }

// -----------------------------------------------------
// GETTERS
// -----------------------------------------------------
    public String getId() {
        return getObjectId();
    }

    public String getNombreUsuario() {
        return getUsername();
    }

    public Deporte getDeporte() throws ParseException {
        ParseQuery<Deporte> query = ParseQuery.getQuery(ManejadorPersistencia.DEPORTE);
        Deporte deporte =  query.getFirst();
        try {
            deporte = getParseObject(DEPORTE).fetch();
        } catch (NullPointerException e) {
            Log.e(LOG_NAME+"getDeporte", "Para este usuario no se ha definido el deporte ::"+e.getMessage());
        }
        return deporte;
    }

    public long getNumCelular() throws ParseException {
        return getLong(NUM_CELULAR);
    }

    public ArrayList<Evento> getEventosSeHaInscrito() throws ParseException {
        ParseRelation<Evento> eventosSeHaInscrito = getRelation(INSCRITO_A_LOS_EVENTOS);
        ParseQuery<Evento> query = eventosSeHaInscrito.getQuery();
        return new ArrayList<>(query.find());
    }

}
