package com.ubung.tc.ubungmobile.modelo.persistencia.Parse;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Deporte;

import java.util.UUID;

/**
 * Created by cvargasc on 9/03/15.
 */
@ParseClassName("ParseUsuario")
public class ParseUsuario extends ParseObject {

    public static final String ID = "id";
    public static final String NOMBRE_USUARIO = "nombreUsuario";





    public long getId() {
        return getLong(ID);
    }

    public void setId(long id) {
        put(ID,id);
    }

    public String getNombreUsuario() {
        return getString(NOMBRE_USUARIO);
    }

    public void setNombreUsuario(String nombreUsuario) {
        put(NOMBRE_USUARIO,nombreUsuario);
    }


    public void setUuidString() {
        UUID uuid = UUID.randomUUID();
        put("uuid", uuid.toString());
    }

    public String getUuidString() {
        return getString("uuid");
    }

    public static ParseQuery<ParseUsuario> getQuery() {
        return ParseQuery.getQuery(ParseUsuario.class);
    }

}
