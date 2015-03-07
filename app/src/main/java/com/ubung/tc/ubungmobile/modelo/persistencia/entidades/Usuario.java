package com.ubung.tc.ubungmobile.modelo.persistencia.entidades;


/**
 * Created by cvargasc on 1/03/15.
 */
public class Usuario {

    protected static final String LOG_NAME = "Usuario";

    private int id;
    private String nombreUsuario;
    private Deporte deporte; // Es el deporte seleccionado por el usuario en este momento

    public Usuario(int id, String nombreUsuario, Deporte deporte) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.deporte = deporte;
    }

    protected Usuario(int id, String nombreUsuario) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
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
        return deporte;
    }

    public void setDeporte(Deporte deporte) {
        this.deporte = deporte;
    }
}
