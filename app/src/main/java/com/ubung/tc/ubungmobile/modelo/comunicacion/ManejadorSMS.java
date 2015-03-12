package com.ubung.tc.ubungmobile.modelo.comunicacion;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.ubung.tc.ubungmobile.modelo.Singleton;
import com.ubung.tc.ubungmobile.modelo.excepciones.ExcepcionPersistencia;
import com.ubung.tc.ubungmobile.modelo.persistencia.entidades.ManejadorPersistencia;

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
                    long idEvento = Long.parseLong(protocolo[2]);
                    long idInscrito = Long.parseLong(protocolo[3]);
                    long idRegistro = manejadorPersistencia.agregarInscritoEvento(idEvento,idInscrito);
                    Log.w(LOG_NAME+"onRecei","idEvento="+idEvento+" idInscrito="+idInscrito);
                    singleton.notificarUsuario(idRegistro,idEvento,idInscrito);
                } catch (ExcepcionPersistencia e) {
                    Log.e(LOG_NAME+"onRecei","Error al persistir registro de usuario "+e.getMessage());
                }
            }
        }
    }
}
