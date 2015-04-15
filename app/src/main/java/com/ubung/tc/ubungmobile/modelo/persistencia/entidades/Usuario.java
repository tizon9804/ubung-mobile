package com.ubung.tc.ubungmobile.modelo.persistencia.entidades;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.ubung.tc.ubungmobile.modelo.persistencia.ManejadorPersistencia;

/**
 * Created by cvargasc on 15/04/15.
 */
@ParseClassName(ManejadorPersistencia.USUARIO)
public class Usuario extends ParseUser {

    private static final String DEPORTE = "deporte";
    private static final String NUM_CELULAR = "numCelular";

    public String getId() {
        return getObjectId();
    }

    public void setNombreUsuario(String nombreUsuario) {
        setUsername(nombreUsuario);
    }

    public String getNombreUsuario() {
        return getUsername();
    }

    public void setDeporte(Deporte deporte) {
        put(DEPORTE,deporte);
        saveEventually();
    }

    public Deporte getDeporte() throws ParseException {
        return getParseObject(DEPORTE).fetch();
    }

    public void setNumCelular(long numCelular) {
        put(NUM_CELULAR,numCelular);
        saveEventually();
    }

    public long getNumCelular() throws ParseException {
        return getLong(NUM_CELULAR);
    }

}
