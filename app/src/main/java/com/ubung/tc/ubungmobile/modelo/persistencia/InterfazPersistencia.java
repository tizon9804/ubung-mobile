package com.ubung.tc.ubungmobile.modelo.persistencia;

import com.ubung.tc.ubungmobile.modelo.excepciones.ExcepcionPersistencia;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Deporte;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Usuario;

import java.util.ArrayList;

/**
 * Created by cvargasc on 4/03/15.
 */
public interface InterfazPersistencia {

    /**
     * Devuelve todos los deportes
     * @return Arreglo con todos los deportes registrados, el arreglo estará vacío si no hay ningún
     * deporte registrado en la base de datos.
     */
    public ArrayList<Deporte> darDeportes();

    /**
     * Devuelve un deporte dado su id.
     * @param id El identificador del deporte en la base de datos
     * @return El objeto deporte del id  especificado, null si no se encuentra dicho id en la base
     * de datos.
     */
    public Deporte darDeporte(int id);

    /**
     * Crea un usuario en la aplicación
     * @param usuario El objeto Usuario que se almacenará en la base de datos
     * @return El id del usurio recién creado
     * @throws ExcepcionPersistencia en caso que el usuario ya exista o se presente algún otro error.
     */
    public int crearUsuario(Usuario usuario) throws ExcepcionPersistencia;

    /**
     * Actualiza un usuario existente en la aplicación. El objeto usuario debe tener su identificador
     * definido. Se actualizan todos los atributos del usuario en la base de datos por aquellos
     * contenidos en el objeto.
     * @param usuario objeto con la información del usuario a actualizar
     * @throws ExcepcionPersistencia en caso que no se actualice ningún usuario
     */
    public void actualizarUsuario(Usuario usuario) throws ExcepcionPersistencia;

    /**
     * Devuelve todos los usuarios
     * @return Arreglo con todos los usuarios registrados, el arreglo estará vacío si no hay ningún
     * usuario registrado en la base de datos.
     */
    public ArrayList<Usuario> darUsuarios();

    /**
     * Devuelve un usuario dado su id.
     * @param id El identificado del usuario en la base de datos.
     * @return El usuario asociado con el id especificado
     */
    public Usuario darUsuario(int id);

    /**
     * Devuelve un usuario dado su nombre de usuario
     * @param nombreUsuario El nombre del usuario en la base de datos
     * @return El usuario con el nombre especificado
     */
    public Usuario darUsuario(String nombreUsuario);
}
