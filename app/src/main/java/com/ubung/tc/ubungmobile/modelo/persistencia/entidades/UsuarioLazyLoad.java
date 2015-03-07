package com.ubung.tc.ubungmobile.modelo.persistencia.entidades;

import android.util.Log;

/**
 * Created by cvargasc on 7/03/15.
 */
public class UsuarioLazyLoad extends Usuario {
    private ManejadorPersistencia manejadorPersistencia;

    private int idDeporte;

    /**
     * Permite a la clase instanciarse con una referencia directa al ManejadorPersistencia para
     * implementar LazyLoad de los demás objetos asociados a la clase
     * @param manejadorPersistencia Referencia al ManejadorPersistencia que instació la clase
     */
    protected UsuarioLazyLoad(int id, String nombreUsuario, int idDeporte, ManejadorPersistencia manejadorPersistencia) {
        super(id,nombreUsuario);
        this.idDeporte = idDeporte;
        this.manejadorPersistencia = manejadorPersistencia;
    }

    @Override
    public Deporte getDeporte() {
        Log.i(LOG_NAME + "getDeporte", "Obteniendo deporte a través de LazyLoad");
        super.setDeporte(manejadorPersistencia.darDeporte(idDeporte));
        return super.getDeporte();
    }

}