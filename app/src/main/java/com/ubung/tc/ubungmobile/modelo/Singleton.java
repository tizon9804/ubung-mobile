package com.ubung.tc.ubungmobile.modelo;


/*
Implementacion de los metodos de Ubung
 */

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.ubung.tc.ubungmobile.modelo.persistencia.ManejadorPersistencia;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Deporte;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
}
