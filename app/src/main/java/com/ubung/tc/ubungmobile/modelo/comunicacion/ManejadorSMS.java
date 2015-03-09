package com.ubung.tc.ubungmobile.modelo.comunicacion;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.ubung.tc.ubungmobile.modelo.Singleton;

/**
 * Created by cvargasc on 9/03/15.
 */
public class ManejadorSMS extends BroadcastReceiver {

    private static final String LOG_NAME = "ManejSMS";

    private Singleton singleton;

    public ManejadorSMS(Singleton singleton) {
        this.singleton = singleton;
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
            }
        }
    }
}
