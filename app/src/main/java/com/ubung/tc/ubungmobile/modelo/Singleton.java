package com.ubung.tc.ubungmobile.modelo;


/*
Implementacion de los metodos de Ubung
 */

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.ubung.tc.ubungmobile.modelo.comunicacion.ManejadorSMS;
import com.ubung.tc.ubungmobile.modelo.excepciones.ExcepcionComunicacion;
import com.ubung.tc.ubungmobile.modelo.excepciones.ExcepcionPersistencia;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Deporte;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Evento;
import com.ubung.tc.ubungmobile.modelo.persistencia.ManejadorPersistencia;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Usuario;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Zona;

import java.util.ArrayList;
import java.util.Date;

public class Singleton implements Ubung {

// -----------------------------------------------------
// CONSTANTES
// -----------------------------------------------------
    public static final String LOG_NAME = "Singleton";

    // Constantes para la inicializar Parse SDK
    public static final String APPLICATION_ID = "kCmuY1ucbCPH9pRRZKQUcdTlEibuqzsVMsHrZVhJ";
    public static final String CLIENT_KEY = "tNELg4aMTozgwQm7WBatobNZJUOiYgbHUQbJ0PBl";

// -----------------------------------------------------
// ATRIBUTOS
// -----------------------------------------------------
    private static Singleton singleton = new Singleton();
    private Context context;

    private Usuario propietario;

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
    public void inicializar(Context context) throws ParseException {
        if (this.context == null) {
            Log.i(LOG_NAME+".inicializar()", System.currentTimeMillis()+" Inicializando Singleton...");
            Log.i(LOG_NAME+".inicializar()", "Definiendo contexto...");
            this.context = context;

            Log.i(LOG_NAME+".inicializar()", "Instanciando manejadorPersistencia...");
            manejadorPersistencia = new ManejadorPersistencia();

            Log.i(LOG_NAME+".inicializar()", "Inicializando com.parse SDK...");
            inicializarParseSDK();

            Log.i(LOG_NAME+"inicializar()", "Instanciando y registrando ManejadorSMS (BroadcastReceiver)...");
            inicializarModuloSMS();

            Log.i(LOG_NAME+".inicializar()", "Inicializando manejadorPersistencia...");
            manejadorPersistencia.inicializar();

            Log.i(LOG_NAME+".inicializar()", "Recuperando la información del usuario...");
            propietario = (Usuario)ParseUser.getCurrentUser();
            if (propietario == null) Log.w(LOG_NAME+".inicializar()", "Usuario no encontrado...");
            else Log.i(LOG_NAME+".inicializar()", "Usuario encontrado, restableciendo la información de ("+propietario.getObjectId()+";"+propietario.getUsername()+";"+propietario.getDeporte().getNombre()+")");

        } else {
            Log.w(LOG_NAME + ".inicializar()", System.currentTimeMillis() + " Está tratanto de volver a inicializar un Singleton ya inicializado!");
        }
    }

    private void inicializarParseSDK() {
        // Persistencia
        Parse.enableLocalDatastore(this.context);
        manejadorPersistencia.registrarSubclasesParseObject();

        // Inicializar
        Parse.initialize(this.context, APPLICATION_ID, CLIENT_KEY);
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }

    private void inicializarModuloSMS() {
        manejadorSMS = new ManejadorSMS(this, manejadorPersistencia);
        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        this.context.registerReceiver(manejadorSMS,intentFilter);
    }

    public Context darContexto() {
        return this.context;
    }

// -----------------------------------------------------
// MÉTODOS INTERFAZ UBUNG
// -----------------------------------------------------
    @Override
    public Usuario darPropietario() {
        return propietario;
    }

    @Override
    public void modificarPropietario(String nombreUsuario, Long numCelular, Deporte deporte) throws ParseException {
        if (nombreUsuario != null) {
            Log.i(LOG_NAME+".modifProp","Actualizando nombre del propietario desde "+propietario.getUsername()
                    +" a "+nombreUsuario);
            propietario.setUsername(nombreUsuario);
        }
        if (deporte != null) {
            Log.i(LOG_NAME+".modifProp","Actualizando deporte del propietario desde "+propietario.getDeporte().getNombre()
                    +" a "+deporte.getNombre());
            this.propietario.setDeporte(deporte);
        }
        if (numCelular != null) {
            Log.i(LOG_NAME+".modifProp","Actualizando celular del propietario desde "+propietario.getNumCelular()
                    +" a "+numCelular);
            propietario.setNumCelular(numCelular);
        }
    }

