package com.ubung.tc.ubungmobile.modelo.persistencia.entidades;

import android.util.Log;

/**
 * Created by cvargasc on 7/03/15.
 */
public class UsuarioLazyLoad extends Usuario {
    private ManejadorPersistencia manejadorPersistencia;

    private long idDeporte;

    /**
     * Permite a la clase instanciarse con una referencia directa al ManejadorPersistencia para
     * implementar LazyLoad de los demás objetos asociados a la clase
     * @param manejadorPersistencia Referencia al ManejadorPersistencia que instació la clase
     */
    protected UsuarioLazyLoad(long id, String nombreUsuario, long idDeporte, ManejadorPersistencia manejadorPersistencia) {
        super(id,nombreUsuario);
        this.idDeporte = idDeporte;
        this.manejadorPersistencia = manejadorPersistencia;
    }

    @Override
    public void setDeporte(Deporte deporte) {
        idDeporte = deporte.getId();
        super.setDeporte(deporte);
    }

    @Override
    public Deporte getDeporte() {
        if (super.getDeporte() == null) {
            Log.i(LOG_NAME + "getDeporte", "Obteniendo deporte a través de LazyLoad");
            super.setDeporte(manejadorPersistencia.darDeporte(idDeporte));
        }
        return super.getDeporte();
    }

}
