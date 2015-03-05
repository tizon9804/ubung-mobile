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
    public static final String NOMBRE_ARCHIVO_CONF = "config.properties";

    // -----------------------------------------------------
// ATRIBUTOS
// -----------------------------------------------------
    private static Singleton singleton = new Singleton();
    private Context context;
    private Properties configuracion;

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
            Log.i("Singleton.inicializar()", "Inicializando Singleton...");
            Log.i("Singleton.inicializar()", "Definiendo contexto...");
            this.context = context;

            Log.i("Singleton.inicializar()", "Cargando archivo de configuración...");
            this.configuracion = new Properties();
            AssetManager assetManager = context.getAssets();

            try {
                InputStream inputStream = assetManager.open(Singleton.NOMBRE_ARCHIVO_CONF);
                configuracion.load(inputStream);
            } catch (IOException e) {
                Log.e("Singleton.inicializar()", "Error al cargar el archivo '" + NOMBRE_ARCHIVO_CONF + "' con la configuración del programa: " + e.toString());
            }

            Log.i("Singleton.inicializar()", "Instanciando manejadorPersistencia...");
            this.manejadorPersistencia = new ManejadorPersistencia(this);

        } else
            Log.w("Singleton.inicializar()", "Está tratanto de volver a inicializar un Singleton ya inicializado!");

    }

    public Context darContexto() {
        return this.context;
    }

    public Properties darConfiguracion() {
        return this.configuracion;
    }

// -----------------------------------------------------
// MÉTODOS INTERFAZ
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