    @Override
    public void logIn(final String nombreUsuario, String contrasena) {
        Log.w(LOG_NAME+".logIn","Intentando logear al usuario "+nombreUsuario+"...");
        ParseUser.logInInBackground(nombreUsuario,contrasena,new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (parseUser != null) {
                    propietario = (Usuario) parseUser;
                    //ToDo Manejar estos textos en la forma adecuada con el XML
                    notificarUsuario("Usuario "+propietario.getNombreUsuario()+" ha iniciado sesión correctamente");
                    Log.i(LOG_NAME+".logIn","Usuario "+propietario.getNombreUsuario()+" logueado exitosamente...");
                } else {
                    //ToDo Manejar estos textos en la forma adecuada con el XML
                    notificarUsuario("Falló el intento de inicio de sesión para "+nombreUsuario);
                    Log.e(LOG_NAME+".logIn","Error al procesar el login de "+nombreUsuario+" :: "+e.getMessage());
                }
            }
        });
    }

    @Override
    public void registrarNuevoUsuario(String nombreUsuario, String contrasena) {
        Log.w(LOG_NAME+".registNuevUsu","Intentando registrar al usuario "+nombreUsuario+"...");
        try {
            propietario = new Usuario(nombreUsuario,contrasena);
            //ToDo Manejar estos textos en la forma adecuada con el XML
            notificarUsuario("El usuario "+propietario.getNombreUsuario()+" se registró exitosamente!");
            Log.i(LOG_NAME+".registNuevUsu","Usuario "+nombreUsuario+" registrado exitosamente...");
        } catch (ParseException e) {
            //ToDo Manejar estos textos en la forma adecuada con el XML
            notificarUsuario("No fue posible crear la cuenta de usuario para "+nombreUsuario);
            Log.e(LOG_NAME+".registNuevUsu","Error al registrar el usuario "+nombreUsuario+" :: "+e.getMessage());
        }
    }

    @Override
    public void crearEvento(Date fechaHora, Zona zona, Deporte deporte) throws ParseException {
        Evento evento = new Evento(fechaHora,  zona, deporte);
        evento.inscribirUsuarioAEvento(propietario);
        notificarUsuario("Se ha creado el evento");
        Log.i(LOG_NAME+"crearEven", "Creado evento "+evento.getId()+" e inscrito propietario como participante");
    }

    @Override
    public void inscribirseEvento(String idEvento) throws ParseException, ExcepcionComunicacion {
        Evento evento = manejadorPersistencia.darEvento(idEvento);
        evento.inscribirUsuarioAEvento(propietario);
        // Verificando si hay conectividad para determinar si se debe o no enviar el mensaje de texto
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean estoyConectado = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (!estoyConectado) {
            manejadorSMS.notificarUsuarioRemoto(evento);
            //ToDo Manejar estos textos en la forma adecuada con el XML
            notificarUsuario("Se ha notificado al organizador del evento por medio de SMS");
        }
    }

    public void notificarUsuario(String mensaje) {
        Log.i(LOG_NAME+"notifUsuario","Se le ha mostrado al usuario el mensaje: "+mensaje);
        Toast.makeText(context,mensaje,Toast.LENGTH_LONG).show();
    }

// -----------------------------------------------------
// MÉTODOS INTERFAZ PERSISTENCIA
// -----------------------------------------------------

    @Override
    public ArrayList<Deporte> darDeportes() throws ParseException {
        return manejadorPersistencia.darDeportes();
    }

    @Override
    public Deporte darDeporte(String id) throws ParseException {
        return manejadorPersistencia.darDeporte(id);
    }

    @Override
    public ArrayList<Usuario> darUsuarios() throws ParseException {
        return manejadorPersistencia.darUsuarios();
    }

    @Override
    public Usuario darUsuario(String id) throws ParseException {
        return manejadorPersistencia.darUsuario(id);
    }

    @Override
    public Usuario buscarUsuario(String nombreUsuario) throws ParseException {
        return manejadorPersistencia.darUsuario(nombreUsuario);
    }

    @Override
    public ArrayList<Zona> darZonas() throws ParseException {
        return manejadorPersistencia.darZonas();
    }

    @Override
    public Zona darZona(String id) throws ParseException {
        return manejadorPersistencia.darZona(id);
    }

    //@Override
    public void actualizarEvento(Evento evento) throws ExcepcionPersistencia {
        manejadorPersistencia.actualizarEvento(evento);
    }

    @Override
    public ArrayList<Evento> darEventos() throws ParseException {
        return manejadorPersistencia.darEventos();
    }

    @Override
    public ArrayList<Evento> buscarEventos(String idZona) throws ParseException {
        return manejadorPersistencia.buscarEventos(idZona);
    }

    @Override
    public Evento darEvento(String id) throws ParseException {
        return manejadorPersistencia.darEvento(id);
    }
}
