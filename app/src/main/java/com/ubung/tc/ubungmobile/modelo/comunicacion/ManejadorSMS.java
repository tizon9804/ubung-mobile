package com.ubung.tc.ubungmobile.modelo.comunicacion;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.ubung.tc.ubungmobile.modelo.Singleton;
import com.ubung.tc.ubungmobile.modelo.excepciones.ExcepcionPersistencia;
import com.ubung.tc.ubungmobile.modelo.persistencia.ManejadorPersistencia;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Evento;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.Usuario;

/**
 * Created by cvargasc on 9/03/15.
 */
public class ManejadorSMS extends BroadcastReceiver {

    private static final String LOG_NAME = "ManejSMS";

    private Singleton singleton;
    private ManejadorPersistencia manejadorPersistencia;

    public ManejadorSMS(Singleton singleton, ManejadorPersistencia manejadorPersistencia) {
        this.singleton = singleton;
        this.manejadorPersistencia = manejadorPersistencia;
    }

    private void notificarUsuario(String idRegistro, String idEvento, String idInscrito) {
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
                try {
                    String idEvento = protocolo[2];
                    String idInscrito = protocolo[3];
                    String idRegistro = manejadorPersistencia.agregarInscritoEvento(idEvento,idInscrito);
                    Log.w(LOG_NAME+"onRecei","idEvento="+idEvento+" idInscrito="+idInscrito);
                    notificarUsuario(idRegistro,idEvento,idInscrito);
                } catch (ExcepcionPersistencia e) {
                    Log.e(LOG_NAME+"onRecei","Error al persistir registro de usuario "+e.getMessage());
                }
            }
        }
    }
}
