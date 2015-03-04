package com.ubung.tc.ubungmobile.modelo.persistencia;

import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ubung.tc.ubungmobile.modelo.Singleton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by cvargasc on 3/03/15.
 */
public class ManejadorBD extends SQLiteOpenHelper {

    // -----------------------------------------------------
// CONSTANTES
// -----------------------------------------------------
    private static final String NOMBRE_BD = "ubungBD";
    private static final int VERSION_BD = 1;
    private static final String SCRIPT_INICIAL = "inicializarBD";

    // -----------------------------------------------------
// ATRIBUTOS
// -----------------------------------------------------
    private Singleton singleton;

    // -----------------------------------------------------
// CONSTRUCTOR
// -----------------------------------------------------
    public ManejadorBD(Singleton singleton) {
        super(singleton.darContexto(), NOMBRE_BD, null, VERSION_BD);
        this.singleton = singleton;
    }

    // -----------------------------------------------------
// MÉTODOS
// -----------------------------------------------------
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("manejadorBD.onCreate()", "Creando base de datos local por primera vez...");

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
            Log.e("manejadorBD.onCreate()", "Error al cargar " + SCRIPT_INICIAL + ": " + e.toString());
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
