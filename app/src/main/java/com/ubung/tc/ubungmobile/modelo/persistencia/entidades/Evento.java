package com.ubung.tc.ubungmobile.modelo.persistencia.entidades;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by cvargasc on 1/03/15.
 */
public class Evento {

    private int id;
    private Date fechaHora;
    private Zona zona;
    private Deporte deporte;
    private Usuario organizador;
    private ArrayList<Usuario> inscritos;

    private Date fechaCreacion;

    public Evento(int id, Date fechaHora, Zona zona, Deporte deporte, Usuario organizador) {
        this.id = id;
        this.fechaHora = fechaHora;
        this.zona = zona;
        this.deporte = deporte;
        this.organizador = organizador;
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

    public ArrayList<Usuario> getInscritos() {
        return inscritos;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }
}
