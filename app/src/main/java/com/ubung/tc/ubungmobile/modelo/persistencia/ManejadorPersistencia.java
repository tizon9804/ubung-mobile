package com.ubung.tc.ubungmobile.modelo.persistencia;

import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ubung.tc.ubungmobile.modelo.Singleton;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Deporte;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Evento;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Usuario;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Zona;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by cvargasc on 1/03/15.
 */
public class ManejadorPersistencia extends SQLiteOpenHelper {

    // -----------------------------------------------------
// CONSTANTES
// -----------------------------------------------------
    private static final String LOG_NAME = "persistencia";

    private static final String NOMBRE_BD = "ubungBD";
    private static final int VERSION_BD = 1;
    private static final String SCRIPT_INICIAL = "inicializarBD";

    private static final String TABLA_DEPORTES = "Deportes";

    // -----------------------------------------------------
// ATRIBUTOS
// -----------------------------------------------------
    private Singleton singleton;

    // -----------------------------------------------------
// CONSTRUCTOR
// -----------------------------------------------------
    public ManejadorPersistencia(Singleton singleton) {
        super(singleton.darContexto(), NOMBRE_BD, null, VERSION_BD);
        this.singleton = singleton;

    }

// -----------------------------------------------------
// MÉTODOS PÚBLICOS
// -----------------------------------------------------
    public Deporte darDeporte(int id) {
        Cursor cursor = consulta(TABLA_DEPORTES, id);
        if (!cursor.moveToFirst()) return null;
        return new Deporte(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
    }

    public Evento darEvento(int id) {

        return null;
    }

    public Usuario darUsuario(int id) {

        return null;
    }

    public Zona darZona(int id) {

        return null;
    }

    public ArrayList<Deporte> darDeportes() {
        Cursor cursor = consulta(TABLA_DEPORTES);
        ArrayList<Deporte> resultado = new ArrayList<Deporte>();
        if (cursor.moveToFirst()) {
            do {
                resultado.add(new Deporte(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));
            } while (cursor.moveToNext());
        }
        return resultado; // Si la consulta no retorna registros devolverá el ArrayList vacío.
    }

    public ArrayList<Zona> darZonas() {

        return null;
    }

// -----------------------------------------------------
// MÉTODOS PRIVADOS
// -----------------------------------------------------

    private Cursor consulta(String tabla) {
        String consulta = "SELECT * FROM " + tabla;
        SQLiteDatabase bdLectura = this.getReadableDatabase();
        return bdLectura.rawQuery(consulta, null);
    }

    private Cursor consulta(String tabla, int id) {
        String consulta = "SELECT * FROM " + tabla + " WHERE id=" + id;
        SQLiteDatabase bdLectura = this.getReadableDatabase();
        return bdLectura.rawQuery(consulta, null);
    }

    // -----------------------------------------------------
// MÉTODOS OVERRIDE
// -----------------------------------------------------
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(LOG_NAME + ".onCreate()", "Creando base de datos local por primera vez...");

        String scriptSQL = singleton.darConfiguracion().getProperty(SCRIPT_INICIAL);
        AssetManager assetManager = singleton.darContexto().getAssets();

        try {
            InputStream inputStream = assetManager.open(scriptSQL);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String consulta;
            while ((consulta = bufferedReader.readLine()) != null) {
                db.execSQL(consulta);
            }
        } catch (IOException e) {
            Log.e(LOG_NAME + ".onCreate()", "Error al cargar " + SCRIPT_INICIAL + ": " + e.toString());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
