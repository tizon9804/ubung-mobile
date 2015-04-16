package com.ubung.tc.ubungmobile.modelo.persistencia.entidades;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.ubung.tc.ubungmobile.modelo.persistencia.ManejadorPersistencia;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by cvargasc on 15/04/15.
 */
@ParseClassName(ManejadorPersistencia.EVENTO)
public class Evento extends ParseObject {

// -----------------------------------------------------
// CONSTANTES PARA LAS LLAVES DE LOS ATRIBUTOS
// -----------------------------------------------------
    private static final String USUARIO_ORGANIZADOR =  "usuarioOrganizador";
    private static final String USUARIOS_INSCRITOS = "usuariosInscritos";
    private static final String FECHA_HORA_EVENTO = "fechaHoraEvento";
    public static final String ZONA = "zona";
    private static final String DEPORTE = "deporte";

// -----------------------------------------------------
// CONSTRUCTORES
// -----------------------------------------------------
    public Evento() {
        super();
    }

    public Evento(Date fechaHora, Zona zona, Deporte deporte) throws ParseException {
        put(USUARIO_ORGANIZADOR, ParseUser.getCurrentUser());
        put(FECHA_HORA_EVENTO, fechaHora);
        put(ZONA,zona);
        put(DEPORTE,deporte);
        save();
    }

// -----------------------------------------------------
// MÉTODOS
// -----------------------------------------------------
    public void inscribirUsuarioAEvento(Usuario usuario) {
        ParseRelation<Usuario> inscritosEvento = getRelation(USUARIOS_INSCRITOS);
        inscritosEvento.add(usuario);
        saveInBackground();
    }

// -----------------------------------------------------
// GETTERS
// -----------------------------------------------------
    public String getId() {
        return getObjectId();
    }

    public Deporte getDeporte() throws ParseException {
        return getParseObject(DEPORTE).fetch();
    }

    public Zona getZona() throws ParseException {
        return getParseObject(ZONA).fetch();
    }

    public Date getFechaHoraEvento() {
        return getDate(FECHA_HORA_EVENTO);
    }

    public Usuario getUsuarioOrganizador() throws ParseException {
        return getParseObject(USUARIO_ORGANIZADOR).fetch();
    }

    public long getCelNotificacion() throws ParseException {
        return getUsuarioOrganizador().getNumCelular();
    }

    public ArrayList<Usuario> getUsuariosInscritos() throws ParseException {
        ParseRelation<Usuario> inscritosEvento = getRelation(USUARIOS_INSCRITOS);
        ParseQuery<Usuario> query = inscritosEvento.getQuery();
        return new ArrayList<>(query.find());
    }

}
