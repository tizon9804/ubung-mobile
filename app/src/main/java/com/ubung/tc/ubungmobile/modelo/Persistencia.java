package com.ubung.tc.ubungmobile.modelo;

import com.parse.ParseException;
import com.ubung.tc.ubungmobile.modelo.excepciones.ExcepcionPersistencia;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Deporte;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Evento;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Usuario;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Zona;

import java.util.ArrayList;

/**
 * Created by cvargasc on 4/03/15.
 */
public interface Persistencia {

    /**
     * Devuelve todos los deportes
     * @return Arreglo con todos los deportes registrados, el arreglo estará vacío si no hay ningún
     * deporte registrado en la base de datos.
     */
    public ArrayList<Deporte> darDeportes() throws ParseException;

    /**
     * Devuelve un deporte dado su id.
     * @param id El identificador del deporte en la base de datos
     * @return El objeto deporte del id  especificado, null si no se encuentra dicho id en la base
     * de datos.
     */
    public Deporte darDeporte(String id) throws ParseException;

    /**
     * Devuelve todos los usuarios
     * @return Arreglo con todos los usuarios registrados, el arreglo estará vacío si no hay ningún
     * usuario registrado en la base de datos.
     */
    public ArrayList<Usuario> darUsuarios() throws ParseException;

    /**
     * Devuelve un usuario dado su id.
     * @param id El identificado del usuario en la base de datos.
     * @return El usuario asociado con el id especificado. Null si no existe ningún usuario
     * con ese id.
     */
    public Usuario darUsuario(String id) throws ParseException;

    /**
     * Devuelve un usuario dado su nombre de usuario
     * @param nombreUsuario El nombre del usuario en la base de datos
     * @return El usuario con el nombre especificado. Null si no existe ningún usuario con el
     * nombre de usuario especificado.
     */
    public Usuario buscarUsuario(String nombreUsuario) throws ParseException;

    /**
     * Devuelve todas las zonas
     * @return Arreglo con todas las zonas registradas, el arreglo estará vacío si no hay ninguna
     * zona en la base de datos.
     */
    public ArrayList<Zona> darZonas() throws ParseException;

    /**
     * Devuelve la zona dado su id.
     * @param id El identificado de la zona en la base de datos.
     * @return La zona con el id especificado. Null si no existe ninguna zona con ese id
     */
    public Zona darZona(String id) throws ParseException;

    /**
     * Actualiza un evento existente en la aplicación. El objeto evento debe tener su identificador
     * definido. Se actualizan todos los atributos del evento en la base de datos por aquellos
     * contenidos en el objeto.
     * @param evento objeto con la información del evento a actualizar
     * @throws ExcepcionPersistencia en caso que no se actualice ningún evento
     */
    public void actualizarEvento(Evento evento) throws ExcepcionPersistencia;

    /**
     * Devuelve todos los eventos
     * @return Arreglo con todos los eventos registrados, el arreglo estará vacío si no hay ningún
     * evento registrado en la base de datos.
     */
    public ArrayList<Evento> darEventos() throws ParseException;

    /**
     * Devuelve todos los eventos asociados a la zona pasada como parámetro
     * @param idZona El id de la zona para la cual se quieren consultar los eventos
     * @return Arreglo con todos los eventos registrados para la zona especificada, el arreglo
     * estará vacío si no hay ningún evento registrado para esa zona en la base de datos.
     */
    public ArrayList<Evento> buscarEventos(String idZona) throws ParseException;

    /**
     * Devuelve el evento dado su id
     * @param id El identificador del evento en la base de datos.
     * @return El evento con el id especificado. Null si no existe ningún evento con ese id.
     */
    public Evento darEvento(String id) throws ParseException;
}
