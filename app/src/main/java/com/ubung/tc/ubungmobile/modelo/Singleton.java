package com.ubung.tc.ubungmobile.modelo;


/*
Implementacion de los metodos de Ubung
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.util.Log;

import com.ubung.tc.ubungmobile.modelo.excepciones.ExcepcionPersistencia;
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

    public static final String ARCHIVO_CONF_GLB = "config.properties";
    public static final String ARCHIVO_CONF_LOC = "config";

    public static final String CONF_ID_PROPIETARIO = "idPropietario";
// -----------------------------------------------------
// ATRIBUTOS
// -----------------------------------------------------
    private static Singleton singleton = new Singleton();
    private Context context;
    private Properties configuracionGlobal;
    private SharedPreferences configuracionLocal;

    private Usuario propietario;

    private ManejadorPersistencia manejadorPersistencia;

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
            Log.i(LOG_NAME+".inicializar()", System.currentTimeMillis()+" Inicializando Singleton...");
            Log.i(LOG_NAME+".inicializar()", "Definiendo contexto...");
            this.context = context;

            Log.i(LOG_NAME+".inicializar()", "Cargando configuración global...");
            configuracionGlobal = new Properties();
            AssetManager assetManager = context.getAssets();

            try {
                InputStream inputStream = assetManager.open(Singleton.ARCHIVO_CONF_GLB);
                configuracionGlobal.load(inputStream);
                inputStream.close();
            } catch (IOException e) {
                Log.e(LOG_NAME+".inicializar()", "Error al cargar el archivo '" + ARCHIVO_CONF_GLB + "' con la configuración del programa: " + e.toString());
            }

            Log.i(LOG_NAME+".inicializar()", "Instanciando manejadorPersistencia...");
            manejadorPersistencia = new ManejadorPersistencia(this);

            Log.i(LOG_NAME+".inicializar()", "Cargando configuración local...");
            configuracionLocal = context.getSharedPreferences(ARCHIVO_CONF_LOC,Context.MODE_PRIVATE);

            Log.i(LOG_NAME+".inicializar()", "Recuperando la información del usuario "+ configuracionLocal.getInt(CONF_ID_PROPIETARIO, -1)+"...");
            propietario = manejadorPersistencia.darUsuario(configuracionLocal.getInt(CONF_ID_PROPIETARIO,-1));
            if (propietario == null) Log.w(LOG_NAME+".inicializar()", "Usuario no encontrado...");
            else Log.i(LOG_NAME+".inicializar()", "Usuario encontrado, restableciendo la información de "+propietario.getNombreUsuario()+"...");

        } else {
            Log.w(LOG_NAME + ".inicializar()", System.currentTimeMillis()+" Está tratanto de volver a inicializar un Singleton ya inicializado!");
        }
    }

    public Context darContexto() {
        return this.context;
    }

    public Properties darConfiguracion() {
        return this.configuracionGlobal;
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
                Log.i(LOG_NAME+".modifProp","Actualizando nombre del propietario desde "+propietario.getNombreUsuario()
                        +" a "+nombreUsuario);
                propietario.setNombreUsuario(nombreUsuario);
            }
            if (deporte != null) {
                Log.i(LOG_NAME+".modifProp","Actualizando deporte del propietario desde "+propietario.getDeporte().getNombre()
                        +" a "+deporte.getNombre());
                this.propietario.setDeporte(deporte);
            }
            manejadorPersistencia.actualizarUsuario(propietario);
        } else {
            Log.i(LOG_NAME+".modifProp","Definiendo propietario...");
            if(nombreUsuario.length()<1) throw new ExcepcionPersistencia("El nombre de usuario tiene "+nombreUsuario.length()+" caracteres");
            propietario = manejadorPersistencia.darUsuario(nombreUsuario);
            if(propietario == null) {
                Log.w(LOG_NAME+".modifProp","Propietario no encontrado como usuario! Creando nuevo usuario...");
                propietario = manejadorPersistencia.darUsuario(manejadorPersistencia.crearUsuario(nombreUsuario, deporte));
            } else Log.i(LOG_NAME+".modifProp","Propietario ya existe como usuario! Solo fue necesario definirlo...");

            Editor editor = configuracionLocal.edit();
            editor.putLong(CONF_ID_PROPIETARIO, propietario.getId());
            editor.commit();
            Log.i(LOG_NAME+".modifProp","Almacenando propietario en configuracion local...");
        }
    }

    @Override
    public void inscribirseEvento(long idEvento) throws ExcepcionPersistencia {
        manejadorPersistencia.agregarInscritoEvento(idEvento, propietario.getId());
    }

// -----------------------------------------------------
// MÉTODOS INTERFAZ PERSISTENCIA
// -----------------------------------------------------

    @Override
    public ArrayList<Deporte> darDeportes() {
        return manejadorPersistencia.darDeportes();
    }

    @Override
    public Deporte darDeporte(long id) {
        return manejadorPersistencia.darDeporte(id);
    }

    @Override
    public ArrayList<Usuario> darUsuarios() {
        return manejadorPersistencia.darUsuarios();
    }

    @Override
    public Usuario darUsuario(long id) {
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
    public Zona darZona(long id) {
        return manejadorPersistencia.darZona(id);
    }

    @Override
    public int crearEvento(Date fechaHora, Zona zona, Deporte deporte, Usuario organizador)
            throws  ExcepcionPersistencia {
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
    public ArrayList<Evento> darEventos(long idZona) {
        return manejadorPersistencia.darEventos(idZona);
    }

    @Override
    public Evento darEvento(long id) {
        return manejadorPersistencia.darEvento(id);
    }
}
