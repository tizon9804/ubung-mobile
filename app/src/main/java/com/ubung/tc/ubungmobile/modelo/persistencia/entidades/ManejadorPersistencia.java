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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by cvargasc on 1/03/15.
 */
public class ManejadorPersistencia extends SQLiteOpenHelper implements InterfazPersistencia {

// -----------------------------------------------------
// CONSTANTES
// -----------------------------------------------------
    private static final String LOG_NAME = "persistencia";

    private static final int VERSION_BD = 1;
    private static final String NOMBRE_BD = "ubungBD";
    private static final String SCRIPT_INICIAL = "inicializarBD";

    protected static final String ID = "id";
// DEPORTES --------------------------------------------
    protected static final String TABLA_DEPORTES = "Deportes";
// USUARIOS --------------------------------------------
    protected static final String TABLA_USUARIOS = "Usuarios";
    protected static final String CAMPO_USUARIOS_NOMBRE = "nombreUsuario";
    protected static final String CAMPO_USUARIOS_DEPORTE = "idDeporte";
// ZONAS -----------------------------------------------
    protected static final String TABLA_ZONAS = "Zonas";
// EVENTOS ---------------------------------------------
    protected static final String TABLA_EVENTOS = "Eventos";
    protected static final String CAMPO_EVENTOS_FECHAHORA = "fechaHora";
    protected static final String CAMPO_EVENTOS_IDZONA = "idZona";
    protected static final String CAMPO_EVENTOS_IDDEPORTE = "idDeporte";
    protected static final String CAMPO_EVENTOS_IDORGANIZADOR = "idOrganizador";
    protected static final String CAMPO_EVENTOS_FECHACREACION = "fechaCreacion";
// INSCRITOS EVENTO ------------------------------------
    protected static final String TABLA_INSCRITOSEVENTO = "InscritosEvento";
    protected static final String CAMPO_INSCRITOSEVENTO_IDEVENTO = "idEvento";
    protected static final String CAMPO_INSCRITOSEVENTO_IDINSCRITO = "idInscrito";
    protected static final String CAMPO_INSCRITOSEVENTO_FECHAINSCRIP = "fechaInscripcion";

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

    /**
     * Permite ejecutar una consulta plana sobre la base de datos
     * @param consulta que se realizará sobre la base de datos
     * @return Cursor con el resultado de la consulta
     */
    protected Cursor consultaPlana(String consulta) {
        SQLiteDatabase bdLectura = this.getReadableDatabase();
        return bdLectura.rawQuery(consulta, null);
    }

    /**
     * Devuelve todos los registros de la tabla pasada como parámetro
     * @param tabla de la cual se quieren recuparar los registros
     * @return cursor con el resultado de la consulta
     */
    protected Cursor consulta(String tabla) {
        String consulta = "SELECT * FROM " + tabla;
        SQLiteDatabase bdLectura = this.getReadableDatabase();
        return bdLectura.rawQuery(consulta, null);
    }

    /**
     * Devuelve el registro de la tabla pasada como parámetro cuyo identificador es el pasado
     * como parámetro.
     * @param tabla de la cual se quieren recuparar el registro
     * @param id del registro que se desea recuperar
     * @return cursor con el registro solicitado
     */
    protected Cursor consulta(String tabla, int id) {
        String consulta = "SELECT * FROM " + tabla + " WHERE "+ID+"="+ id;
        SQLiteDatabase bdLectura = this.getReadableDatabase();
        return bdLectura.rawQuery(consulta, null);
    }

