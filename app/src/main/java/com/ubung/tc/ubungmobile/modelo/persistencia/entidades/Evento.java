package com.ubung.tc.ubungmobile.modelo.persistencia.entidades;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.ubung.tc.ubungmobile.modelo.persistencia.ManejadorPersistencia;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by cvargasc on 15/04/15.
 */
@ParseClassName(ManejadorPersistencia.EVENTO)
public class Evento extends ParseObject {
// -----------------------------------------------------
// CONSTANTES
// -----------------------------------------------------
    public static final String LOG_NAME = "Evento";
// -----------------------------------------------------
// CONSTANTES PARA LAS LLAVES DE LOS ATRIBUTOS
// -----------------------------------------------------
    private static final String USUARIO_ORGANIZADOR =  "usuarioOrganizador";
    private static final String USUARIOS_INSCRITOS = "usuariosInscritos";
    private static final String FECHA_HORA_EVENTO = "fechaHoraEvento";
    public static final String ZONA = "zona";
    private static final String DEPORTE = "deporte";

    // Prefijo para el canal de notificaciones asociado al evento
    public static final String PREFIJO_CANAL = "evento"; // El canal será el prefijo+ID del evento
// -----------------------------------------------------
// CONSTRUCTORES
// -----------------------------------------------------
    public Evento() {
        super();
    }

    public Evento(final Usuario organizador, Date fechaHora, Zona zona, Deporte deporte) throws ParseException {
        put(USUARIO_ORGANIZADOR, organizador);
        put(FECHA_HORA_EVENTO, fechaHora);
        put(ZONA,zona);
        put(DEPORTE,deporte);
        saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                inscribirUsuarioAEvento(organizador);
            }
        });
    }

// -----------------------------------------------------
// MÉTODOS
// -----------------------------------------------------
    public void inscribirUsuarioAEvento(final Usuario usuario) {
        Log.i(LOG_NAME + "inscUsEvent", "Inscribiendo "+usuario.getNombreUsuario()+" a evento "+getId());
        ParseRelation<Usuario> inscritosEvento = getRelation(USUARIOS_INSCRITOS);
        inscritosEvento.add(usuario);
        saveEventually();
        Log.i(LOG_NAME + "inscUsEvent", "Suscribiendo al canal del evento: " + PREFIJO_CANAL + getId());
        ParsePush.subscribeInBackground(PREFIJO_CANAL+getId(), new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.i(LOG_NAME + "inscUsEvent", "Se ha inscrito al canal del evento.");
                try {
                    ParsePush parsePush = new ParsePush();
                    parsePush.setChannel(PREFIJO_CANAL+getId());
                    String mensaje = usuario.getNombreUsuario()+" se inscribio a "
                            + getDeporte().getNombre()+" el "
                            + DateFormat.getDateTimeInstance().format(getFechaHoraEvento())+" en "
                            + getZona().getNombre();
                    Log.i(LOG_NAME + "inscUsEvent", "Preparando mensaje: " + mensaje);
                    //ToDo Manejar estos textos en la forma adecuada con el XML
                    parsePush.setMessage(mensaje);
                    //parsePush.setQuery(ParseInstallation.getQuery());
                    parsePush.send();
                    Log.i(LOG_NAME + "inscUsEvent", "Mensaje publicado en el canal");
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

// -----------------------------------------------------
// GETTERS
// -----------------------------------------------------
    public String getId() {
        return getObjectId();
    }

    public Deporte getDeporte() throws ParseException {
        return getParseObject(DEPORTE).fetch();
    }

    public Zona getZona() throws ParseException {
        return getParseObject(ZONA).fetch();
    }

    public Date getFechaHoraEvento() {
        return getDate(FECHA_HORA_EVENTO);
    }

    public Usuario getUsuarioOrganizador() throws ParseException {
        return getParseObject(USUARIO_ORGANIZADOR).fetch();
    }

    public long getCelNotificacion() throws ParseException {
        return getUsuarioOrganizador().getNumCelular();
    }

    public ArrayList<Usuario> getUsuariosInscritos() throws ParseException {
        ParseRelation<Usuario> inscritosEvento = getRelation(USUARIOS_INSCRITOS);
        ParseQuery<Usuario> query = inscritosEvento.getQuery();
        return new ArrayList<>(query.find());
    }
}
