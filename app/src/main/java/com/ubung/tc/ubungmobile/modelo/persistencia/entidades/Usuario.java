package com.ubung.tc.ubungmobile.modelo.persistencia.entidades;


import android.util.Log;

/**
 * Created by cvargasc on 1/03/15.
 */
public class Usuario {

    private static final String LOG_NAME = "Usuario";

    private int id;
    private String nombreUsuario;
    private Deporte deporte; // El deporte cambia dinámicamente en función de la busqueda del usuario.

    private int idDeporte;
    private ManejadorPersistencia manejadorPersistencia;

    public Usuario(int id, String nombreUsuario, Deporte deporte) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.deporte = deporte;
    }

    /**
     * Permite a la clase instanciarse con una referencia directa al ManejadorPersistencia para
     * implementar LazyLoad de los demás objetos asociados a la clase
     * @param manejadorPersistencia Referencia al ManejadorPersistencia que instació la clase
     */
    protected Usuario(int id, String nombreUsuario, int idDeporte, ManejadorPersistencia manejadorPersistencia) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.idDeporte = idDeporte;
        this.manejadorPersistencia = manejadorPersistencia;
    }

    public int getId() {
        return id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public Deporte getDeporte() {
        if (deporte == null) {
            Log.i(LOG_NAME+"getDeporte", "Obteniendo deporte a través de LazyLoad");
            deporte = manejadorPersistencia.darDeporte(idDeporte);
        }
        return deporte;
    }

    public void setDeporte(Deporte deporte) {
        this.deporte = deporte;
    }
}