    /**
     * Devuelve los registros de la tabla que coinciden con el valor en el campo
     * @param tabla sobre la cual se realizará la consulta
     * @param campo el campo de la clausura WHERE
     * @param valor el valor que debe tomar el campo
     * @return curson con los registros que coinciden con la búsqueda.
     */
    protected Cursor consulta(String tabla, String campo, String valor) {
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

    // CREATE ------------------------------------------
    @Override
    public int crearUsuario(String nombreUsuario, Deporte deporte) throws ExcepcionPersistencia {
        if (this.darUsuario(nombreUsuario) != null)
            throw new ExcepcionPersistencia("Ya existe un usuario con nombre "+nombreUsuario);

        ContentValues contentValues = new ContentValues();
        contentValues.put(CAMPO_USUARIOS_NOMBRE, nombreUsuario);
        contentValues.put(CAMPO_USUARIOS_DEPORTE, deporte.getId());

        long resultado = this.getWritableDatabase().insertOrThrow(TABLA_USUARIOS,null,contentValues);
        if (resultado == -1) throw new ExcepcionPersistencia("Se presentó un error no especificado al " +
                "insertar el usuario "+nombreUsuario+" en la base de datos");
        Log.i(LOG_NAME+"crearUsuari","Se ha creado el usuario "+nombreUsuario+" con id "+resultado);
        return (int) resultado;
    }

    @Override
    public int crearEvento(Date fechaHora, Zona zona, Deporte deporte, Usuario organizador)
            throws  ExcepcionPersistencia {

        ContentValues contentValues = new ContentValues();
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        contentValues.put(CAMPO_EVENTOS_FECHAHORA, formatoFecha.format(fechaHora));
        contentValues.put(CAMPO_EVENTOS_IDZONA, zona.getId());
        contentValues.put(CAMPO_EVENTOS_IDDEPORTE, deporte.getId());
        contentValues.put(CAMPO_EVENTOS_IDORGANIZADOR, organizador.getId());

        long resultado = this.getWritableDatabase().insertOrThrow(TABLA_EVENTOS,null,contentValues);
        if (resultado == -1) throw new ExcepcionPersistencia("Se presentó un error no especificado al " +
                "insertar el evento del "+formatoFecha.format(fechaHora)+" en la zona "+
                zona.getNombre()+" en la base de datos");
        Log.i(LOG_NAME+"crearEvento","Se ha creado el evento ("+formatoFecha.format(fechaHora)+","+
                zona.getNombre()+") con id "+resultado);
        return (int) resultado;
    }

    // READ   ------------------------------------------
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
    public ArrayList<Usuario> darUsuarios() {
        Cursor cursor = consulta(TABLA_USUARIOS);
        ArrayList<Usuario> resultado = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                resultado.add(new UsuarioLazyLoad(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), this));
            } while (cursor.moveToNext());
        }
        Log.i(LOG_NAME+"darUsuarios","Se recuperaron "+cursor.getCount()+" usuarios de la base de datos");
        return resultado;
    }

    @Override
    public Usuario darUsuario(int id) {
        Cursor cursor = consulta(TABLA_USUARIOS, id);
        if (!cursor.moveToFirst()) return null;
        return new UsuarioLazyLoad(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), this);
    }

    @Override
    public Usuario darUsuario(String nombreUsuario) {
        Cursor cursor = consulta(TABLA_USUARIOS, CAMPO_USUARIOS_NOMBRE, nombreUsuario);
        if(!cursor.moveToFirst()) return null;
        if(cursor.getCount() != 1) Log.e(LOG_NAME+"darUsuario", "Se ha recuperado más de un usuario " +
                "con nombre "+nombreUsuario);
        return new UsuarioLazyLoad(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), this);
    }

    @Override
    public ArrayList<Zona> darZonas() {
        Cursor cursor = consulta(TABLA_ZONAS);
        ArrayList<Zona> resultado = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                String[] latLongZoomBD = cursor.getString(2).split(":");
                double[] latLongZoom = {Double.parseDouble(latLongZoomBD[0]),Double.parseDouble(latLongZoomBD[1]),Double.parseDouble(latLongZoomBD[2])};
                resultado.add(new Zona(cursor.getInt(0), cursor.getString(1), latLongZoom, cursor.getInt(3)));
            } while (cursor.moveToNext());
        }
        Log.i(LOG_NAME+"darZonas","Se recuperaron "+cursor.getCount()+" zonas de la base de datos");
        return resultado; // Si la consulta no retorna registros devolverá el ArrayList vacío.
    }

    @Override
    public Zona darZona(int id) {
        Cursor cursor = consulta(TABLA_ZONAS,id);
        if (!cursor.moveToFirst()) return null;
        String[] latLongZoomBD = cursor.getString(2).split(":");
        double[] latLongZoom = {Double.parseDouble(latLongZoomBD[0]),Double.parseDouble(latLongZoomBD[1]),Double.parseDouble(latLongZoomBD[2])};
        return new Zona(cursor.getInt(0), cursor.getString(1), latLongZoom, cursor.getInt(3));
    }

    @Override
    public ArrayList<Evento> darEventos() {
        Cursor cursor = consulta(TABLA_EVENTOS);
        ArrayList<Evento> resultado = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Date fechaHora = null;
                Date fechaCreacion = null;
                try {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    fechaHora = dateFormat.parse(cursor.getString(1));
                    fechaCreacion = dateFormat.parse(cursor.getString(5));
                } catch (ParseException e) {
                    Log.e(LOG_NAME+"darEven(id)","No fue posible convertir string to date para el campo fechaHora");
                }
                resultado.add(new EventoLazyLoad(cursor.getInt(0), fechaHora, cursor.getInt(2),
                        cursor.getInt(3), cursor.getInt(4), fechaCreacion, this));
            } while (cursor.moveToNext());
        }
        Log.i(LOG_NAME+"darEventos","Se recuperaron "+cursor.getCount()+" eventos de la base de datos");
        return resultado;
    }

    @Override
    public ArrayList<Evento> darEventos(int idZona) {
        Cursor cursor = consulta(TABLA_EVENTOS,CAMPO_EVENTOS_IDZONA, ""+idZona);
        ArrayList<Evento> resultado = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Date fechaHora = null;
                Date fechaCreacion = null;
                try {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    fechaHora = dateFormat.parse(cursor.getString(1));
                    fechaCreacion = dateFormat.parse(cursor.getString(5));
                } catch (ParseException e) {
                    Log.e(LOG_NAME+"darEven(id)","No fue posible convertir string to date para el campo fechaHora");
                }
                resultado.add(new EventoLazyLoad(cursor.getInt(0), fechaHora, cursor.getInt(2),
                        cursor.getInt(3), cursor.getInt(4), fechaCreacion, this));
            } while (cursor.moveToNext());
        }
        Log.i(LOG_NAME+"darEventZon","Se recuperaron "+cursor.getCount()+" eventos para la zona "+idZona);
        return resultado;
    }

    @Override
    public Evento darEvento(int id) {
        Cursor cursor = consulta(TABLA_EVENTOS,id);
        if (!cursor.moveToFirst()) return null;

        Date fechaHora = null;
        Date fechaCreacion = null;
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            fechaHora = dateFormat.parse(cursor.getString(1));
            fechaCreacion = dateFormat.parse(cursor.getString(5));
        } catch (ParseException e) {
            Log.e(LOG_NAME+"darEven(id)","No fue posible convertir string to date para el campo fechaHora");
        }

        return new EventoLazyLoad(cursor.getInt(0), fechaHora, cursor.getInt(2),
                cursor.getInt(3), cursor.getInt(4), fechaCreacion, this);
    }

    // UPDATE ------------------------------------------
    @Override
    public Usuario actualizarUsuario(Usuario usuario) throws ExcepcionPersistencia {
        Log.i(LOG_NAME+"actualizUsu","Actualizando usuario ("+usuario.getId()+";"+usuario.getNombreUsuario()
                +";"+usuario.getDeporte().getNombre()+")");
        ContentValues contentValues = new ContentValues();
        contentValues.put(CAMPO_USUARIOS_NOMBRE, usuario.getNombreUsuario());
        contentValues.put(CAMPO_USUARIOS_DEPORTE, usuario.getDeporte().getId());
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        long resultado = sqLiteDatabase.update(TABLA_USUARIOS,contentValues,ID+"="+usuario.getId(),null);
        if (resultado != 1) {
            throw new ExcepcionPersistencia("Se actualizaron "+resultado+" filas en la tabla");
        }
        Usuario usuarioModificado = darUsuario(usuario.getId());
        Log.i(LOG_NAME+"actualizUsu","Usuario actualizado a ("+usuarioModificado.getId()+";"+usuario.getNombreUsuario()
                +";"+usuarioModificado.getDeporte().getNombre()+")");
        return usuarioModificado;
    }








    @Override
    public void actualizarEvento(Evento evento) throws ExcepcionPersistencia {

    }


}
