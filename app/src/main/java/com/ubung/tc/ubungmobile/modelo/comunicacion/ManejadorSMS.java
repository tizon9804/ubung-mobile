package com.ubung.tc.ubungmobile.modelo.comunicacion;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import com.parse.ParseException;
import com.ubung.tc.ubungmobile.modelo.Singleton;
import com.ubung.tc.ubungmobile.modelo.excepciones.ExcepcionComunicacion;
import com.ubung.tc.ubungmobile.modelo.persistencia.ManejadorPersistencia;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Evento;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Usuario;

/**
 * Created by cvargasc on 9/03/15.
 */
public class ManejadorSMS extends BroadcastReceiver {

    private static final String LOG_NAME = "ManejSMS";
    // El protocolo para el mensaje SMS es ubung:idInscripcion:idEvento:idUsuario
    public static final String SMS_INSCR_EVENTO = "ubung";

    private Singleton singleton;
    private ManejadorPersistencia manejadorPersistencia;

    public ManejadorSMS(Singleton singleton, ManejadorPersistencia manejadorPersistencia) {
        this.singleton = singleton;
        this.manejadorPersistencia = manejadorPersistencia;
    }

    private void notificarUsuarioLocal(String idEvento, String idInscrito) throws ParseException {
        Evento evento = manejadorPersistencia.darEvento(idEvento);
        Usuario inscrito = manejadorPersistencia.darUsuario(idInscrito);
        String nombreUsuario = inscrito == null ? ""+idInscrito : inscrito.getNombreUsuario();
        String nombreZona = "ZONeventNull";
        String nombreDeporte = "DEPeventNull";

        if (evento != null) {
            nombreZona = evento.getZona() == null ? "zonaNoEncr" : evento.getZona().getNombre();
            nombreDeporte = evento.getDeporte() == null ? "deporNoEncr" : evento.getDeporte().getNombre();
        }

        //ToDo Manejar estos textos en la forma adecuada con el XML
        String mensaje = "El usuario "+nombreUsuario+" se ha inscrito a tu evento "+nombreDeporte
                +" en "+nombreZona;
        singleton.notificarUsuario(mensaje);
    }

    public void notificarUsuarioRemoto(Evento evento) throws ExcepcionComunicacion {
        String mensaje = SMS_INSCR_EVENTO +":"+evento.getId()+":"+singleton.darPropietario().getId();
        // Voy a notificar al creador del evento
        try {
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
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        if(bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            for (int i = 0; i < pdus.length; i++) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
                String mensaje = smsMessage.getDisplayMessageBody();
                Log.i(LOG_NAME+"onRecei","Se ha recibido el SMS "+mensaje+" desde "+smsMessage.getDisplayOriginatingAddress());
                String[] protocolo = mensaje.split(":");
                String idEvento = protocolo[1];
                String idInscrito = protocolo[2];
                Log.w(LOG_NAME+"onRecei","idEvento="+idEvento+" idInscrito="+idInscrito);
                try {
                    notificarUsuarioLocal(idEvento, idInscrito);
                } catch (ParseException e) {
                    Log.e(LOG_NAME+"onRecei","Error al notificar usuario "+e.getMessage());
                }
            }
        }
    }
}
