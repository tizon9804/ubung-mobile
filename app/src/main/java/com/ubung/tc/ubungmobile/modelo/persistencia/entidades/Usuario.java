package com.ubung.tc.ubungmobile.modelo.persistencia.entidades;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.ubung.tc.ubungmobile.modelo.persistencia.ManejadorPersistencia;

/**
 * Created by cvargasc on 15/04/15.
 */
@ParseClassName(ManejadorPersistencia.USUARIO)
public class Usuario extends ParseUser {

// -----------------------------------------------------
// CONSTANTES PARA LAS LLAVES DE LOS ATRIBUTOS
// -----------------------------------------------------
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
        setUsername(nombreUsuario);
        setPassword(contrasena);
        signUp();
    }
// -----------------------------------------------------
// MÉTODOS
// -----------------------------------------------------
    public void registrarInscripcionAEvento(Evento evento) {
        ParseRelation<Evento> eventosSeARegistrado = getRelation(INSCRITO_A_LOS_EVENTOS);
        eventosSeARegistrado.add(evento);
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
        return getParseObject(DEPORTE).fetch();
    }

    public long getNumCelular() throws ParseException {
        return getLong(NUM_CELULAR);
    }

}
