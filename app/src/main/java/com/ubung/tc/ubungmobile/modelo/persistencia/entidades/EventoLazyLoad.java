package com.ubung.tc.ubungmobile.modelo.persistencia.entidades;

import android.util.Log;

import java.util.Date;

/**
 * Created by cvargasc on 7/03/15.
 */
public class EventoLazyLoad extends Evento {
    private ManejadorPersistencia manejadorPersistencia;

    private int idZona;
    private int idDeporte;
    private int idOrganizador;

    /**
     * Permite a la clase instanciarse con una referencia directa al ManejadorPersistencia para
     * implementar LazyLoad de los demás objetos asociados a la clase
     * @param manejadorPersistencia Referencia al ManejadorPersistencia que instació la clase
     */
    protected EventoLazyLoad(int id, Date fechaHora, int idZona, int idDeporte, int idOrganizador,
                     Date fechaCreacion, ManejadorPersistencia manejadorPersistencia) {
        super(id, fechaHora, fechaCreacion);

        this.manejadorPersistencia = manejadorPersistencia;

        this.idZona = idZona;
        this.idDeporte = idDeporte;
        this.idOrganizador = idOrganizador;

        this.inscritosEvento = new InscritosEvento(this, manejadorPersistencia);
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

    @Override
    public void setZona(Zona zona) {
        idZona = zona.getId();
        super.setZona(zona);
    }

    @Override
    public Zona getZona() {
        if (super.getZona() == null) {
            Log.i(LOG_NAME + "getZona", "Obteniendo zona a través de LazyLoad");
            super.setZona(manejadorPersistencia.darZona(idZona));
        }
        return super.getZona();
    }

    @Override
    public void setOrganizador(Usuario organizador) {
        idOrganizador = organizador.getId();
        super.setOrganizador(organizador);
    }

    @Override
    public Usuario getOrganizador() {
        if (super.getOrganizador() == null) {
            Log.i(LOG_NAME + "getOrganizador", "Obteniendo organizador a través de LazyLoad");
            super.setOrganizador(manejadorPersistencia.darUsuario(idOrganizador));
        }
        return super.getOrganizador();
    }
}
