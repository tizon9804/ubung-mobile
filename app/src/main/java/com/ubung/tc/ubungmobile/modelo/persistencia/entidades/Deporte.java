package com.ubung.tc.ubungmobile.modelo.persistencia.entidades;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.ubung.tc.ubungmobile.modelo.persistencia.ManejadorPersistencia;

/**
 * Created by cvargasc on 15/04/15.
 */
@ParseClassName(ManejadorPersistencia.DEPORTE)
public class Deporte extends ParseObject {

// -----------------------------------------------------
// CONSTANTES PARA LAS LLAVES DE LOS ATRIBUTOS
// -----------------------------------------------------
    private static final String NOMBRE = "nombre";
    private static final String NOMBRE_ARCHIVO_IMAGEN = "nombreArchivoImagen";
    private static final String DESCRIPCION = "descripcion";

// -----------------------------------------------------
// GETTERS
// -----------------------------------------------------
    public String getId() {
        return getObjectId();
    }

    public String getNombre() {
        return getString(NOMBRE);
    }

    public String getNombreArchivoImagen() {
        return getString(NOMBRE_ARCHIVO_IMAGEN);
    }

    public String getDescripcion() {
        return getString(DESCRIPCION);
    }
    
}
