package com.ubung.tc.ubungmobile.modelo;


/*
Implementacion de los metodos de Ubung
 */

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.ubung.tc.ubungmobile.R;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Deporte;

import java.io.IOException;
import java.io.InputStream;
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
            Log.e("Singleton INFO", "Inicializando Singleton...");
            Log.e("Singleton INFO", "Definiendo contexto...");
            this.context = context;

            Log.e("Singleton INFO", "Cargando archivo de configuración...");
            this.configuracion = new Properties();
            AssetManager assetManager = context.getAssets();

            try {
                InputStream inputStream = assetManager.open(Singleton.NOMBRE_ARCHIVO_CONF);
                configuracion.load(inputStream);
            } catch (IOException e) {
                Log.e("Singleton ERR", "Error al cargar el archivo '" + NOMBRE_ARCHIVO_CONF + "' con la configuración del programa: " + e.toString());
            }
        } else
            Log.e("Singleton WAR", "Está tratanto de volver a inicializar un Singleton ya inicializado!");

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
    public Integer[] getDeportes() {
        //ToDo: crear y retornar las imagenes de deportes;

        return new Integer[]{
                R.drawable.basket, R.drawable.futbol,
                R.drawable.basket, R.drawable.futbol,
                R.drawable.voleibol, R.drawable.voleibol
        };
    }

    @Override
    public Deporte getDeporte(int pos) {
        return null;
    }
}
