package com.ubung.tc.ubungmobile.modelo;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.ubung.tc.ubungmobile.modelo.excepciones.ExcepcionComunicacion;
import com.ubung.tc.ubungmobile.modelo.excepciones.ExcepcionPersistencia;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Deporte;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Usuario;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Zona;

import java.util.Date;

/*
Metodos que consumira la interfaz
 */
public interface Ubung extends Persistencia {

    /**
     * Devuelve el usuario propietario del dispositivo
     * @return el objeto Usuario que corresponde al dispositivo
     */
    public Usuario darPropietario();

    /**
     * Modifica la información del propietario del dispositivo
     * @param nombreUsuario el nombre de usario del propietario del dispositivo
     * @param deporte el deporte que el propietario del dispositivo practica
     * @throws ExcepcionPersistencia en caso que se presente algún error al persistir los cambios
     */
    public void modificarPropietario(String nombreUsuario, Long celular, Deporte deporte) throws Exception;

    /**
     * Permite realizar el login del usuario en la aplicación
     * @param nombreUsuario
     * @param contrasena
     */
    public void logIn(String nombreUsuario, String contrasena);

    /**
     * Permite registrar un nuevo usuario en la aplicación
     * @param nombreUsuario
     * @param contrasena
     */
    public void registrarNuevoUsuario(String nombreUsuario, String contrasena);

    /**
     * Permite inscribir al propietario en un evento
     * @param idEvento el id del evento al cual se inscribirá el propietario
     * @throws ExcepcionPersistencia en caso que el usuario especificado ya se haya inscrito al evento
     * especificado o se presente algún error al persistir los cambios
     * @throws ExcepcionComunicacion en caso que no sea posible enviar el SMS al usuario organizador
     * del evento notificando la inscripción.
     */
    public void inscribirseEvento(String idEvento) throws ExcepcionPersistencia, ExcepcionComunicacion;

    /**
     * Crea un nuevo evento e inscribe al propietario a ese evento
     * @param fechaHora en la que se realizará el evento
     * @param zona en la que se realizará el evento
     * @param deporte que se practicará durante el evento
     * @return el identificador del evento recién creado
     * @throws ExcepcionPersistencia en caso que se presente algún error al persistir los cambios
     */
    public void crearEvento(Date fechaHora, Zona zona, Deporte deporte) throws ParseException;

}