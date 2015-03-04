package com.ubung.tc.ubungmobile.modelo.persistencia.entidades;

/**
 * Created by cvargasc on 1/03/15.
 */
public class Deporte {

    private int id;
    private String nombre;
    private String nombreArchivoImagen;
    private String descripcion;

    public Deporte(int id, String nombre, String nombreArchivoImagen, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.nombreArchivoImagen = nombreArchivoImagen;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getNombreArchivoImagen() {
        return nombreArchivoImagen;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
