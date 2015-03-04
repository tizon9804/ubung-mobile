package com.ubung.tc.ubungmobile.modelo.persistencia.entidades;

import android.content.ContentValues;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ubung.tc.ubungmobile.modelo.Singleton;
import com.ubung.tc.ubungmobile.modelo.excepciones.ExcepcionPersistencia;
import com.ubung.tc.ubungmobile.modelo.persistencia.InterfazPersistencia;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by cvargasc on 1/03/15.
 */
public class ManejadorPersistencia extends SQLiteOpenHelper implements InterfazPersistencia {

// -----------------------------------------------------
// CONSTANTES
// -----------------------------------------------------
    private static final String LOG_NAME = "persistencia";

    private static final String NOMBRE_BD = "ubungBD";
    private static final int VERSION_BD = 1;
    private static final String SCRIPT_INICIAL = "inicializarBD";

    private static final String ID = "id";

    private static final String TABLA_DEPORTES = "Deportes";

    private static final String TABLA_USUARIOS = "Usuarios";
    private static final String CAMPO_USUARIOS_NOMBRE = "nombreUsuario";
    private static final String CAMPO_USUARIOS_DEPORTE = "idDeporte";

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
// MÉTODOS PRIVADOS / PROTECTED
// -----------------------------------------------------

    private Cursor consulta(String tabla) {
        String consulta = "SELECT * FROM " + tabla;
        SQLiteDatabase bdLectura = this.getReadableDatabase();
        return bdLectura.rawQuery(consulta, null);
    }

    private Cursor consulta(String tabla, int id) {
        String consulta = "SELECT * FROM " + tabla + " WHERE "+ID+"="+ id;
        SQLiteDatabase bdLectura = this.getReadableDatabase();
        return bdLectura.rawQuery(consulta, null);
    }

    private Cursor consulta(String tabla, String campo, String valor) {
        String consulta = "SELECT * FROM " + tabla + " WHERE "+campo+"='" + valor+"'";
        SQLiteDatabase bdLectura = this.getReadableDatabase();
        return bdLectura.rawQuery(consulta, null);
    }

// -----------------------------------------------------
// MÉTODOS OVERRIDE SQLiteOpenHelper
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
// -----------------------------------------------------
// MÉTODOS INTERFAZ InterfazPersistencia
// -----------------------------------------------------
    @Override
    public Deporte darDeporte(int id) {
        Cursor cursor = consulta(TABLA_DEPORTES, id);
        if (!cursor.moveToFirst()) return null;
        return new Deporte(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
    }

    @Override
    public ArrayList<Deporte> darDeportes() {
        Cursor cursor = consulta(TABLA_DEPORTES);
        ArrayList<Deporte> resultado = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                resultado.add(new Deporte(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));
            } while (cursor.moveToNext());
        }
        Log.i(LOG_NAME+"darDeportes","Se recuperaron "+cursor.getCount()+" deportes de la base de datos");
        return resultado; // Si la consulta no retorna registros devolverá el ArrayList vacío.
    }

    @Override
    public int crearUsuario(Usuario usuario) throws ExcepcionPersistencia {
        if (this.darUsuario(usuario.getNombreUsuario()) != null)
            throw new ExcepcionPersistencia("Ya existe un usuario con nombre "+usuario.getNombreUsuario());

        ContentValues contentValues = new ContentValues();
        contentValues.put(CAMPO_USUARIOS_NOMBRE, usuario.getNombreUsuario());
        contentValues.put(CAMPO_USUARIOS_DEPORTE, usuario.getDeporte().getId());

        long resultado = this.getWritableDatabase().insertOrThrow(TABLA_USUARIOS,null,contentValues);
        if (resultado == -1) throw new ExcepcionPersistencia("Se presentó un error no especificado al " +
                "insertar el usuario "+usuario.getNombreUsuario()+" en la base de datos");
        Log.i(LOG_NAME+"crearUsuari","Se ha creado el usuario "+usuario.getNombreUsuario()+" con id "+resultado);
        return (int) resultado;
    }

    @Override
    public void actualizarUsuario(Usuario usuario) throws ExcepcionPersistencia {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CAMPO_USUARIOS_NOMBRE, usuario.getNombreUsuario());
        contentValues.put(CAMPO_USUARIOS_DEPORTE, usuario.getDeporte().getId());
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        long resultado = sqLiteDatabase.update(TABLA_USUARIOS,contentValues,ID+"="+usuario.getId(),null);
        if (resultado != 1) {
            throw new ExcepcionPersistencia("Se actualizaron "+resultado+" filas en la tabla");
        }
    }

    @Override
    public ArrayList<Usuario> darUsuarios() {
        Cursor cursor = consulta(TABLA_USUARIOS);
        ArrayList<Usuario> resultado = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                resultado.add(new Usuario(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), this));
            } while (cursor.moveToNext());
        }
        Log.i(LOG_NAME+"darUsuarios","Se recuperaron "+cursor.getCount()+" usuarios de la base de datos");
        return resultado;
    }

    @Override
    public Usuario darUsuario(int id) {
        Cursor cursor = consulta(TABLA_USUARIOS, id);
        if (!cursor.moveToFirst()) return null;
        return new Usuario(cursor.getInt(0), cursor.getString(1), this.darDeporte(cursor.getInt(2)));
    }

    @Override
    public Usuario darUsuario(String nombreUsuario) {
        Cursor cursor = consulta(TABLA_USUARIOS, CAMPO_USUARIOS_NOMBRE, nombreUsuario);
        if(!cursor.moveToFirst()) return null;
        if(cursor.getCount() != 1) Log.e(LOG_NAME+"darUsuario", "Se ha recuperado más de un usuario " +
                "con nombre "+nombreUsuario);
        return new Usuario(cursor.getInt(0), cursor.getString(1), this.darDeporte(cursor.getInt(2)));
    }
}
