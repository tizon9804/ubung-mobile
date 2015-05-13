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
    private static final String ULTIMA_UBICACION = "ultimaUbicacion";
// -----------------------------------------------------
// ATRIBUTOS
// -----------------------------------------------------
    private double[] ultimaUbicacion = null;

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
        double[] ultimaUbicaion = {4.640178,-74.082677,13}; // Centrado en Bogotá con Zoom de 13
        setUltimaUbicacion(ultimaUbicaion);
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
        put(DEPORTE, deporte);
        saveEventually();
    }

    public void setNumCelular(long numCelular) {
        put(NUM_CELULAR,numCelular);
        saveEventually();
    }

    public void setUltimaUbicacion(double[] LatLongZoom) {
        String cadena = LatLongZoom[0]+":"+LatLongZoom[1]+":"+LatLongZoom[2];
        put(ULTIMA_UBICACION,cadena);
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

    public double[] getUltimaUbicacion() {
        if (ultimaUbicacion == null) {
            String cadena = getString(ULTIMA_UBICACION);
            String[] datos = cadena.split(":");
            ultimaUbicacion = new double[3];
            ultimaUbicacion[0] = Double.parseDouble(datos[0]);
            ultimaUbicacion[1] = Double.parseDouble(datos[1]);
            ultimaUbicacion[2] = Double.parseDouble(datos[2]);
        }
        return ultimaUbicacion;
    }
}
