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
    public void setDeporte(Deporte deporte) {
        idDeporte = deporte.getId();
        super.setDeporte(deporte);
    }

    @Override
    public Deporte getDeporte() {
        super.setDeporte(manejadorPersistencia.darDeporte(idDeporte));
        Log.i(LOG_NAME + "getDeporte", "Deporte "+super.getDeporte().getNombre() +" obtendio a través de LazyLoad");
        return super.getDeporte();
    }

}
