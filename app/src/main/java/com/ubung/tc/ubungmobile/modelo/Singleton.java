package com.ubung.tc.ubungmobile.modelo;


/*
Implementacion de los metodos de Ubung
 */

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.ubung.tc.ubungmobile.modelo.excepciones.ExcepcionPersistencia;
import com.ubung.tc.ubungmobile.modelo.persistencia.InterfazPersistencia;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Deporte;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Evento;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.ManejadorPersistencia;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Usuario;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Zona;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

public class Singleton implements InterfazUbung {

// -----------------------------------------------------
// CONSTANTES
// -----------------------------------------------------
    public static final String LOG_NAME = "Singleton";

    public static final String NOMBRE_ARCHIVO_CONF = "config.properties";
    public static final String CONF_ID_PROPIETARIO = "idPropietario";
// -----------------------------------------------------
// ATRIBUTOS
// -----------------------------------------------------
    private static Singleton singleton = new Singleton();
    private Context context;
    private Properties configuracion;

    private Usuario propietario;

    private InterfazPersistencia manejadorPersistencia;

// -----------------------------------------------------
// CONSTRUCTOR (patrón singleton)
// -----------------------------------------------------
    public static Singleton getInstance() {
        return singleton;
    }

// -----------------------------------------------------
// MÉTODOS
// -----------------------------------------------------
    public void inicializar(Context context) {
        if (this.context == null) {
            Log.i(LOG_NAME+".inicializar()", "Inicializando Singleton...");
            Log.i(LOG_NAME+".inicializar()", "Definiendo contexto...");
            this.context = context;

            Log.i(LOG_NAME+".inicializar()", "Cargando archivo de configuración...");
            this.configuracion = new Properties();
            AssetManager assetManager = context.getAssets();

            try {
                InputStream inputStream = assetManager.open(Singleton.NOMBRE_ARCHIVO_CONF);
                configuracion.load(inputStream);
            } catch (IOException e) {
                Log.e(LOG_NAME+".inicializar()", "Error al cargar el archivo '" + NOMBRE_ARCHIVO_CONF + "' con la configuración del programa: " + e.toString());
            }

            Log.i(LOG_NAME+".inicializar()", "Instanciando manejadorPersistencia...");
            this.manejadorPersistencia = new ManejadorPersistencia(this);

            Log.i(LOG_NAME+".inicializar()", "Recuperando la infomación del usuario...");
            this.propietario = this.manejadorPersistencia.darUsuario(configuracion.getProperty(CONF_ID_PROPIETARIO));
            if (this.propietario == null) Log.w(LOG_NAME+".inicializar()", "Usuario no encontrado...");
            else Log.i(LOG_NAME+".inicializar()", "Restableciendo la información de "+this.propietario.getNombreUsuario());

        } else
            Log.w(LOG_NAME+".inicializar()", "Está tratanto de volver a inicializar un Singleton ya inicializado!");

    }

    public Context darContexto() {
        return this.context;
    }

    public Properties darConfiguracion() {
        return this.configuracion;
    }

// -----------------------------------------------------
// MÉTODOS INTERFAZ UBUNG
// -----------------------------------------------------
    @Override
    public Usuario darPropietario() {
        return propietario;
    }

    @Override
    public void modificarPropietario(String nombreUsuario, Deporte deporte) throws ExcepcionPersistencia {
        if (propietario != null) {
            if (nombreUsuario != null) {
                Log.i(LOG_NAME+".modifProp","Actualizando nombre del propietario desde "+this.propietario.getNombreUsuario()
                        +" a "+nombreUsuario);
                this.propietario.setNombreUsuario(nombreUsuario);
            }
            if (deporte != null) {
                Log.i(LOG_NAME+".modifProp","Actualizando deporte del propietario desde "+this.propietario.getDeporte().getNombre()
                        +" a "+deporte.getNombre());
                this.propietario.setDeporte(deporte);
            }
            manejadorPersistencia.actualizarUsuario(propietario);
        } else {
            Log.i(LOG_NAME+".modifProp","Definiendo propietario...");
            this.propietario = this.manejadorPersistencia.darUsuario(nombreUsuario);
            if(this.propietario == null) {
                Log.w(LOG_NAME+".modifProp","Propietario no encontrado como usuario! Creando nuevo usuario...");
                this.propietario = this.manejadorPersistencia.darUsuario(this.manejadorPersistencia.crearUsuario(nombreUsuario, deporte));
            } else Log.i(LOG_NAME+".modifProp","Propietario ya existe como usuario! Definiendo...");
            this.configuracion.setProperty(CONF_ID_PROPIETARIO,""+this.propietario.getId());
        }

    }

// -----------------------------------------------------
// MÉTODOS INTERFAZ PERSISTENCIA
// -----------------------------------------------------

    @Override
    public ArrayList<Deporte> darDeportes() {
        return manejadorPersistencia.darDeportes();
    }

    @Override
    public Deporte darDeporte(int id) {
        return manejadorPersistencia.darDeporte(id);
    }

    @Override
    public int crearUsuario(String nombreUsuario, Deporte deporte) throws ExcepcionPersistencia {
        return manejadorPersistencia.crearUsuario(nombreUsuario, deporte);
    }

    @Override
    public void actualizarUsuario(Usuario usuario) throws ExcepcionPersistencia {
        manejadorPersistencia.actualizarUsuario(usuario);
    }

    @Override
    public ArrayList<Usuario> darUsuarios() {
        return manejadorPersistencia.darUsuarios();
    }

    @Override
    public Usuario darUsuario(int id) {
        return manejadorPersistencia.darUsuario(id);
    }

    @Override
    public Usuario darUsuario(String nombreUsuario) {
        return manejadorPersistencia.darUsuario(nombreUsuario);
    }

    @Override
    public ArrayList<Zona> darZonas() {
        return manejadorPersistencia.darZonas();
    }

    @Override
    public Zona darZona(int id) {
        return manejadorPersistencia.darZona(id);
    }

    @Override
    public int crearEvento(Date fechaHora, Zona zona, Deporte deporte, Usuario organizador) {
        return manejadorPersistencia.crearEvento(fechaHora, zona, deporte, organizador );
    }

    @Override
    public void actualizarEvento(Evento evento) throws ExcepcionPersistencia {
        manejadorPersistencia.actualizarEvento(evento);
    }

    @Override
    public ArrayList<Evento> darEventos() {
        return manejadorPersistencia.darEventos();
    }

    @Override
    public ArrayList<Evento> darEventos(int idZona) {
        return manejadorPersistencia.darEventos(idZona);
    }

    @Override
    public Evento darEvento(int id) {
        return manejadorPersistencia.darEvento(id);
    }
}
