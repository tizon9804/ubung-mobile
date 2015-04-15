package com.ubung.tc.ubungmobile.modelo;


/*
Implementacion de los metodos de Ubung
 */

import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.ubung.tc.ubungmobile.modelo.comunicacion.ManejadorSMS;
import com.ubung.tc.ubungmobile.modelo.excepciones.ExcepcionComunicacion;
import com.ubung.tc.ubungmobile.modelo.excepciones.ExcepcionPersistencia;
import com.ubung.tc.ubungmobile.modelo.persistencia.Parse.ParseUsuario;
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

    // Nombres de los archivos de configuración
    public static final String ARCHIVO_CONF_GLB = "config.properties";
    public static final String ARCHIVO_CONF_LOC = "config";

    // Constantes para leer datos del archivo de configuración
    public static final String CONF_ID_PROPIETARIO = "idPropietario";
    public static final String CONF_CEL_PROPIETARIO = "celPropietario";

    // El protocolo para el mensaje SMS es ubung:idInscripcion:idEvento:idUsuario
    public static final String SMS_INSCR_EVENTO = "ubung";

    // Constantes para la inicializar Parse SDK
    public static final String APPLICATION_ID = "kCmuY1ucbCPH9pRRZKQUcdTlEibuqzsVMsHrZVhJ";
    public static final String CLIENT_KEY = "tNELg4aMTozgwQm7WBatobNZJUOiYgbHUQbJ0PBl";
    // -----------------------------------------------------
// ATRIBUTOS
// -----------------------------------------------------
    private static Singleton singleton = new Singleton();
    private Context context;
    private Properties configuracionGlobal;
    private SharedPreferences configuracionLocal;

    private Usuario propietario;
    private long numCelular;

    private ManejadorPersistencia manejadorPersistencia;
    private ManejadorSMS manejadorSMS;

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

            inicializarParseSDK();

            Log.i(LOG_NAME+"inicializar()", "Instanciando y registrando ManejadorSMS (BroadcastReceiver)...");
            manejadorSMS = new ManejadorSMS(this, manejadorPersistencia);
            IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
            this.context.registerReceiver(manejadorSMS,intentFilter);

            Log.i(LOG_NAME+".inicializar()", "Cargando configuración local...");
            configuracionLocal = context.getSharedPreferences(ARCHIVO_CONF_LOC,Context.MODE_PRIVATE);

            Log.i(LOG_NAME+".inicializar()", "Recuperando la información del usuario "+ configuracionLocal.getLong(CONF_ID_PROPIETARIO, -1)+"...");
            propietario = manejadorPersistencia.darUsuario(configuracionLocal.getLong(CONF_ID_PROPIETARIO, -1));
            numCelular = configuracionLocal.getLong(CONF_CEL_PROPIETARIO, -1);
            if (propietario == null) Log.w(LOG_NAME+".inicializar()", "Usuario no encontrado...");
            else Log.i(LOG_NAME+".inicializar()", "Usuario encontrado, restableciendo la información de ("+propietario.getNombreUsuario()+";"+propietario.getDeporte().getNombre()+")");

        } else {
            Log.w(LOG_NAME + ".inicializar()", System.currentTimeMillis() + " Está tratanto de volver a inicializar un Singleton ya inicializado!");
        }
    }

    private void inicializarParseSDK() {
        Log.i(LOG_NAME+".inicializar()", "Inicializando com.parse SDK...");
        Parse.enableLocalDatastore(this.context);
        Parse.initialize(this.context, APPLICATION_ID, CLIENT_KEY);
        ParseInstallation.getCurrentInstallation().saveInBackground();

        ParseObject.registerSubclass(ParseUsuario.class);

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
    public void modificarPropietario(String nombreUsuario, long numCelular, Deporte deporte) throws ExcepcionPersistencia {
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
                 //   ParseObject testObject = new ParseObject("ParseUsuario");
                 // testObject.put("id","1");
                 //  testObject.put("nombreUsuario", nombreUsuario);
                  //  testObject.saveInBackground();
            } else Log.i(LOG_NAME+".modifProp","Propietario ya existe como usuario! Solo fue necesario definirlo...");

            Editor editor = configuracionLocal.edit();
            editor.putLong(CONF_ID_PROPIETARIO, propietario.getId());
            editor.putLong(CONF_CEL_PROPIETARIO,numCelular);
            editor.commit();
            Log.i(LOG_NAME+".modifProp","Almacenando propietario en configuracion local...");
        }
    }

    @Override
    public long crearEvento(Date fechaHora, Zona zona, Deporte deporte) throws  ExcepcionPersistencia {
        long idEvento = manejadorPersistencia.crearEvento(fechaHora, zona, deporte, propietario, numCelular);
        manejadorPersistencia.agregarInscritoEvento(idEvento, propietario.getId());
        Log.i(LOG_NAME+"crearEven", "Creado evento "+idEvento+" e inscrito propietario como participante");
        return idEvento;
    }

    @Override
    public long inscribirseEvento(long idEvento) throws ExcepcionPersistencia, ExcepcionComunicacion {
        long idInscripcion = manejadorPersistencia.agregarInscritoEvento(idEvento, propietario.getId());
        Log.i(LOG_NAME+"inscrEve","Inscrito el propietario al evento "+idEvento);
        // Voy a notificar al creador del evento
        try {
            Evento evento = manejadorPersistencia.darEvento(idEvento);
            String mensaje = SMS_INSCR_EVENTO + ":" + idInscripcion+":"+evento.getId() + ":" + propietario.getId();
            Log.i(LOG_NAME+"inscrEve", "Enviando SMS '"+mensaje+"' a "+evento.getCelNotificacion());
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("" + evento.getCelNotificacion(),
                    null,
                    mensaje,
                    null,
                    null);
        } catch (Exception e) {
            throw new ExcepcionComunicacion("No fue posible enviar SMS "+e.getMessage());
        }
        return idInscripcion;
    }

    public void notificarUsuario(long idInscripcion, long idEvento, long idInscrito) {
        Evento evento = manejadorPersistencia.darEvento(idEvento);
        Usuario inscrito = manejadorPersistencia.darUsuario(idInscrito);

        String nombreUsuario = inscrito == null ? ""+idInscrito : inscrito.getNombreUsuario();
        String nombreZona = "ZONeventNull";
        String nombreDeporte = "DEPeventNull";
        if (evento != null) {
            nombreZona = evento.getZona() == null ? "zonaNoEncr" : evento.getZona().getNombre();
            nombreDeporte = evento.getDeporte() == null ? "deporNoEncr" : evento.getDeporte().getNombre();
        }

        String mensaje = "El usuario "+nombreUsuario+" se ha inscrito a tu evento "+nombreDeporte
                +" en "+nombreZona;
        Toast.makeText(context,mensaje,Toast.LENGTH_LONG).show();
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
