package com.ubung.tc.ubungmobile.modelo;

import com.ubung.tc.ubungmobile.modelo.excepciones.ExcepcionPersistencia;
import com.ubung.tc.ubungmobile.modelo.persistencia.InterfazPersistencia;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Deporte;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Usuario;

/*
Metodos que consumira la interfaz
 */
public interface InterfazUbung extends InterfazPersistencia {

    /**
     * Devuelve el usuario propietario del dispositivo
     * @return el objeto Usuario que corresponde al dispositivo
     */
    public Usuario darPropietario();

    /**
     * Registra el propietario del dispositivo
     * @param nombreUsuario el nombre de usario del propietario del dispositivo
     * @param deporte el deporte que el propietario del dispositivo practica
     * @throws ExcepcionPersistencia en caso que se presente algún error al persistir los cambios
     */
    public void modificarPropietario(String nombreUsuario, Deporte deporte) throws ExcepcionPersistencia;

    /**
     * Permite inscribir al propietario en un evento
     * @param idEvento el id del evento al cual se inscribirá el propietario
     * @throws ExcepcionPersistencia en caso que se presente algún error al persistir los cambios
     */
    public void inscribirseEvento(int idEvento) throws ExcepcionPersistencia;

}
