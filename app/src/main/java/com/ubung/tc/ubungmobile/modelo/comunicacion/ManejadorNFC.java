package com.ubung.tc.ubungmobile.modelo.comunicacion;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Build;
import android.os.Parcelable;
import android.provider.Settings;
import android.util.Log;

import com.ubung.tc.ubungmobile.controlador.DescripcionProgramacionActivity;
import com.ubung.tc.ubungmobile.controlador.MainUbungActivity;
import com.ubung.tc.ubungmobile.modelo.Singleton;
import com.ubung.tc.ubungmobile.modelo.excepciones.ExcepcionComunicacion;

/**
 * Created by cvargasc on 13/05/15.
 */
public class ManejadorNFC {

    public static final String LOG_NAME = "ManejadorNFC";

    private Singleton singleton;
    private NfcAdapter nfcAdapter;

    public ManejadorNFC(Singleton singleton) {
        this.singleton = singleton;
        this.nfcAdapter = NfcAdapter.getDefaultAdapter(singleton.darContexto());
    }

    public String recibirIdEvento(Intent intent) throws ExcepcionComunicacion {
        String accion = intent.getAction();
        Log.e(LOG_NAME+"recibirId","Recibiendo intent con acción  "+accion);
        if (accion.equals(NfcAdapter.ACTION_NDEF_DISCOVERED)) {
            Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage ndefMessage = (NdefMessage) parcelables[0];
            NdefRecord[] ndefRecords = ndefMessage.getRecords();
            NdefRecord ndefRecord = ndefRecords[0];
            return new String(ndefRecord.getPayload());
        } else {
            throw new ExcepcionComunicacion("La acción del Intent no corresponde al intercambio de un evento :: "+accion);
        }
    }

    public void definirNfcActivity(DescripcionProgramacionActivity activity) {
        nfcAdapter.setNdefPushMessageCallback(activity, activity);
        nfcAdapter.setOnNdefPushCompleteCallback(activity, activity);
    }

    public NdefMessage enviarIdEvento(String idEvento) {

        Log.e(LOG_NAME+"enviarId","Enviando evento "+idEvento);

        if (!nfcAdapter.isEnabled()) {
            singleton.darContexto().startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
        }
        if (!nfcAdapter.isNdefPushEnabled()) {
            singleton.darContexto().startActivity(new Intent(Settings.ACTION_NFCSHARING_SETTINGS));
        }
        byte[] bytesOut = idEvento.getBytes();

        NdefRecord ndefRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA,
                "text/plain".getBytes(),
                new byte[]{},
                bytesOut);
        NdefMessage ndefMessage = new NdefMessage(ndefRecord);
        return ndefMessage;
    }
}
