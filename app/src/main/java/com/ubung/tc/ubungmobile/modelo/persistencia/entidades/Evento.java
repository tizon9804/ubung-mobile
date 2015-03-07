package com.ubung.tc.ubungmobile.modelo.persistencia.entidades;

import android.util.Log;

import com.ubung.tc.ubungmobile.modelo.persistencia.Tupla;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by cvargasc on 1/03/15.
 */
public class Evento {

    protected static final String LOG_NAME = "Evento";

    private int id;
    private Date fechaHora;
    private Zona zona;
    private Deporte deporte;
    private Usuario organizador;
    protected InscritosEvento inscritosEvento;

    private Date fechaCreacion;

    public Evento(int id, Date fechaHora, Zona zona, Deporte deporte, Usuario organizador) {
        this.id = id;
        this.fechaHora = fechaHora;
        this.zona = zona;
        this.deporte = deporte;
        this.organizador = organizador;
    }

    protected Evento(int id, Date fechaHora, Date fechaCreacion) {
        this.id = id;
        this.fechaHora = fechaHora;
        this.fechaCreacion = fechaCreacion;
    }

    public int getId() {
        return id;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Zona getZona() {
        return zona;
    }

    public void setZona(Zona zona) {
        this.zona = zona;
    }

    public Deporte getDeporte() {
        return deporte;
    }

    public void setDeporte(Deporte deporte) {
        this.deporte = deporte;
    }

    public Usuario getOrganizador() {
        return organizador;
    }

    public void setOrganizador(Usuario organizador) {
        this.organizador = organizador;
    }

    /**
     * Recupera los usuarios inscritos a este evento
     * @return un ArrayList vacío en caso que no se hayan inscritos usuarios, null en caso que este
     * evento no haya sido instanciado desde el modelo.
     */
    public ArrayList<Tupla<Usuario, Date>> getInscritos() {
        if (inscritosEvento == null) {
            Log.e(LOG_NAME+"getInsc", "No debería estar llamando este método desde aquí... el " +
                    "arreglo de inscritos no está inicializado, lo cual implica que este objeto " +
                    "no fue instanciado desde el modelo");
        }
        return inscritosEvento.getInscritosEvento();
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }
}
